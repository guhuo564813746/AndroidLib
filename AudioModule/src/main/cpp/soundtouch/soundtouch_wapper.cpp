/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

#include "SoundTouch.h"
#define BUFFER_SIZE 4096

soundtouch::SoundTouch mSoundTouch;


/**
 * 采样率
 */
JNIEXPORT void JNICALL Java_com_zj_audiomodule_jniapi_JNISoundTouch_setSampleRate
        (JNIEnv *env, jobject obj, jint sampleRate) {
    mSoundTouch.setSampleRate(sampleRate);
}

/**
 * 通道数
 */
JNIEXPORT void JNICALL Java_com_zj_audiomodule_jniapi_JNISoundTouch_setChannels
        (JNIEnv *env, jobject obj, jint channel) {
    mSoundTouch.setChannels(channel);
}

/**
 * 拍子
 */
JNIEXPORT void JNICALL Java_com_zj_audiomodule_jniapi_JNISoundTouch_setTempo
        (JNIEnv *env, jobject obj, jfloat newTempo) {
    mSoundTouch.setTempo(newTempo);
}

JNIEXPORT void JNICALL Java_com_zj_audiomodule_jniapi_JNISoundTouch_setTempoChange
        (JNIEnv *env, jobject obj, jfloat newTempo) {
    mSoundTouch.setTempoChange(newTempo);
}

/**
 * 音调
 */
JNIEXPORT void JNICALL Java_com_zj_audiomodule_jniapi_JNISoundTouch_setPitchSemiTones
        (JNIEnv *env, jobject obj, jint pitch) {
    mSoundTouch.setPitchSemiTones(pitch);
}

/**
 * 速度
 */
JNIEXPORT void JNICALL Java_com_zj_audiomodule_jniapi_JNISoundTouch_setRate
        (JNIEnv *env, jobject obj, jfloat newRate) {
    mSoundTouch.setRate(newRate);
}

JNIEXPORT void JNICALL Java_com_zj_audiomodule_jniapi_JNISoundTouch_setRateChange
        (JNIEnv *env, jobject obj, jfloat newRate) {
    mSoundTouch.setRateChange(newRate);
}


JNIEXPORT void JNICALL Java_com_zj_audiomodule_jniapi_JNISoundTouch_putSamples
        (JNIEnv *env, jobject obj, jshortArray samples, jint len) {
// 转换为本地数组
    jshort *input_samples = env->GetShortArrayElements(samples, NULL);
    mSoundTouch.putSamples(input_samples, len);
    // 释放本地数组(避免内存泄露)
    env->ReleaseShortArrayElements(samples, input_samples, 0);
}


JNIEXPORT jshortArray JNICALL Java_com_zj_audiomodule_jniapi_JNISoundTouch_receiveSamples
        (JNIEnv *env, jobject obj) {
    short buffer[BUFFER_SIZE];
    int nSamples = mSoundTouch.receiveSamples(buffer, BUFFER_SIZE);

    // 局部引用，创建一个short数组
    jshortArray receiveSamples = env->NewShortArray(nSamples);
    // 给short数组设置值
    env->SetShortArrayRegion(receiveSamples, 0, nSamples, buffer);

    return receiveSamples;
}

#ifdef __cplusplus
}
#endif


