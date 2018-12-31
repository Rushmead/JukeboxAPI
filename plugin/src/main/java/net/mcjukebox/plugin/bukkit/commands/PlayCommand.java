package net.mcjukebox.plugin.bukkit.commands;

import lombok.AllArgsConstructor;
import net.mcjukebox.plugin.bukkit.api.JukeboxAPI;
import net.mcjukebox.plugin.bukkit.api.ResourceType;
import net.mcjukebox.plugin.bukkit.api.models.Media;
import net.mcjukebox.plugin.bukkit.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.util.HashMap;

@AllArgsConstructor
public class PlayCommand extends JukeboxCommand {

    private ResourceType type;

    @Override
    public boolean execute(CommandSender dispatcher, String[] args) {
        if (args.length < 2) return false;

        String url = args[1];
        Media toPlay = new Media(type, url);

        if (args.length >= 3) {
            JSONObject options = jsonFromArgs(args, 2);

            if (options == null) {
                dispatcher.sendMessage(ChatColor.RED + "Unable to parse options as JSON.");
                return true;
            }

            toPlay.loadOptions(options);
        }

        if (args[0].startsWith("@")) {
            JukeboxAPI.getShowManager().getShow(args[0]).play(toPlay);
        } else {
            Player player = Bukkit.getPlayer(args[0]);
            if (player != null) {
                JukeboxAPI.play(player, toPlay);
            } else {
                HashMap<String, String> findAndReplace = new HashMap<String, String>();
                findAndReplace.put("user", args[1]);
                MessageUtils.sendMessage(dispatcher, "command.notOnline", findAndReplace);
            }
        }

        return true;
    }

}
