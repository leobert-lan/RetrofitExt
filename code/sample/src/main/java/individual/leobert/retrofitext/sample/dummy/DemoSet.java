/*
 * MIT License
 *
 * Copyright (c) 2017 leobert-lan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package individual.leobert.retrofitext.sample.dummy;

import android.content.DialogInterface;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import individual.leobert.retrofitext.ext.LifeCycledCallHelper;
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
            DEMO_FILE_DOWNLOAD, DEMO_FILE_UPLOAD})
    public @interface DemoType {
    }

    public interface Demo<T> {
        LifeCycledCallHelper callHelper = LifeCycledCallHelper.getInstance();

        @DemoType
        int getDemoType();

        String getDemoName();

        Call<T> newCallInstance();

        void startWithDebug(ViewInterface viewInterface);

        @NonNull
        ProgressStyle getProgressStyle();

    }

    public static final class ProgressStyle {

        public static final ProgressStyle DEFAULT = new ProgressStyle(true, false, null);
        public static final ProgressStyle DOWNLOAD = new ProgressStyle(false, true, null);


        public CharSequence title;
        public CharSequence message;
        public boolean indeterminate;
        public boolean cancelable;
        public DialogInterface.OnCancelListener cancelListener;

        public ProgressStyle() {
        }

        public ProgressStyle(boolean indeterminate, boolean cancelable,
                             DialogInterface.OnCancelListener cancelListener) {
            this.indeterminate = indeterminate;
            this.cancelable = cancelable;
            this.cancelListener = cancelListener;
        }

        public ProgressStyle(CharSequence title, CharSequence message,
                             boolean indeterminate, boolean cancelable,
                             DialogInterface.OnCancelListener cancelListener) {
            this.title = title;
            this.message = message;
            this.indeterminate = indeterminate;
            this.cancelable = cancelable;
            this.cancelListener = cancelListener;
        }
    }

}
