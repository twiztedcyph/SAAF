package com.ashlimeianwarren.saaf.Implementation;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.ashlimeianwarren.saaf.Framework.FileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * AndroidFileIO class.
 */
public class AndroidFileIO implements FileIO
{
    Context context;
    AssetManager assets;
    String externalStoragePath;

    /**
     * Constructor for the AndroidFileIO class.
     *
     * @param context The current context of the app.
     */
    public AndroidFileIO(Context context)
    {
        this.context = context;
        this.assets = context.getAssets();
        this.externalStoragePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator;


    }

    /**
     * Get an InputStream for an asset to be read.
     *
     * @param file Path to the asset;
     * @return An ImputSteam for the asset to be read.
     * @throws IOException If the asset cannot be found or read.
     */
    @Override
    public InputStream readAsset(String file) throws IOException
    {
        return assets.open(file);
    }

    /**
     * Get an InputStream for a file to be read.
     *
     * @param file Path to the file.
     * @return An InputStream for the file to be read.
     * @throws IOException If the file cannot be found or read.
     */
    @Override
    public InputStream readFile(String file) throws IOException
    {
        return new FileInputStream(externalStoragePath + file);
    }

    /**
     * Get and OutputStream for a file to be written to.
     *
     * @param file Path to the file.
     * @return An OutputStream for the file to be written to.
     * @throws IOException If the file cannot be written to.
     */
    @Override
    public OutputStream writeFile(String file) throws IOException
    {
        return new FileOutputStream(externalStoragePath + file);
    }

    /**
     * Get the SharedPreferences for this application.
     *
     * @return The SharedPreferences for this application.
     */
    @Override
    public SharedPreferences getSharedPref()
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Get and OutputStream for a file to be written to.
     *
     * @param file Path to the file.
     * @return An OutputStream for the file to be written to.
     * @throws IOException If the file cannot be written to.
     */
    public OutputStream writeFile(File file) throws IOException
    {
        return new FileOutputStream(file);
    }
}
