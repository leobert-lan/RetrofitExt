package individual.leobert.retrofitext.core;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.core </p>
 * <p><b>Project:</b> code </p>
 * <p><b>Classname:</b> Debug </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/7/17.
 */

public class Debug {
    public static void setDebugEnable(boolean isDebugEnable) {
        DebugHandler.setDebugEnable(isDebugEnable);
    }

    public static void enableDebug(String tag) {
        DebugHandler.enableDebug(tag);
    }

}
