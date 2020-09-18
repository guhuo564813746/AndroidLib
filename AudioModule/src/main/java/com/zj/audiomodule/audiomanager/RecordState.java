package com.zj.audiomodule.audiomanager;

public enum  RecordState {

    IDLE(1),START(2),STOP(3);
    private int state;
    private RecordState(int state){
        this.state=state;
    }
    public int getState(){
        return this.state;
    }
}
