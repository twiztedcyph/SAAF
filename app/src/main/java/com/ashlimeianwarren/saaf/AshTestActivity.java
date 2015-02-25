package com.ashlimeianwarren.saaf;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v4.app.FragmentActivity;
import com.ashlimeianwarren.saaf.Implementation.MediaCapture;

import com.ashlimeianwarren.saaf.Implementation.MediaCapture;




public class AshTestActivity extends ActionBarActivity {

    boolean mStartRecording = true;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    MediaCapture sound;
    Button recordSoundButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ash_test);
        Button pictureButton = (Button) findViewById(R.id.takePictureButton);
        recordSoundButton = (Button) findViewById(R.id.recordButton);
        imageView = (ImageView) findViewById(R.id.imageView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ash_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void recordSound(View view)
    {
       if(mStartRecording == true) {
           sound = new MediaCapture();
           sound.onRecord(mStartRecording);
           mStartRecording = !mStartRecording;
           recordSoundButton.setText("Recording...");
       }else {
           sound.onRecord(mStartRecording);
           mStartRecording = true;
           recordSoundButton.setText("Record Sound");
       }
    }

    public void launchCamera(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            imageView.setImageBitmap(photo);
        }
    }
}
