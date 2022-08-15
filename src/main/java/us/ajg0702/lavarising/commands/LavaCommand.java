package us.ajg0702.lavarising.commands;

import net.kyori.adventure.text.Component;
import us.ajg0702.commands.BaseCommand;
import us.ajg0702.commands.CommandSender;
import us.ajg0702.commands.SubCommand;
import us.ajg0702.lavarising.LavaPlugin;
import us.ajg0702.lavarising.commands.subcommands.Pause;
import us.ajg0702.lavarising.commands.subcommands.Start;

import java.util.Arrays;
import java.util.List;

import static us.ajg0702.lavarising.LavaPlugin.message;

public class LavaCommand extends BaseCommand {
    private final LavaPlugin plugin;
    public LavaCommand(LavaPlugin plugin) {
        super("lavarising", Arrays.asList("ajlavarising", "lavarising", "lava"), "ajlavarising.manage", "Commands for ajRisingLava");
        this.plugin = plugin;

        addSubCommand(new Start(plugin));
        addSubCommand(new Pause(plugin));
    }

    @Override
    public List<String> autoComplete(CommandSender sender, String[] args) {
        return subCommandAutoComplete(sender, args);
    }

    @Override
    public void execute(CommandSender sender, String[] args, String label) {
        if(!checkPermission(sender)) {
            sender.sendMessage(message("<red>You don't have permission to do this!"));
        }
        if(subCommandExecute(sender, args, label)) return;

        sendHelp(sender, label, getSubCommands());
    }
    public static void sendHelp(CommandSender sender, String label, List<SubCommand> subCommands) {
        sender.sendMessage(message(""));
        for(SubCommand subCommand : subCommands) {
            if(!subCommand.showInTabComplete()) continue;
            String command = "/"+label+" "+subCommand.getName();
            sender.sendMessage(message(
                    "<hover:show_text:'<yellow>Click to start typing <gold>"+command+"'>" +
                            "<click:suggest_command:"+command+" >" +
                            "<gold>"+command+"<yellow> - "+subCommand.getDescription()+"" +
                            "</click>" +
                            "</hover>"
            ));
        }
    }
}
