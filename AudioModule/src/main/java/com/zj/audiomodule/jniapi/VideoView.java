package com.zj.audiomodule.jniapi;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class VideoView extends SurfaceView {
    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        SurfaceHolder holder=getHolder();
        holder.setFormat(PixelFormat.RGBA_8888);
    }

    public void player(final String input){
        new Thread(new Runnable() {
            @Override
            public void run() {
                render(input,getHolder().getSurface());
            }
        }).start();
    }

    public native void render(String input, Surface surface);
    static {
        System.loadLibrary("avcodec-56");
        System.loadLibrary("avdevice-56");
        System.loadLibrary("avfilter-5");
        System.loadLibrary("avformat-56");
        System.loadLibrary("avutil-54");
        System.loadLibrary("postproc-53");
        System.loadLibrary("swresample-1");
        System.loadLibrary("swscale-3");
        System.loadLibrary("ffmpeg_deal");
    }


}
