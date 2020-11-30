package com.pa2.bot;

import com.pa2.bot.database.SQLiteDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class Bot {

    private Bot() throws LoginException, SQLException {
        SQLiteDataSource.getConnection();

        WebUtils.setUserAgent("Mozilla/5.0 pa2bot #5823 / pa2games#6968");
        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                .setColor(0x3775b3)
                .setFooter("PA2Bot")
        );

        JDABuilder.createDefault(
                Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_VOICE_STATES
        )
                .enableCache(CacheFlag.VOICE_STATE)
                .setActivity(Activity.watching(";help"))
                .addEventListeners(new Listener())
                .build();
    }

    public static void main(String[] args) throws LoginException, SQLException {
        new Bot();
    }

}
