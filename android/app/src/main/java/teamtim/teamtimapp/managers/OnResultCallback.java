package teamtim.teamtimapp.managers;

import android.support.annotation.Nullable;

import java.io.Serializable;

public interface OnResultCallback extends Serializable{
    void onResult(ResultKey key, int value);
}
