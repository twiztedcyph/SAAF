package com.ashlimeianwarren.saaf.Framework;


/**
 * Interface class Audio for AndroidMusic and AndroidSound.
 * @version 1.0
 */
public interface Audio
{
    /**
     * Create an AndroidMusic object from a sound file.
     *
     * @param filename Path to the sound file.
     * @return An AndroidMusic object.
     */
    public com.ashlimeianwarren.saaf.Implementation.AndroidMusic createMusic(String filename);


    /**
     * Create an AndroidSound object from a sound file.
     *
     * @param filename Path to the sound file.
     * @return An AndroidSound object.
     */
    public com.ashlimeianwarren.saaf.Implementation.AndroidSound createSound(String filename);
}
