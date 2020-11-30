package com.pa2.bot;

import com.jagrosh.jdautilities.examples.command.PingCommand;
import com.pa2.bot.command.CommandContext;
import com.pa2.bot.command.ICommands;
import com.pa2.bot.command.commands.*;
import com.pa2.bot.command.commands.music.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommands> commands = new ArrayList<ICommands>();

    public CommandManager(){
        addCommand(new pingCommand());
        addCommand(new helpCommand(this));
        addCommand(new pasteComand());
        addCommand(new hasteCommand());
        addCommand(new kickCommand());
        addCommand(new memeCommand());
        addCommand(new wehookCommands());
        addCommand(new jokeCommand());
        addCommand(new instagramCommand());
        addCommand(new setPrefixCommand());
        addCommand(new minecraftCommand());

        // Music
        addCommand(new joinCommand());
        addCommand(new playCommand());
        addCommand(new stopCommand());
        addCommand(new skipCommand());
        addCommand(new playingCommand());
        addCommand(new queueCommand());
        addCommand(new repeatCommand());
        addCommand(new leaveCommand());
    }

    private void addCommand(ICommands cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound){
            try {
                throw new IllegalAccessException("A command with this name is alredy present");
            } catch (IllegalAccessException e) {
                Listener.LOGGER.info("An error ocurred: " + e);
            }
        }


        commands.add(cmd);
    }

    public List<ICommands> getCommands(){
        return commands;
    }

    @Nullable
    public ICommands getCommand(String search){
        String searchLower = search.toLowerCase();

        for (ICommands cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)){
                return cmd;
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event, String prefix){
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommands cmd = this.getCommand(invoke);

        if (cmd != null){
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }
}
