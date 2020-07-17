package com.lib.audio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.media2.player.MediaPlayer2;

import java.io.IOException;

public class AudioUtils {
    private static final String TAG="AudioUtils";
    public static void startPlayMedia(MediaPlayer mediaPlayer, String dataSource){
        try {
            mediaPlayer.setDataSource(dataSource);
            mediaPlayer.prepare();
            mediaPlayer.start();

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void stopPlayMedia(MediaPlayer mediaPlayer){
        mediaPlayer.release();
        mediaPlayer=null;
    }

    //Record media
    public static void recordMedia(MediaRecorder mediaRecorder,String outputFile){
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(outputFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        }catch (IOException e){
            Log.e(TAG,"recorder failed");
        }
        mediaRecorder.start();
    }

    public static void stopRecordMedia(MediaRecorder recorder){
        recorder.stop();
        recorder.release();
        recorder=null;
    }

    /*
     * 用户获取屏幕录制权限
     * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void getUserRecordPermission(Activity activity,int requestCode){
        MediaProjectionManager mediaProjectionManager= (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (mediaProjectionManager != null){
            Intent intent=mediaProjectionManager.createScreenCaptureIntent();
            PackageManager packageManager=activity.getPackageManager();
            if (packageManager.resolveActivity(intent,PackageManager.MATCH_DEFAULT_ONLY) != null){
                activity.startActivityForResult(intent,requestCode);
            }else {
                Toast.makeText(activity, "系统不存在这个Activity", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
