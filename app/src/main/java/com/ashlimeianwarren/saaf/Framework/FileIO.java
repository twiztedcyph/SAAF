package com.ashlimeianwarren.saaf.Framework;

import android.content.SharedPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface class for file input and output.
 *
 * @version 1.0
 */
public interface FileIO
{
    /**
     * Get an InputStream for a file to be read.
     *
     * @param file Path to the file.
     * @return An InputStream for the file to be read.
     * @throws IOException If the file cannot be found or read.
     */
    public InputStream readFile(String file) throws IOException;

    /**
     * Get and OutputStream for a file to be written to.
     *
     * @param file Path to the file.
     * @return An OutputStream for the file to be written to.
     * @throws IOException If the file cannot be written to.
     */
    public OutputStream writeFile(String file) throws IOException;

    /**
     * Get an InputStream for an asset to be read.
     *
     * @param file Path to the asset;
     * @return An ImputSteam for the asset to be read.
     * @throws IOException If the asset cannot be found or read.
     */
    public InputStream readAsset(String file) throws IOException;

    /**
     * Get the SharedPreferences for this application.
     *
     * @return The SharedPreferences for this application.
     */
    public SharedPreferences getSharedPref();
}
