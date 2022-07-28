package com.zj.audiomodule.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

/**
 * 作者：create by 张金 on 2022/6/8 15:14
 * 邮箱：564813746@qq.com
 */
public class AudioPlayerHelper {
    private static final String TAG="AudioPlayerHelper";
    private static MediaPlayer mMediaPlayer;

    public static void startPlayAudio(String path){
        try {
            File file=new File(path);
            if (!file.exists()){
                return;
            }
            stopMediaplayer();
            mMediaPlayer=new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void stopMediaplayer(){
        if (mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer=null;
        }
    }
}
