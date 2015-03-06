package com.ashlimeianwarren.saaf.Implementation;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.ashlimeianwarren.saaf.Framework.Capture;

import java.io.File;
import java.io.IOException;

import java.util.Date;


/**
 * Created by Ash on 20/02/2015.
 */
public class MediaCapture implements Capture
{

    private MediaPlayer player = null;
    private static String fileName = null;
    private MediaRecorder recorder = null;
    private static final String LOG_TAG = "com.ashlimeianwarren.saaf";
    private File myImageFile = null;
    private Uri myPicture = null;
    private File myVideoFile = null;
    private Uri myVideo = null;
    Activity activity;

    /**
     * @param ac
     */
    public MediaCapture(Activity ac)
    {
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        String datetime = new Date().toString();
        datetime = datetime.replace(" ", "");
        datetime = datetime.replace(":", "");
        fileName += ("/" + datetime);
        activity = ac;

    }

    /**
     *
     */
    @Override
    public String captureSound()
    {

        String ext = ".3gp";
        fileName += ext;
        //Initialising the recorder object
        recorder = new MediaRecorder();
        //Setting the input source, Microphone
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //Setting the format of the audio
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        //Assigning the filename as the output file
        Log.i(LOG_TAG, fileName);
        recorder.setOutputFile(fileName);
        //Setting the Encoder
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try
        {
            recorder.prepare();
        } catch (IOException e)
        {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();

        return fileName;
    }

    /**
     *
     */
    @Override
    public String captureImage()
    {

        fileName += ".jpg";
        myImageFile = new File(fileName);
        myImageFile.delete();
        myPicture = Uri.fromFile(myImageFile);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, myPicture);

        return fileName;
    }

    @Override
    public String captureVideo()
    {
        fileName += ".mp4";
        myVideoFile = new File(fileName);
        myVideoFile.delete();
        myVideo = Uri.fromFile(myVideoFile);
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, myVideo);
        return fileName;
    }

    public void stopCaptureSound()
    {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    public void onRecord(boolean start)
    {
        if (start)
        {
            captureSound();
        } else
        {
            stopCaptureSound();
        }
    }
}
