package com.zj.audiomodule.utils;

import android.media.AudioRecord;

import com.zj.audiomodule.audiomanager.RecordState;

public class AudioRecordHelper {
    private volatile RecordState state=RecordState.IDLE ;
    private AudioRecordThread audioRecordThread=null;

    public void startAudioRecord(){

    }
    public void stopAudioRecord(){

    }

    private class AudioRecordThread extends Thread{
        private AudioRecord audioRecord;
        private int bufferSize;
        AudioRecordThread(){
            bufferSize=AudioRecord.getMinBufferSize();
        }
    }
}
