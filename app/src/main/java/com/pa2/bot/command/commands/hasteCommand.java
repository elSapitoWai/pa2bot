package com.pa2.bot.command.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pa2.bot.command.CommandContext;
import com.pa2.bot.command.ICommands;
import me.duncte123.botcommons.web.ContentType;
import me.duncte123.botcommons.web.WebParserUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.function.Consumer;

public class hasteCommand implements ICommands {
    private static final String HASTE_SERVER = "https://hastebin.com/";

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if(ctx.getArgs().isEmpty()){
            channel.sendMessage("Missing arguments\n" +
                    "Usage: `;haste <text>`").queue();
            return;
        }

        final String invoke = this.getName();
        final String contentRaw = ctx.getMessage().getContentRaw();
        final int index = contentRaw.indexOf(invoke) + invoke.length();
        final String body = contentRaw.substring(index).trim();

        this.cratePaste(body, (text ) -> channel.sendMessage(text).queue());
    }

    private void cratePaste(String text, Consumer<String> callback){
        Request request = WebUtils.defaultRequest()
                .post(RequestBody.create(text.getBytes()))
                .addHeader("Content-Type", ContentType.TEXT_HTML.getType())
                .url(HASTE_SERVER + "documents")
                .build();
        WebUtils.ins.prepareRaw(request, (r) -> WebParserUtils.toJSONObject(r, new ObjectMapper())).async(
                (json) -> {
                    String key = json.get("key").asText();

                    callback.accept(HASTE_SERVER + key);
                },
                (e) -> callback.accept("Error: " + e.getMessage())
        );
    }

    @Override
    public String getName() {
        return "haste";
    }

    @Override
    public String getHelp() {
        return "Posts some text to hastebin\n" +
                "Usage: `;haste <text>`";
    }
}
