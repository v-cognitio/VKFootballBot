package com.v_cognitio.VKFootballBot.VKApiSDK.com.petersamokhin.bots.sdk.callbacks;

/**
 * Created by PeterSamokhin on 29/09/2017 00:54
 */
public interface CallbackDouble<T, R> extends AbstractCallback {

    void onEvent(T t, R r);
}
