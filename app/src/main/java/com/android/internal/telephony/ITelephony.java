package com.android.internal.telephony;

/**
 * Created by Tian on 2015/6/3.
 */
public interface ITelephony {
    boolean endCall();

    void answerRingingCall();

    void silenceRinger();
}
