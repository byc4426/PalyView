package surui.huizhou.com.palycontrol;

/**
 * Created by surui29 on 2017/8/29.
 */

public interface OnPlayListener {

    void pauseListener(boolean pause);//暂停false/播放true

    void muteListener(boolean mute);//静音false/不静音true

    void volumeListener(double volume);//音量

    void progressListener(double total, double progress, double percent);//播放进度(总时长,进度，百分比)

    void speedListener();//快进

    void backListener();//后退
}
