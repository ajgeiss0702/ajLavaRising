package us.ajg0702.lavarising.commands.subcommands;

import org.bukkit.entity.Player;
import us.ajg0702.commands.CommandSender;
import us.ajg0702.commands.SubCommand;
import us.ajg0702.lavarising.LavaPlugin;

import java.util.Collections;
import java.util.List;

import static us.ajg0702.lavarising.LavaPlugin.message;

public class Start extends SubCommand {
    private final LavaPlugin plugin;
    public Start(LavaPlugin plugin) {
        super("start", Collections.emptyList(), null, "Start or resume the lava rising");
        this.plugin = plugin;
    }

    @Override
    public List<String> autoComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void execute(CommandSender sender, String[] args, String label) {
        if(!sender.isPlayer()) {
            sender.sendMessage(message("<red>Must be in-game!"));
            return;
        }

        Player player = (Player) sender.getHandle();

        boolean startedBefore = plugin.getLavaManager().isSetup();

        plugin.getLavaManager().start(player.getLocation());

        if(startedBefore) {
            sender.sendMessage(message("<green>Resumed the lava rising!"));
        } else {
            sender.sendMessage(message("<green>Started the lava rising!"));
        }
    }
}
