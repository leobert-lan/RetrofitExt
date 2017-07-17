package individual.leobert.retrofitext.sample.dummy;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import individual.leobert.retrofitext.sample.dummy.demos.CancelDemo;
import individual.leobert.retrofitext.sample.dummy.demos.DeleteDemo;
import individual.leobert.retrofitext.sample.dummy.demos.FileDownloadDemo;
import individual.leobert.retrofitext.sample.dummy.demos.FileUploadDemo;
import individual.leobert.retrofitext.sample.dummy.demos.GetDemo;
import individual.leobert.retrofitext.sample.dummy.demos.ParseEntityDemo;
import individual.leobert.retrofitext.sample.dummy.demos.PostDemo;
import individual.leobert.retrofitext.sample.dummy.demos.PutDemo;
import retrofit2.Call;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DemoSet {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Demo> DEMOS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Demo> ITEM_MAP = new HashMap<>();

    static {
        addDemo(new CancelDemo());
        addDemo(new GetDemo());
        addDemo(new PostDemo());
        addDemo(new PutDemo());
        addDemo(new DeleteDemo());
        addDemo(new ParseEntityDemo());
        addDemo(new FileDownloadDemo());
        addDemo(new FileUploadDemo());
    }

    private static void addDemo(Demo demo) {
        DEMOS.add(demo);
        ITEM_MAP.put(String.valueOf(demo.getDemoType()), demo);
    }

    public static final int DEMO_CANCEL = 0;
    public static final int DEMO_GET = 1;
    public static final int DEMO_POST = 2;
    public static final int DEMO_PUT = 3;
    public static final int DEMO_DELETE = 4;

    public static final int DEMO_PARSEENTITY_GET = 5;

    public static final int DEMO_FILE_DOWNLOAD = 6;
    public static final int DEMO_FILE_UPLOAD = 7;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            DEMO_CANCEL,
            DEMO_GET, DEMO_POST, DEMO_PUT, DEMO_DELETE,
            DEMO_PARSEENTITY_GET,
            DEMO_FILE_DOWNLOAD,DEMO_FILE_UPLOAD})
    public @interface DemoType {
    }

    public interface Demo<T> {
        @DemoType
        int getDemoType();

        String getDemoName();

        Call<T> newCallInstance();

        void startWithDebug(IDebugDisplay iDebugDisplay);


    }

}
