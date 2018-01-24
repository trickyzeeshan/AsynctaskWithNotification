package com.solitians.jishan.asynctaskwithnotification;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
private static final int PERMISSION_REQUEST_CODE=1;
Button button;
String file ="https://firebasestorage.googleapis.com/v0/b/jalan-7e161.appspot.com/o/82317.jpg?alt=media&token=0f7731d2-3eda-4707-83c6-22521c6bb6a5";
String file1="https://api.androidhive.info/progressdialog/hive.jpg";
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= 15)
        {
            if (checkPermission())
            {
            } else {
                requestPermission();
            }
        }
        else
        {
        }
    button=findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this,Result.class);
                startActivity(i);
            }
        });
        Button btn = (Button) findViewById(R.id.button1);

        btn.setOnClickListener(new View.OnClickListener() {

//            @TargetApi(11)
//            private void asyncTaskApi11() {
//                new NotificationTask(getApplicationContext(), "Download", 2)
//                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 5);
//            }

            @Override
            public void onClick(View v) {

                    new NotificationTask(getApplicationContext(), 2)
                            .execute(file);
            }
        });
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                break;
        }
    }

}