package com.pa2.bot.command.commands.music;

import com.pa2.bot.command.CommandContext;
import com.pa2.bot.command.ICommands;
import com.pa2.bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class playCommand implements ICommands {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();
        final List<String> args = ctx.getArgs();

        if (args.isEmpty()){
            channel.sendMessage("You need to put a youtube link").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel()){
            channel.sendMessage("I need to be in a voice channel, try first with `join`").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState voiceState = member.getVoiceState();

        if (!voiceState.inVoiceChannel()){
            channel.sendMessage("You need to be in a voice channel for this command work").queue();
            return;
        }

        if (!voiceState.getChannel().equals(selfVoiceState.getChannel())){
            channel.sendMessage("You need to be at the same voice channel as me").queue();
            return;
        }

        String link = String.join(" ", ctx.getArgs());

        if (!isUrl(link)){
            link = "ytsearch:"+link;
        }

        PlayerManager.getINSTANCE()
                .loadAndPlay(channel, link);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Plays a song\n" +
                "Usage: `;play <youtube link>`";
    }

    private boolean isUrl(String link){
        try {
            new URI(link);
            return true;
        }catch (URISyntaxException e){
            return false;
        }
    }
}
