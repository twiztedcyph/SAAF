package com.ashlimeianwarren.saaf.Framework;

/**
 * Interface for the Music class.
 */
public interface Music
{
    /**
     * Play music.
     */
    public void play();

    /**
     * Stop music.
     */
    public void stop();

    /**
     * Pause the music.
     */
    public void pause();

    /**
     * Set whether to loop the music.
     *
     * @param looping True to loop. False to not loop.
     */
    public void setLooping(boolean looping);

    /**
     * Set the volume for the music.
     *
     * @param volume The volume.
     */
    public void setVolume(float volume);

    /**
     * Check if the music is playing.
     *
     * @return True if the music is playing. False if not.
     */
    public boolean isPlaying();

    /**
     * Check if the music is stopped.
     *
     * @return True if the music is stopped. False if not.
     */
    public boolean isStopped();

    /**
     * Check if the music is set to loop.
     *
     * @return True if the music is set to loop. False if not.
     */
    public boolean isLooping();

    /**
     * Releases resources associated with this MediaPlayer object.
     */
    public void dispose();

    /**
     * Seek to the beginning of the music.
     */
    void seekBegin();
}