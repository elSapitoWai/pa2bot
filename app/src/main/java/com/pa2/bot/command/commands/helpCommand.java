package com.pa2.bot.command.commands;

import com.pa2.bot.CommandManager;
import com.pa2.bot.Config;
import com.pa2.bot.command.CommandContext;
import com.pa2.bot.command.ICommands;
import net.dv8tion.jda.api.entities.TextChannel;

import com.pa2.bot.veryBadDesign;
import java.util.Arrays;
import java.util.List;

public class helpCommand implements ICommands {

    CommandManager manager;

    public helpCommand(CommandManager manager){
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if(args.isEmpty()){
            StringBuilder builder = new StringBuilder();
            String prefix = veryBadDesign.PREFIXES.get(ctx.getGuild().getIdLong());

            builder.append("List of commands\n");

            manager.getCommands().stream().map(ICommands::getName).forEach(
                    (it) -> builder.append('`').append(prefix).append(it).append("`\n")
            );

            channel.sendMessage(builder.toString()).queue();
            return;
        }

        String search = args.get(0);
        ICommands command = manager.getCommand(search);

        if (command == null){
            channel.sendMessage("Nothing found for " + search).queue();
            return;
        }

        channel.sendMessage(command.getHelp()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows the list with commands in the bot\n" +
                "Usage: `;help [comand]`";
    }

    public List<String> getAliases(){
        return Arrays.asList("commands", "cmds", "commandlist");
    }
}
