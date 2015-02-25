package com.ashlimeianwarren.saaf.Implementation;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.ashlimeianwarren.saaf.Framework.Capture;

import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.util.Date;
/**
 * Created by Ash on 20/02/2015.
 */
public class MediaCapture implements Capture {
    private MediaPlayer player = null;
    private static String fileName = null;
    private MediaRecorder recorder = null;
    private static final String LOG_TAG = "com.ashlimeianwarren.saaf";

    public MediaCapture(){
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        String datetime=new Date().toString();
        datetime =datetime.replace(" ", "");
        datetime =datetime.replace(":", "");
        String ext = ".3gp";
        fileName += ("/"+datetime + ext);

    }

    @Override
    public void captureSound() {

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

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();


    }

    @Override
    public void captureImage() {

    }

    @Override
    public void captureVideo() {

    }

    public void stopCaptureSound() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    public void onRecord(boolean start) {
        if (start) {
            captureSound();
        } else {
            stopCaptureSound();
        }
    }
}
