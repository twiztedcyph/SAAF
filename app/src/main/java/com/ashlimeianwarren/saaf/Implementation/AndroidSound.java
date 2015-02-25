package com.ashlimeianwarren.saaf.Implementation;

import android.media.SoundPool;

import com.ashlimeianwarren.saaf.Framework.Sound;

/**
 * AndroidSound class.
 * <p/>
 * Sounds are typically shorter than music objects.
 */
public class AndroidSound implements Sound
{
    int soundId;
    SoundPool soundPool;

    /**
     * Constructor for the AndroidSound class.
     *
     * @param soundPool Collection of samples loaded into memory.
     * @param soundId   The sound id.
     */
    public AndroidSound(SoundPool soundPool, int soundId)
    {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    /**
     * Play a sound.
     *
     * @param volume The volume to play at.
     */
    @Override
    public void play(float volume)
    {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    /**
     * Unload a sound.
     */
    @Override
    public void dispose()
    {
        soundPool.unload(soundId);
    }

}
