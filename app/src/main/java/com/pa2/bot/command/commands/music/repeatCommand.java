package com.pa2.bot.command.commands.music;

import com.pa2.bot.command.CommandContext;
import com.pa2.bot.command.ICommands;
import com.pa2.bot.lavaplayer.GuildMusicManager;
import com.pa2.bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class repeatCommand implements ICommands {
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
        final boolean newRepeating = !musicManager.scheduler.repeating;

        musicManager.scheduler.repeating = newRepeating;

        channel.sendMessageFormat("The player has been set to **%s**", newRepeating ? "repeating" : "not repeating").queue();
    }

    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getHelp() {
        return "Repeats the current track";
    }
}
