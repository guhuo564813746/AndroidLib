package com.lib.audio;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import com.lib.audio.service.ScreenRecordService;

import java.util.ArrayList;
import java.util.List;

public class ScreenRecordManager {
    private static ScreenRecordService screenRecordService;
    private static List<RecordListener> recordListeners=new ArrayList<>();
    private static List<OnPageRecordListener> pageRecordListeners=new ArrayList<>();
    //录制结束的提示语
    public static boolean isShowingRecordTips=false;

    /*
    * 判断系统版本录屏功能
    * */
    public static boolean isScreenRecordEnable(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void setScreenRecordService(ScreenRecordService service){
        screenRecordService=service;
    }

    public static void clear(){
        if (isScreenRecordEnable() && screenRecordService != null){
            screenRecordService.clearAll();
            screenRecordService=null;
        }
        if (recordListeners != null && recordListeners.size() >0){
            recordListeners.clear();
        }
        if (pageRecordListeners != null && pageRecordListeners.size() >0){
            pageRecordListeners.clear();
        }
    }

    /*
    * 开始录制
    * */
    public static void startScreenRecord(Activity activity,int requestCode){
        if (isScreenRecordEnable()){
            if (screenRecordService != null && screenRecordService.isRecordRun()){
                if (!screenRecordService.isReady()){
                    AudioUtils.getUserRecordPermission(activity,requestCode);
                }
            }else {
                screenRecordService.startRecord();
            }
        }
    }

    /*
    * 获取用户录屏权限后，设置初始数据
    * */
    public static void setUpData(int resultCode, Intent resultData) throws Exception{
        if (isScreenRecordEnable()){
            if (screenRecordService != null && !screenRecordService.isRecordRun()){
                screenRecordService.setResultData(resultCode,resultData);
                screenRecordService.startRecord();
            }
        }
    }

    /*
    * 停止录制
    * */
    public static void stopRecord(){
        if (isScreenRecordEnable()){
            if (screenRecordService != null && screenRecordService.isRecordRun()){
                screenRecordService.stopRecord("停止录制");
            }
        }
    }

    /*
    * 获取录制文件
    * */
    public static String getRecordFilePath(){
        if (isScreenRecordEnable() && screenRecordService != null){
            return screenRecordService.getmRecordPath();
        }
        return null;
    }

    /*
    * 判断当前是否在录制
    * */
    public static boolean isRecording(){
        if (isScreenRecordEnable() && screenRecordService != null){
            return screenRecordService.isRecordRun();
        }
        return false;
    }

    /*
    * 录制提示语是否正在显示
    * */
    public static boolean isShowRecordTips(){
        return isShowingRecordTips;
    }

    /*设置录制状态*/
    public static void setRecordingStatus(boolean isShow){
        isShowingRecordTips=isShow;
    }

    /*系统正在录屏，录屏会冲突，需要释放*/
    public static void clearRecordElement(){
        if (isScreenRecordEnable()){
            if (screenRecordService != null){
                screenRecordService.clearRecordElement();
            }
        }
    }

    public static void addRecordListeners(RecordListener recordListener){
        if (recordListener != null && !recordListeners.contains(recordListener)){
            recordListeners.add(recordListener);
        }
    }

    public static void removeRecordListener(RecordListener recordListener){
        if (recordListener !=null && recordListeners.contains(recordListener)){
            recordListeners.remove(recordListener);
        }
    }

    public static void addPageRecordListener(OnPageRecordListener listener){
        if (listener != null && !pageRecordListeners.contains(listener)){
            pageRecordListeners.add(listener);
        }
    }

    public static void removePageRecordListener(OnPageRecordListener listener){
        if (listener != null && pageRecordListeners.contains(listener)){
            pageRecordListeners.remove(listener);
        }
    }

    public static void onPageRecordStart(){
        if (pageRecordListeners != null && pageRecordListeners.size() >0){
            for (OnPageRecordListener listener : pageRecordListeners){
                listener.onStartRecord();
            }
        }
    }

    public static void onPageRecordStop(){
        if (pageRecordListeners != null && pageRecordListeners.size()> 0){
            for (OnPageRecordListener listener : pageRecordListeners){
                listener.onStopRecord();
            }
        }
    }

    public static void onPageBeforeShowAnim(){
        if (pageRecordListeners != null && pageRecordListeners.size() >0){
            for (OnPageRecordListener listener : pageRecordListeners){
                listener.onBeforeShowAnim();
            }
        }
    }

    public static void onPageAfterHideAnim(){
        if (pageRecordListeners != null && pageRecordListeners.size() >0){
            for (OnPageRecordListener listener: pageRecordListeners){
                listener.onAfterHideAnim();
            }
        }
    }

    public static void startRecord(){
        if (recordListeners != null && recordListeners.size() >0){
            for (RecordListener listener:recordListeners){
                listener.onStartRecord();
            }
        }
    }

    public static void pauseRecord(){
        if (recordListeners != null && recordListeners.size() >0){
            for (RecordListener listener:recordListeners){
                listener.onPauseRecord();
            }
        }
    }

    public static void resumeRecord(){
        if (recordListeners != null && recordListeners.size() >0){
            for (RecordListener listener:recordListeners){
                listener.onResumeRecord();
            }
        }
    }

    public static void onRecording(String tips){
        if (recordListeners != null && recordListeners.size() >0){
            for (RecordListener listener:recordListeners){
                listener.onRecording(tips);
            }
        }
    }

    public static void stopRecord(String tips){
        if (recordListeners != null && recordListeners.size() >0){
            for (RecordListener listener:recordListeners){
                listener.onStopRecord(tips);
            }
        }
    }
    public interface RecordListener{
        void onStartRecord();
        void onPauseRecord();
        void onResumeRecord();
        void onStopRecord(String stopTips);
        void onRecording(String timeTips);
    }

    public interface OnPageRecordListener{
        void onStartRecord();
        void onStopRecord();
        void onBeforeShowAnim();
        void onAfterHideAnim();
    }

}
