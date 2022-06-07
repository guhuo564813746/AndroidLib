package com.lib.audio.activitis;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.lib.audio.dialog.AudioRecordAndSendMsgDialog;
import com.lib.code.R;
import com.zj.audiomodule.utils.AudioRecordHelper;

import java.io.File;

/**
 * 作者：create by 张金 on 2022/6/1 10:20
 * 邮箱：564813746@qq.com
 */
public class AudioRecordTestActivity extends AppCompatActivity {
    private static final String TAG="AudioRecordTestActivity";
    private float curX=0f;
    private float curY=0f;
    private TextView tvSendAudioMsg;
    private ViewGroup audioRecordRoot;
    private View viewAudioControl;
    private TextView tvSendTips;
    private View viewAudioCancel;
    private TextView tvCancelTips;
    private ImageView imVoicePlay;
    private int screenHeight;
    private int AUDIO_REQUEST=100;
    private String audioPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate--");
        setContentView(R.layout.audio_record_test_activity_lay);
        audioPath=getExternalFilesDir("audioMsg").getAbsolutePath()+ File.separator+"audioMsg";
        tvSendAudioMsg=findViewById(R.id.tv_send_audio_msg);
        audioRecordRoot=findViewById(R.id.audio_record_root);
        screenHeight= ScreenUtils.getScreenHeight();
        viewAudioControl=findViewById(R.id.view_audio_control);
        tvSendTips=findViewById(R.id.tv_send_tips);
        viewAudioCancel=findViewById(R.id.view_audio_cancel);
        tvCancelTips=findViewById(R.id.tv_cancel_tips);
        imVoicePlay=findViewById(R.id.im_voice_play);
        imVoicePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private boolean hasPermission(String permission){
        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,permission)){
            ActivityCompat.requestPermissions(this,new String[]{permission},AUDIO_REQUEST);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AUDIO_REQUEST){

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN :{
                if (!hasPermission(Manifest.permission.RECORD_AUDIO)){
                    break;
                }
                curY=event.getY();
                Log.d(TAG,"ACTION_DOWN--curY"+curY+"--screenHeight--"+screenHeight+"--dpi-"+ ScreenUtils.getScreenDensity()*50);
                if (curY >= (screenHeight- ScreenUtils.getScreenDensity()*50)){
                    tvSendAudioMsg.setVisibility(View.GONE);
                    audioRecordRoot.setVisibility(View.VISIBLE);
                    AudioRecordHelper.startRecordAudioByMediaRecord(audioPath);
                }
            }
            break;
            case MotionEvent.ACTION_MOVE :{
                curX=event.getX();
                curY=event.getY();
                Log.d(TAG,"curX--${curX}--curY--${curY}--screenHeight--${screenHeight}");
                if (isInViewArea(viewAudioControl,curX,curY)){
                    viewAudioControl.setBackgroundColor(getResources().getColor(R.color.green));
                    tvSendTips.setVisibility(View.VISIBLE);
                    viewAudioCancel.setBackgroundResource(R.mipmap.im_cancel_normal);
                    tvCancelTips.setVisibility(View.GONE);
                }else {
                    viewAudioControl.setBackgroundColor(getResources().getColor(R.color.gray));
                    tvSendTips.setVisibility(View.GONE);
                    tvCancelTips.setVisibility(View.VISIBLE);
                    viewAudioCancel.setBackgroundResource(R.mipmap.im_cancel_selected);
                }
            }
            break;
            case MotionEvent.ACTION_UP :{
                curX=event.getX();
                curY=event.getY();
                tvSendAudioMsg.setVisibility(View.VISIBLE);
                audioRecordRoot.setVisibility(View.GONE);
                if (isInViewArea(viewAudioControl,curX,curY)){
                    Log.d(TAG,"发送语音消息");
                }else {
                    Log.d(TAG,"取消发送语音消息");
                    AudioRecordHelper.releaseRecordAudioByMediaRecord(audioPath);
                }
            }
            break;
        }
        return true;
    }

    private boolean isInViewArea(View view, float x, float y) {
        Log.e(TAG, "x " + x + "y " + y);
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        RectF rectF=new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
        return rectF.contains(x,y);
    }

    private void showAudioRecordAndSendMsgDialog(){
        AudioRecordAndSendMsgDialog audioRecordAndSendMsgDialog=new AudioRecordAndSendMsgDialog();
        if (!audioRecordAndSendMsgDialog.isAdded()){
            audioRecordAndSendMsgDialog.show(getSupportFragmentManager(),"AudioRecordAndSendMsgDialog");
        }
    }
}
