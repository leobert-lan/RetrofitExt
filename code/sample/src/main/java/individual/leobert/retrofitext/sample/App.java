package individual.leobert.retrofitext.sample;

import android.app.Application;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> App </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/16.
 */

public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ApiClient.setLogEnable(true);
    }

    public static App getInstance() {
        return instance;
    }
}
