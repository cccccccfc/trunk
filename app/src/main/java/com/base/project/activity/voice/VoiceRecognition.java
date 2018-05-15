package com.base.project.activity.voice;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.base.project.base.BaseApplication;
import com.base.project.utils.FucUtil;
import com.base.project.utils.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ResourceUtil;

import static com.base.project.activity.voice.OperatingDevice.controlCommand;


/**
 * Created by Administrator on 2017/11/1.
 * 语音识别
 */

public class VoiceRecognition {

    private static SpeechRecognizer mAsr;
    private static String mLocalGrammar;
    private static String mContent;
    private static int ret;
    private static String mCloudGrammar;
    private static final String GRAMMAR_TYPE_ABNF = "abnf";

    /**
     * 语音识别引擎初始化
     */
    public static void engineInit() {
        // 初始化识别对象
        if (mAsr == null) {
            mAsr = SpeechRecognizer.createRecognizer(BaseApplication.getContext(), mInitListener);
            // 初始化语法、命令词
            mCloudGrammar = FucUtil.readFile(BaseApplication.getContext(), "grammar_sample.abnf", "utf-8");
            mAsr.setParameter(SpeechConstant.PARAMS, null);
            mContent = new String(mCloudGrammar);
            // 指定引擎类型
            mAsr.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置文本编码格式
            mAsr.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
            //ret = mAsr.buildGrammar(GRAMMAR_TYPE_ABNF, mContent, grammarListener);
        }

    }

    /**
     * 开始语音识别
     */
    public static void startSpeech() {
        if (!setParam()) {
            Log.i(TAG, "请先构建语法。");
            return;
        }
        ret = mAsr.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            Log.i(TAG, "识别失败,错误码: " + ret);
        }
    }

    /**
     * 识别监听器
     */

    private static RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int volume, byte[] bytes) {
            //   Log.i("-----", "返回音频数据：" + data.length);

        }

        @Override
        public void onBeginOfSpeech() {
            Log.i(TAG, "onVolumeChanged: 用户可以开始语音输入");
        }

        @Override
        public void onEndOfSpeech() {
            Log.i(TAG, "onVolumeChanged: 进入识别过程，不再接受语音输入");
        }

        @Override
        public void onResult(RecognizerResult result, boolean b) {
            if (null != result && !TextUtils.isEmpty(result.getResultString())) {
                ActivityVoiceHome.getInstance().setWidget(6);
              //  Log.i(TAG, "onResult: " + result.getResultString());
                if(b){
                   // Log.i(TAG, "onResult: 空的");
                }else{
                    String text = "";
                    text = JsonParser.parseGrammarResult(result.getResultString());
                    Log.i(TAG, "onResult: 解析后的" + text);
                    controlCommand(text);
                }
               
                
            } else {
                Log.d(TAG, "recognizer result : null");
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.i(TAG, "onVolumeChanged: 识别错误" + speechError.getErrorCode());
            if (speechError != null) {
                ActivityVoiceHome.getInstance().setWidget(6);
            }

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private static String TAG = "qaz";
    /**
     * 初始化监听器。
     */
    private static InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Log.i(TAG, "初始化失败,错误码：" + code);
            }
        }
    };

    /**
     * 构建语法监听器。
     */
    private static GrammarListener grammarListener = new GrammarListener() {
        @Override
        public void onBuildFinish(String grammarId, SpeechError error) {
            if (error == null) {
                Log.i(TAG, "语法构建成功：" + grammarId);
                mAsr.setParameter(SpeechConstant.CLOUD_GRAMMAR, grammarId);
            } else {
                Log.i(TAG, "语法构建失败,错误码：" + error.getErrorCode());
            }
        }
    };

    /**
     * 获取识别资源路径
     *
     * @return
     */
    private static String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(BaseApplication.getContext(), ResourceUtil.RESOURCE_TYPE.assets, "asr/common.jet"));
        return tempBuffer.toString();
    }

    /**
     * 设置参数
     *
     * @return
     */
    public static boolean setParam() {
        // 先清空之前的参数
        mAsr.setParameter(SpeechConstant.PARAMS, null);
        mAsr.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mAsr.setParameter(SpeechConstant.RESULT_TYPE, "json");//返回文本结果类型
        mAsr.setParameter(SpeechConstant.VAD_BOS, "10000");
        mAsr.setParameter(SpeechConstant.VAD_ENABLE, "0");
        mAsr.setParameter(SpeechConstant.ASR_PTT, "0");
        return true;
    }

    /**
     * 退出时调用 释放资源
     */
    public static void stopSpeech() {

        if (mAsr != null) {
            Log.i(TAG, "退出时调用 识别释放资源");
            mAsr.stopListening();
            mAsr.cancel();
            mAsr.destroy();

        }
    }
}
