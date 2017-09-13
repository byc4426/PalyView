package surui.huizhou.com.face;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;


/**
 * Created by surui29 on 2017/8/29.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "BiWcgKiMUe06mAFuq2EE7Cgb-gzGzoHsz", "NKkqEtKNLGvcgYABueb5K89x");
    }
}
