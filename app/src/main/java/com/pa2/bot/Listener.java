package com.pa2.bot;

import com.pa2.bot.database.SQLiteDataSource;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Listener extends ListenerAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()){
            return;
        }

        final long guildId = event.getGuild().getIdLong();
        String prefix = veryBadDesign.PREFIXES.computeIfAbsent(guildId, this::getPrefix);
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "shutdown") && user.getId().equals(Config.get("owner_id"))){
            LOGGER.info("Shutting down");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());

            return;
        }

        if (raw.startsWith(prefix)){
            manager.handle(event, prefix);
        }
    }

    private String getPrefix(long guildId){
        try (final PreparedStatement preparedStatement = SQLiteDataSource
        .getConnection()
        // language=SQLite
        .prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")){
            preparedStatement.setString(1, String.valueOf(guildId));
            try (final ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    return resultSet.getString("prefix");
                }
            }

            try(final PreparedStatement insertStatement = SQLiteDataSource.getConnection().prepareStatement
                    ("INSERT INTO guild_settings(guild_id) VALUES(?)")){
                insertStatement.setString(1, String.valueOf(guildId));

                insertStatement.execute();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Config.get("prefix");
    }
}
