package dev.felnull.imp.client.nmusic.media;

import com.google.common.collect.ImmutableList;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.client.lava.LavaPlayerManager;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.music.resource.ImageInfo;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class YoutubeMusicMedia extends LavaPlayerBaseMusicMedia {
    private static final Component YT_ENTER_TEXT = Component.translatable("imp.text.enterText.youtube");

    protected YoutubeMusicMedia(String name) {
        super(name);
    }

    @Override
    public void registerSourceManager(AudioPlayerManager audioPlayerManager) {
        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());
    }

    @Override
    public boolean match(AudioTrack track) {
        return track.getSourceManager() instanceof YoutubeAudioSourceManager;
    }

    @Override
    public boolean isSearchable() {
        return true;
    }

    @Override
    protected ImageInfo createThumbnail(AudioTrack track) {
        return new ImageInfo(ImageInfo.ImageType.YOUTUBE_THUMBNAIL, track.getIdentifier());
    }

    @Override
    public List<MusicMediaResult> search(String searchText) {
        var lm = LavaPlayerManager.getInstance();
        List<AudioTrack> tracks;

        try {
            tracks = LavaPlayerUtil.searchYoutube(lm.getAudioPlayerManager(), searchText);
        } catch (ExecutionException | InterruptedException e) {
            return ImmutableList.of();
        }

        return tracks.stream().map(this::createResult).toList();
    }

    @Override
    public Component getEnterText() {
        return YT_ENTER_TEXT;
    }
}
