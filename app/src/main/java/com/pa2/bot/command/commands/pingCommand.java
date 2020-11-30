package com.pa2.bot.command.commands;

import com.pa2.bot.command.CommandContext;
import com.pa2.bot.command.ICommands;
import net.dv8tion.jda.api.JDA;

public class pingCommand implements ICommands {

    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                .sendMessageFormat("Reset ping: %sms\nWS ping: %sms", ping, jda.getGatewayPing())
                .queue()
        );
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getHelp() {
        return "Shows the current ping from the bot to the discord servers.";
    }
}
