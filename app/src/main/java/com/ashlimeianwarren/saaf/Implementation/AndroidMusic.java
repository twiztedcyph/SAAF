package com.ashlimeianwarren.saaf.Implementation;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import com.ashlimeianwarren.saaf.Framework.Music;
import java.io.IOException;

/**
 * AndroidMusic class.
 *
 * Music is typically used for longer files.
 */
public class AndroidMusic implements Music, OnCompletionListener, OnSeekCompleteListener, OnPreparedListener, OnVideoSizeChangedListener
{
    private MediaPlayer mediaPlayer;
    private boolean isPrepared = false;

    /**
     * Constructor for the AndroidMusic class.
     *
     * @param assetDescriptor Descriptor of an entry in the AssetManager.
     */
    public AndroidMusic(AssetFileDescriptor assetDescriptor)
    {
        mediaPlayer = new MediaPlayer();
        try
        {
            mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
                    assetDescriptor.getStartOffset(),
                    assetDescriptor.getLength());
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);

        } catch (Exception e)
        {
            throw new RuntimeException("Couldn't load music");
        }
    }

    /**
     * Releases resources associated with this MediaPlayer object.
     */
    @Override
    public void dispose()
    {

        if (this.mediaPlayer.isPlaying())
        {
            this.mediaPlayer.stop();
        }
        this.mediaPlayer.release();
    }

    /**
     * Check if the music is set to loop.
     *
     * @return True if the music is set to loop. False if not.
     */
    @Override
    public boolean isLooping()
    {
        return mediaPlayer.isLooping();
    }

    /**
     * Check if the music is playing.
     *
     * @return True if the music is playing. False if not.
     */
    @Override
    public boolean isPlaying()
    {
        return this.mediaPlayer.isPlaying();
    }

    @Override
    public boolean isStopped()
    {
        return !isPrepared;
    }

    /**
     * Pause the music.
     */
    @Override
    public void pause()
    {
        if (this.mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
    }


    /**
     * Play music.
     */
    @Override
    public void play()
    {
        if (this.mediaPlayer.isPlaying())
        {
            return;
        }

        try
        {
            synchronized (this)
            {
                if (!isPrepared)
                {
                    mediaPlayer.prepare();
                }
                mediaPlayer.start();
            }
        } catch (IllegalStateException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Set whether to loop the music.
     *
     * @param looping True to loop. False to not loop.
     */
    @Override
    public void setLooping(boolean looping)
    {
        mediaPlayer.setLooping(looping);
    }

    /**
     * Set the volume for the music.
     *
     * @param volume The volume.
     */
    @Override
    public void setVolume(float volume)
    {
        mediaPlayer.setVolume(volume, volume);
    }

    /**
     * Stop music.
     */
    @Override
    public void stop()
    {
        if (this.mediaPlayer.isPlaying() == true)
        {
            this.mediaPlayer.stop();

            synchronized (this)
            {
                isPrepared = false;
            }
        }
    }

    /**
     * Called when the end of a media source is reached during playback.
     *
     * @param player the MediaPlayer that reached the end of the file
     */
    @Override
    public void onCompletion(MediaPlayer player)
    {
        synchronized (this)
        {
            isPrepared = false;
        }
    }

    /**
     * Seek to the beginning of the music.
     */
    @Override
    public void seekBegin()
    {
        mediaPlayer.seekTo(0);

    }

    /**
     * Not implemented.
     *
     * @param player MediaPlayer object.
     */
    @Override
    public void onPrepared(MediaPlayer player)
    {
        // TODO Auto-generated method stub
        synchronized (this)
        {
            isPrepared = true;
        }

    }

    /**
     * Not implemented.
     *
     * @param player MediaPlayer object.
     */
    @Override
    public void onSeekComplete(MediaPlayer player)
    {
        // TODO Auto-generated method stub

    }

    /**
     *
     *
     * @param player MediaPlayer object.
     * @param width New width.
     * @param height New Height.
     */
    @Override
    public void onVideoSizeChanged(MediaPlayer player, int width, int height)
    {
        // TODO Auto-generated method stub

    }
}
