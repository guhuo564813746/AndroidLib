package com.lib.audio.activitis;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.lib.audio.ScreenRecordManager;
import com.lib.audio.service.AudioBrowserService;
import com.lib.audio.service.ScreenRecordService;
import com.lib.code.R;

public class MediaTestActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvStart;
    private TextView mTvEnd;
    private TextView mTvTime;
    private static final int REQUEST_AUDIO = 200;
    private static final int REQUEST_RECORD = 400;
    private MediaBrowserCompat mediaBrowser;
    private MediaRecorder mediaRecorder = null;
    private MediaPlayer mediaPlayer = null;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private ServiceConnection mServiceConnection;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_record_lay);
        mTvStart = findViewById(R.id.tv_start);
        mTvStart.setOnClickListener(this);
        mTvTime = findViewById(R.id.tv_record_time);
        mTvEnd = findViewById(R.id.tv_end);
        mTvEnd.setOnClickListener(this);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_AUDIO);
//        mediaBrowser=new MediaBrowserCompat (this,new ComponentName(this, AudioBrowserService.class),
//                connectionCallbacks,null);
//        mediaRecorder=new MediaRecorder();
//        mediaPlayer=new MediaPlayer();
        startScreenRecordService();
    }

    private void startScreenRecordService(){
        mServiceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ScreenRecordService.RecordBinder recordBinder= (ScreenRecordService.RecordBinder) service;
                ScreenRecordService screenRecordService=recordBinder.getRecordService();
                ScreenRecordManager.setScreenRecordService(screenRecordService);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent=new Intent(this,ScreenRecordService.class);
        bindService(intent,mServiceConnection,BIND_AUTO_CREATE);
        ScreenRecordManager.addRecordListeners(recordListener);
    }

    private ScreenRecordManager.RecordListener recordListener=new ScreenRecordManager.RecordListener() {
        @Override
        public void onStartRecord() {

        }

        @Override
        public void onPauseRecord() {

        }

        @Override
        public void onResumeRecord() {

        }

        @Override
        public void onStopRecord(String stopTips) {
            Toast.makeText(MediaTestActivity.this, stopTips, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRecording(String timeTips) {
            mTvTime.setText(timeTips);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUDIO:
                if (ActivityCompat.checkSelfPermission(MediaTestActivity.this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    //todo
                } else {
                    Toast.makeText(this, "录音权限被拒绝了", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    //todo
                } else {
                    Toast.makeText(this, "手机状态权限被拒绝了", Toast.LENGTH_LONG).show();
                }
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //todo
                } else {
                    Toast.makeText(this, "sd卡写权限被拒绝了", Toast.LENGTH_LONG).show();
                }

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //todo
                } else {
                    Toast.makeText(this, "sd读权限被拒绝了", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaBrowser.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
//        SimpleExoPlayer
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (MediaControllerCompat.getMediaController(MediaTestActivity.this) != null){
//            MediaControllerCompat.getMediaController(MediaTestActivity.this).unregisterCallback(connectionCallbacks);
//        }
        mediaBrowser.disconnect();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECORD && resultCode == RESULT_OK) {
            //录制屏幕
            try {
                ScreenRecordManager.setUpData(resultCode,data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "禁止录制", Toast.LENGTH_SHORT).show();
        }
    }

    private final MediaBrowserCompat.ConnectionCallback connectionCallbacks = new MediaBrowserCompat.ConnectionCallback() {
        @Override
        public void onConnected() {
            super.onConnected();
        }

        @Override
        public void onConnectionSuspended() {
            super.onConnectionSuspended();
        }

        @Override
        public void onConnectionFailed() {
            super.onConnectionFailed();
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start: {
                ScreenRecordManager.startScreenRecord(this, REQUEST_RECORD);
                break;
            }

            case R.id.tv_end: {
                ScreenRecordManager.stopRecord();
                break;
            }

        }
    }
}
