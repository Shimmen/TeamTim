package teamtim.teamtimapp.managers;

public abstract class OnResultListener implements OnResultCallback {
    static OnResultCallback listener = null;

    public static void setGlobalListener(OnResultCallback listener){
        OnResultListener.listener = listener;
    }

    public static OnResultCallback getGlobalListener() throws NullPointerException {
        if (listener == null) {
            throw new NullPointerException("Listener not initialized");
        }
        return listener;
    }

}
