package com.pa2.bot.command.commands.music;

import com.pa2.bot.command.CommandContext;
import com.pa2.bot.command.ICommands;
import com.pa2.bot.lavaplayer.GuildMusicManager;
import com.pa2.bot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.duncte123.botcommons.commands.ICommandContext;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class skipCommand implements ICommands {
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

        if (audioPlayer.getPlayingTrack() == null){
            return;
        }

        musicManager.scheduler.nextTrack();
        channel.sendMessage("Skipped the current track").queue();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "skips the current song.";
    }
}
