package com.zj.audiomodule.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import androidx.annotation.RequiresApi;

import com.zj.audiomodule.audiomanager.RecordState;
import com.zj.audiomodule.common.AudioConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecordHelper {
    private static final String TAG = "AudioRecordHelper";
    private static volatile RecordState state = RecordState.IDLE;
    private static AudioRecord audioRecord = null;
    private static AudioTrack audioTrack = null;
    private static MediaRecorder recorder;

    public static void startRecordAudioByMediaRecord(String audioFileName){
        File file =new File(audioFileName);
        if (file.exists()){
            file.delete();
        }
        if (recorder==null){
            recorder=new MediaRecorder();
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setOutputFile(audioFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        }catch (IOException e){
            Log.e(TAG,"startRecordAudioByMediaRecord--"+e.getMessage());
        }
        recorder.start();
    }


    public static void releaseRecordAudioByMediaRecord(String path){
        if (!TextUtils.isEmpty(path)){
            File file=new File(path);
            if (file.exists()){
                file.delete();
            }
        }
        if (recorder != null){
            recorder.release();
            recorder=null;
        }
    }

    public static void startAudioRecord(Context context, String audioName) {
        final int minBuffersize = AudioRecord.getMinBufferSize(AudioConfig.AUDIO_RATE, AudioConfig.AUDIO_CHANNEL, AudioConfig.AUDIO_FOMAT);
        if (audioRecord == null) {
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, AudioConfig.AUDIO_RATE,
                    AudioConfig.AUDIO_CHANNEL, AudioConfig.AUDIO_FOMAT, minBuffersize);
            final byte data[] = new byte[minBuffersize];
            final File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), audioName);
            if (!file.mkdirs()) {
                Log.d(TAG, "audio dir can't create");
            }
            if (file.exists()) {
                file.delete();
            }
            audioRecord.startRecording();
            state = RecordState.START;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileOutputStream os = null;
                    try {
                        os = new FileOutputStream(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (os != null) {
                        while (state == RecordState.START) {
                            int read = audioRecord.read(data, 0, minBuffersize);
                            if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                                try {
                                    os.write(data);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        try {
                            Log.d(TAG, "close audio files");
                            os.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();
        }
    }

    /*停止录音*/
    public void stopAudioRecord(String path) {
        if (!TextUtils.isEmpty(path)){
            File file=new File(path);
            if (file.exists()){
                file.delete();
            }
        }
        state = RecordState.STOP;
        /*释放资源*/
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    /*播放音频
     *使用stream 模式
     * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void playVoiceInStreamMode(Context context, String audioFile) {
        //声道配置
        int audioChannel = AudioFormat.CHANNEL_OUT_MONO;
        final int minBuffSize = AudioTrack.getMinBufferSize(AudioConfig.AUDIO_RATE, audioChannel, AudioConfig.AUDIO_FOMAT);
//        if (audioTrack == null){
        audioTrack = new AudioTrack(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build(), new AudioFormat.Builder().setSampleRate(AudioConfig.AUDIO_RATE)
                .setEncoding(AudioConfig.AUDIO_FOMAT)
                .setChannelMask(audioChannel).build(), minBuffSize, AudioTrack.MODE_STREAM
                , AudioManager.AUDIO_SESSION_ID_GENERATE);
        audioTrack.play();
//        }
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), audioFile);
        try {
            final FileInputStream is = new FileInputStream(file);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] tempBytes = new byte[minBuffSize];
                        //循环读取音频流

                        while (is.available() > 0) {
                            int readCount = is.read(tempBytes);
                            if (readCount == AudioTrack.ERROR_INVALID_OPERATION
                                    || readCount == AudioTrack.ERROR_BAD_VALUE) {
                                continue;
                            }
                            if (readCount != 0 && readCount != -1) {
                                audioTrack.write(tempBytes, 0, readCount);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 播放音频 static 模式
     * */
    private byte[] audioDatas = null;

    public void playInModeStatic(Context context, String audioName) {
        // static模式，需要将音频数据一次性write到AudioTrack的内部缓冲区
        final File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), audioName);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                FileInputStream is = null;
                try {
                    is = new FileInputStream(file);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    int readDataLenght = is.read();
                    while (readDataLenght != -1) {
                        out.write(readDataLenght);
                    }
                    audioDatas = out.toByteArray();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected void onPostExecute(Void aVoid) {
                audioTrack = new AudioTrack(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build(),new AudioFormat.Builder().setSampleRate(22050)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build(),audioDatas.length,AudioTrack.MODE_STATIC,AudioManager.AUDIO_SESSION_ID_GENERATE);
                audioTrack.write(audioDatas,0,audioDatas.length);
                audioTrack.play();
            }
        }.execute();

    }

    /*
     * 停止播放
     * */
    public void stopPlayAudio() {
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
        }
    }

}
