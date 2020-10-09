package com.zj.audiomodule.utils;

import android.media.AudioRecord;

import com.zj.audiomodule.audiomanager.RecordState;

public class AudioRecordHelper {
    private volatile RecordState state=RecordState.IDLE ;

    public void startAudioRecord(){
        int minBuffersize=AudioRecord.getMinBufferSize();
    }
    public void stopAudioRecord(){

    }

}
