package com.ashlimeianwarren.saaf.Framework;

/**
 * Interface for Sound.
 */
public interface Sound
{
    /**
     * Play a sound.
     *
     * @param volume The volume to play at.
     */
    public void play(float volume);

    /**
     * Unload a sound.
     */
    public void dispose();
}
