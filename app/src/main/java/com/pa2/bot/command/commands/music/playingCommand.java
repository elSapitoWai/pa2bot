package com.pa2.bot.command.commands.music;

import com.pa2.bot.command.CommandContext;
import com.pa2.bot.command.ICommands;
import com.pa2.bot.lavaplayer.GuildMusicManager;
import com.pa2.bot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class playingCommand implements ICommands {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()){
            channel.sendMessage("I need to be in a voice channel").queue();
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

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack playingTrack = audioPlayer.getPlayingTrack();

        if (audioPlayer.getPlayingTrack() == null){
            return;
        }

        final AudioTrackInfo info = playingTrack.getInfo();
        channel.sendMessageFormat("Now playing `%s` by `%s`, (Link: <%s>)", info.title, info.author, info.uri).queue();
    }

    @Override
    public String getName() {
        return "playing";
    }

    @Override
    public String getHelp() {
        return "Says what song is playing";
    }
}
