package com.pa2.bot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;
import sun.security.ec.point.ProjectivePoint;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer;
    private final MutableAudioFrame frame;

    public AudioPlayerSendHandler(AudioPlayer player){
        this.audioPlayer = player;
        this.buffer = ByteBuffer.allocate(1024); // <--------------------  1024
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer);
    }

    @Override
    public boolean canProvide() {
        return this.audioPlayer.provide(this.frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        final Buffer tmp = ((Buffer) this.buffer).flip();
        return (ByteBuffer) tmp;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
