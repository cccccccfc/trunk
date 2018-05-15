package com.base.project.activity.voice;

import android.os.Bundle;
import android.util.Log;

import com.base.project.base.BaseApplication;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * 语音合成
 */

public class VoicePlaying {


    private static SpeechSynthesizer mTts;

    public static void speekText() {
        //1. 创建 SpeechSynthesizer 对象 , 第二个参数： 本地合成时传 InitListener
        if (mTts ==null) {
            mTts = SpeechSynthesizer.createSynthesizer(BaseApplication.getContext(), mInitListener);
            // 清空参数
            mTts.setParameter(SpeechConstant.PARAMS, null);

            //设置使用云端引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME,  "50");
            //设置播放器音频流类型
            mTts.setParameter(SpeechConstant.STREAM_TYPE,  "3");
            // 设置播放合成音频打断音乐播放，默认为true
            mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        }

    }
    public static void startSpeaking(String data){
       // Log.i("qaz", "startSpeaking: 1"  +mTts);
        //Log.i("qaz", "startSpeaking: 2"  +data);
       // Log.i("qaz", "startSpeaking: 3"  +mTtsListener);
        int code =  mTts.startSpeaking(data, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            Log.i("qaz", "语音合成失败,错误码: " + code);
        }


    }
    private static SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            // 合成进度
        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度

        }

        @Override
        public void onCompleted(SpeechError speechError) {
            if (speechError == null) {
                Log.i("qaz" , "onCompleted: 播放完成");
            } else if (speechError != null) {
                Log.i("qaz" , "onCompleted: 播放错误" +speechError.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };


    /**
     * 初始化监听器。
     */
    private static InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
           // Log.d("qaz", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.i("qaz","初始化失败,错误码：" + code);
            }
        }
    };
    /**
     * 退出时调用 释放资源
     */
    public static void stopSpeak() {

            if( null != mTts ){
                Log.i("qaz", "退出时调用 合成释放资源" );
                mTts.stopSpeaking();
                // 退出时释放连接
                mTts.destroy();
            }
    }

}
