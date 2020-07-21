package com.lib.audio.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.ScreenUtils;
import com.lib.audio.ScreenRecordManager;

import java.io.File;
import java.io.IOException;

public class ScreenRecordService extends Service implements Handler.Callback {
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private MediaRecorder mediaRecorder;
    private VirtualDisplay virtualDisplay;
    private boolean isRunning=false;
    private int recordWidth;
    private int recordHeight;
    private int mDpi;
    private int mResultCode;
    private Intent mResultData;
    //录制文件保存地址
    private String mRecordPath;
    private Handler mHandler;
    //录制的时长
    private int mRecordSeconds;
    private static final int MSG_COUNT_DOWNTYPE=110;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RecordBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        recordWidth= ScreenUtils.getScreenWidth();
        recordHeight=ScreenUtils.getScreenHeight();
        mDpi= (int) ScreenUtils.getScreenDensity();
        mediaRecorder=new MediaRecorder();
        mHandler=new Handler(Looper.getMainLooper(),this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean isReady(){
        return mediaProjection != null && mResultData != null;
    }

    public boolean isRecordRun(){
        return isRunning;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setResultData(int resultCode, Intent resultData){
        mResultCode=resultCode;
        mResultData=resultData;
        mediaProjectionManager= (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (mediaProjectionManager != null){
            mediaProjection=mediaProjectionManager.getMediaProjection(mResultCode,mResultData);
        }
    }
//暂停录制
    public void pauseRecord(){
        if (mediaRecorder != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaRecorder.pause();
            }
        }
    }

    //重新录制
    public void resumeRecord(){
        if (mediaRecorder != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaRecorder.resume();
            }
        }
    }

    //创建录屏窗口
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createVirtualDisPlay(){
        virtualDisplay=mediaProjection.createVirtualDisplay("MainCreen",recordWidth,recordHeight,mDpi
                , DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,mediaRecorder.getSurface(),null,null);
    }

    public String getmRecordPath(){
        return mRecordPath;
    }

    public String getSavePath(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }else {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clearAll(){
        if (mediaProjection != null){
            mediaProjection.stop();
            mediaProjection=null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clearRecordElement(){
        clearAll();
        if (mediaRecorder != null){
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder=null;
        }
        mResultData=null;
        isRunning=false;
    }

    public void setUpMediaRecord(){
        mRecordPath=getSavePath()+ File.separator+"Record_"+System.currentTimeMillis()+".mp4";
        if (mediaRecorder == null){
            mediaRecorder=new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setOutputFile(mRecordPath);
            mediaRecorder.setVideoSize(recordWidth,recordHeight);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setVideoEncodingBitRate((int) (recordWidth*recordHeight*3.6));
            mediaRecorder.setVideoFrameRate(20);
            try {
                mediaRecorder.prepare();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean startRecord(){
        if (isRunning){
            return false;
        }
        if (mediaProjection == null){
            mediaProjection=mediaProjectionManager.getMediaProjection(mResultCode,mResultData);
        }
        setUpMediaRecord();
        createVirtualDisPlay();
        mediaRecorder.start();
        //ScreenRecordManager todo
        ScreenRecordManager.startRecord();
        mHandler.sendEmptyMessageDelayed(MSG_COUNT_DOWNTYPE,1000);
        isRunning=true;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean stopRecord(String tips){
        if (!isRunning){
            return false;
        }
        isRunning=false;
        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder=null;
            virtualDisplay.release();
            mediaProjection.stop();
        }catch (Exception e){
            e.printStackTrace();
            mediaRecorder.release();
            mediaRecorder=null;
        }
        mediaProjection=null;
        mHandler.removeMessages(MSG_COUNT_DOWNTYPE);
        //ScreenRecordManager
        ScreenRecordManager.stopRecord("停止录制");
        if (mRecordSeconds <= 2){
            //删除录制文件
        }else {
            //通知系统图库更新

        }
        mRecordSeconds=0;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case MSG_COUNT_DOWNTYPE: {
                String str = null;
                boolean noEnough = true;
                //内存不足
                if (noEnough) {
                    stopRecord("内存不足");
                    mRecordSeconds = 0;
                    break;
                }
                mRecordSeconds++;
                int minute = 0, second = 0;
                if (mRecordSeconds >= 60) {
                    minute = mRecordSeconds / 60;
                    second = mRecordSeconds % 60;
                } else {
                    second = mRecordSeconds;
                }
                //更新界面录制情况--RecordManager
                ScreenRecordManager.onRecording("0"+minute+":"+(second<10?"0"+second:second));
                //时长限制
                if (mRecordSeconds < 3 * 60) {
                    mHandler.sendEmptyMessageDelayed(MSG_COUNT_DOWNTYPE, 1000);
                } else {
                    stopRecord("录制时间到");
                    mRecordSeconds = 0;
                }

                break;
            }
        }
        return true;
    }

    public class RecordBinder extends Binder{
        public ScreenRecordService getRecordService(){
            return ScreenRecordService.this;
        }
    }
}
