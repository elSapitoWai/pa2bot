package com.pa2.bot.command.commands;

import com.pa2.bot.command.CommandContext;
import com.pa2.bot.command.ICommands;

import java.util.Arrays;
import java.util.List;

public class ptosCommand implements ICommands {
    @Override
    public void handle(CommandContext ctx) {
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
        ctx.getChannel().sendMessage("pto").queue();
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getHelp() {
        return "Haha server go brrrrrrrr";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("pto", "ptos");
    }
}
