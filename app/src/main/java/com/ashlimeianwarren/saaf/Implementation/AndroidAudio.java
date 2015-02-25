package com.ashlimeianwarren.saaf.Implementation;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.ashlimeianwarren.saaf.Framework.Audio;

import java.io.IOException;

/**
 * AndroidAudio class.
 */
public class AndroidAudio implements Audio
{
    AssetManager assets;
    SoundPool soundPool;

    /**
     * Constructor for the AndroidAudio class.
     *
     * @param activity The current activity.
     */
    public AndroidAudio(Activity activity)
    {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
    }

    /**
     * Create an AndroidMusic object from a sound file.
     *
     * @param filename Path to the sound file.
     * @return An AndroidMusic object.
     */
    @Override
    public AndroidMusic createMusic(String filename)
    {
        try
        {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            return new AndroidMusic(assetDescriptor);
        } catch (IOException e)
        {
            throw new RuntimeException("Couldn't load music '" + filename + "'");
        }
    }

    /**
     * Create an AndroidSound object from a sound file.
     *
     * @param filename Path to the sound file.
     * @return An AndroidSound object.
     */
    @Override
    public AndroidSound createSound(String filename)
    {
        try
        {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            int soundId = soundPool.load(assetDescriptor, 0);
            return new AndroidSound(soundPool, soundId);
        } catch (IOException e)
        {
            throw new RuntimeException("Couldn't load sound '" + filename + "'");
        }
    }
}
