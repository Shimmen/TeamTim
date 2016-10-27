package teamtim.teamtimapp.managers;

import java.io.Serializable;

import teamtim.teamtimapp.activities.PlayActivity;

public abstract class QuestionResultListener {

    static QuestionResultListener globalListener = null;

    public static void setGlobalListener(QuestionResultListener listener) {
        QuestionResultListener.globalListener = listener;
    }

    public static QuestionResultListener getGlobalListener() throws NullPointerException {
        if (globalListener == null) {
            throw new NullPointerException("Listener not initialized");
        }

        return globalListener;
    }

    public abstract void onPlayActivityCreated(PlayActivity currentPlayActivity);
    public abstract void onQuestionResult(int result, int time, String answer);
    public abstract void onPause();
    public abstract void onResume();

}
