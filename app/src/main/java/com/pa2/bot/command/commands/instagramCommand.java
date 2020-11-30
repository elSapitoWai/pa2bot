package com.pa2.bot.command.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.pa2.bot.command.CommandContext;
import com.pa2.bot.command.ICommands;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Collections;
import java.util.List;

public class instagramCommand implements ICommands {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        if (args.isEmpty()){
            channel.sendMessage("You must provide a username to look up").queue();
            return;
        }

        final String usn = args.get(0);

        WebUtils.ins.getJSONObject("https://apis.duncte123.me/insta/" + usn).async((json) -> {
            if (!json.get("success").asBoolean()){
                channel.sendMessage(json.get("error").get("message").asText()).queue();
                return;
            }

            final JsonNode user = json.get("user");
            final String username = user.get("username").asText();
            final String ppu = user.get("profile_pic_url").asText();
            final String biography = user.get("biography").asText();
            final boolean is_private = user.get("is_private").asBoolean();
            final int followers = user.get("followers").get("count").asInt();
            final int uploads = user.get("uploads").get("count").asInt();
            final boolean is_verified = user.get("is_verified").asBoolean();

            EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                    .setTitle("Instagram info of " + username,  "https://www.instagram.com/" + username)
                    .setThumbnail(ppu)
                    .setDescription(String.format(
                            "**Private account:** %s\n**Bio:** %s\n**Followers:** %s\n**Uploads:** %s\n**Verified:** %s",
                            is_private,
                            biography,
                            followers,
                            uploads,
                            is_verified
                    ))
                    .setImage(getLatestImage(json.get("images")));

            channel.sendMessage(embed.build()).queue();
        });
    }

    @Override
    public String getName() {
        return "instagram";
    }

    @Override
    public String getHelp() {
        return "Shows instagram statistics of a user with the latest image\n" +
                "Usage: `;instagram <username>`";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("insta");
    }

    private String getLatestImage(JsonNode json){

        if (json.isArray()){
            return null;
        }

        if (json.size() == 0){
            return null;
        }

        return json.get(0).get("url").asText();
    }
}
