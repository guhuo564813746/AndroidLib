package com.lib.audio.service;

import android.media.MediaSession2;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.service.media.MediaBrowserService;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.lib.audio.callback.AudioControlCallback;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AudioBrowserService extends MediaBrowserService {
    private static final String TAG="AudioBrowserService";
    private static final String MY_MEDIA_ROOT_ID = "media_root_id";
    private static final String MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id";
    private MediaSession mediaSession;
    private MediaSession2 mediaSession2;
    private PlaybackState.Builder stateBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        //
        mediaSession=new MediaSession(this,TAG);
        mediaSession.setFlags(
                MediaSession.FLAG_HANDLES_MEDIA_BUTTONS|MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS
        );
        stateBuilder=new PlaybackState.Builder().setActions(PlaybackState.ACTION_PLAY|PlaybackState.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        //处理媒体控制器的回调
        mediaSession.setCallback(new AudioControlCallback());
        setSessionToken(mediaSession.getSessionToken());

    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        //控制客户端对服务的访问链接
        if (allowBrowsing(clientPackageName,clientUid)){
            return new BrowserRoot(MY_MEDIA_ROOT_ID,null);
        }else {
            return new BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID,null);
        }
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowser.MediaItem>> result) {
        //处理客户端浏览媒体逻辑
        if (TextUtils.equals(MY_EMPTY_MEDIA_ROOT_ID,parentId)){
            result.sendResult(null);
            return;
        }
        //加入音乐目录已经加载
        List<MediaBrowser.MediaItem> mediaItems=new ArrayList<>();
        //判断是否在主目录
        if (MY_MEDIA_ROOT_ID.equals(parentId)){
            //添加MediaItem 对象在列表中
        }else {

        }
        result.sendResult(mediaItems);
    }

    private boolean allowBrowsing(String clientPageName,int clientUid){
        return true;
    }
}
