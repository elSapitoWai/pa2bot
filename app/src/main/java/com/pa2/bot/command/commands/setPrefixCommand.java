package com.pa2.bot.command.commands;

import com.pa2.bot.command.CommandContext;
import com.pa2.bot.command.ICommands;
import com.pa2.bot.database.SQLiteDataSource;
import com.pa2.bot.veryBadDesign;
import jdk.internal.dynalink.linker.LinkerServices;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.nio.channels.Channel;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class setPrefixCommand implements ICommands {
    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)
                || !member.hasPermission(Permission.ADMINISTRATOR)
                || !member.isOwner()){
            channel.sendMessage("You don't have sufficient permissions").queue();
            return;
        }

        if (args.isEmpty()){
            channel.sendMessage("Missing args").queue();
            return;
        }

        final String newPrefix = String.join("", args);
        updatePrefix(ctx.getGuild().getIdLong(), newPrefix);

        channel.sendMessageFormat("New prefix has been set to `%s`", newPrefix).queue();
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp() {
        return "Changes the prefix\n" +
                "Usage: `setPrefix <prefix>`";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("prefix", "changeprefix");
    }

    private void updatePrefix(long guildId, String newPrefix){
        veryBadDesign.PREFIXES.put(guildId, newPrefix);

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection().prepareStatement
                ("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {
            preparedStatement.setString(1, newPrefix);
            preparedStatement.setString(2, String.valueOf(guildId));

            preparedStatement.executeUpdate();
        }catch (SQLException e) { e.printStackTrace(); }
    }
}
