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

package individual.leobert.retrofitext.core;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.core.</p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> Utils </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/19.
 */

/*public*/ class Utils {
    private static final String LOG_TAG = "Utils";

    private Utils() {
    }

    /**
     * Will throw AssertionError, if expression is not true
     *
     * @param expression    result of your asserted condition
     * @param failedMessage message to be included in error log
     * @throws AssertionError
     */
    static void asserts(final boolean expression, final String failedMessage) {
        if (!expression) {
            throw new AssertionError(failedMessage);
        }
    }

    /**
     * Will throw IllegalArgumentException if provided object is null on runtime
     *
     * @param argument object that should be asserted as not null
     * @param name     name of the object asserted
     * @throws IllegalArgumentException
     */
//    static <T> T notNull(final T argument, final String name) {
//        if (argument == null) {
//            throw new IllegalArgumentException(name + " should not be null!");
//        }
//        return argument;
//    }
//
//    public static String convertStreamToString(InputStream is) throws Exception {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line).append("\n");
//        }
//        return sb.toString();
//    }

//    public static String getStringFromFile(File file) throws Exception {
//        FileInputStream fin = new FileInputStream(file);
//        String ret = convertStreamToString(fin);
//        //Make sure you close all streams.
//        fin.close();
//        return ret;
//    }
//
//    public static File createTempFile(File dir,String namePart, int byteSize) {
//        try {
//            File f = File.createTempFile(namePart, "_handled",dir /*App.getInstance().getCacheDir()*/);
//            FileOutputStream fos = new FileOutputStream(f);
//            Random r = new Random();
//            byte[] buffer = new byte[byteSize];
//            r.nextBytes(buffer);
//            fos.write(buffer);
//            fos.flush();
//            fos.close();
//            return f;
//        } catch (Throwable t) {
//            Log.e(LOG_TAG, "createTempFile failed", t);
//        }
//        return null;
//    }
    

}
