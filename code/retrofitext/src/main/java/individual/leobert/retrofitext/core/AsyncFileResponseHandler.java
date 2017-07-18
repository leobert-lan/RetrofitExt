package individual.leobert.retrofitext.core;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * <p><b>Package:</b> individual.leobert.retrofitext.core.</p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> AsyncFileResponseHandler </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/19.
 */

public abstract class AsyncFileResponseHandler
        extends ApiResponseHandler<ResponseBody> {
    private static final String LOG_TAG = "lmsg";
    protected final File file;
    protected final boolean renameIfExists;
    protected File frontendFile;

    protected Call<ResponseBody> call;

    private interface OnProgressChangedListener {
        void onProgressChanged(long current, long total);
    }

    private final OnProgressChangedListener onProgressChangedListener =
            new OnProgressChangedListener() {
                @Override
                public void onProgressChanged(long current, long total) {
                    if (current < total)
                        AsyncFileResponseHandler.this.onProgress(call, current, total);
                    else
                        AsyncFileResponseHandler.this.onResponse(call, frontendFile);
                }
            };

    private MHandler mHandler = new MHandler(onProgressChangedListener);

    private final static class MHandler extends Handler {
        public static final int WHAT_PROGRESS_CHANGE = 1;
        public static final String KEY_CURRENT = "k_c";
        public static final String KEY_TOTAL = "k_t";

        private final WeakReference<OnProgressChangedListener> listenerRef;

        public MHandler(OnProgressChangedListener onProgressChangedListener) {
            super(Looper.getMainLooper());
            listenerRef = new WeakReference<>(onProgressChangedListener);
        }

        void send(long current, long total) {
            Message m = new Message();
            m.what = WHAT_PROGRESS_CHANGE;
            Bundle bundle = new Bundle();
            bundle.putLong(KEY_CURRENT, current);
            bundle.putLong(KEY_TOTAL, total);
            m.setData(bundle);
            sendMessage(m);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT_PROGRESS_CHANGE) {
                Bundle b = msg.getData();
                onProgressChanged(b.getLong(KEY_CURRENT), b.getLong(KEY_TOTAL));
            }
        }

        private void onProgressChanged(long current, long total) {
            if (listenerRef.get() != null) {
                listenerRef.get().onProgressChanged(current, total);
            }
        }

        public void release() {
            if (listenerRef != null)
                listenerRef.clear();
        }
    }

    /**
     * Obtains new FileAsyncHttpResponseHandler and stores response in passed file
     *
     * @param file File to store response within, must not be null
     */
    public AsyncFileResponseHandler(File file) {
        this(file, false);
    }


    /**
     * Obtains new FileAsyncHttpResponseHandler and stores response in passed file
     *
     * @param file                     File to store response within, must not be null
     * @param renameTargetFileIfExists whether target file should be renamed if it already exists
     */
    public AsyncFileResponseHandler(File file, /*boolean append,*/
                                    boolean renameTargetFileIfExists) {
        Utils.asserts(file != null, "File passed into FileAsyncHttpResponseHandler constructor must not be null");
        if (!file.isDirectory() && !file.getParentFile().isDirectory()) {
            Utils.asserts(file.getParentFile().mkdirs(), "Cannot create parent directories for requested File location");
        }
        if (file.isDirectory()) {
            if (!file.mkdirs()) {
                Log.d(LOG_TAG, "Cannot create directories for requested Directory location, might not be a problem");
            }
        }

        if (file.isFile()&&file.exists()&&!renameTargetFileIfExists) {
            file.delete();
            file.getParentFile().mkdirs();
        }

        this.file = file;
        this.renameIfExists = renameTargetFileIfExists;
    }

    /**
     * Obtains new FileAsyncHttpResponseHandler against context with target being temporary file
     *
     * @param context Context, must not be null
     */
    public AsyncFileResponseHandler(Context context) {
        this.file = getTemporaryFile(context);
        this.renameIfExists = false;
    }

    private String getUrl(Call call) {
        return call.request().url().url().toString();
    }

//    @Override
//    public final void onResponse(Call<ResponseBody> call,
//                                 Response<ResponseBody> response) {
//        this.call = call;
//        if (response.isSuccessful()) {
//            write(response);
//            Log.d(LOG_TAG, "write started");
//        } else {
//            onFailure(response.code(), call);
//        }
//    }


    @Override
    public void onCancel(Call<ResponseBody> call) {
        this.call = call;
        mHandler.release();
    }

    @Override
    public void onSuccess(int code, Call<ResponseBody> call, Headers headers,
                          ResponseBody res) {
        this.call = call;
        write(res);
        Log.d(LOG_TAG, "write started");

    }

    @Override
    public void onFailure(int code, Call<ResponseBody> call, Headers headers, ResponseBody res) {
        this.call = call;
        onFailure(code, call);
    }

    @Override
    public void onFinish(Call<ResponseBody> call) {

    }

    public abstract void onProgress(Call<ResponseBody> call, long current, long total);

    public abstract void onResponse(Call<ResponseBody> call, File file);

    public abstract void onFailure(int code, Call<ResponseBody> call);

    private void write(final ResponseBody responseBody) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = responseBody.byteStream();
                    long contentLength = responseBody.contentLength();
                    File file = getTargetFile(getUrl(call));
                    Log.d(LOG_TAG, "write to:" + file.getAbsolutePath());
                    FileOutputStream fos =
                            new FileOutputStream(file, false);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    int count = 0;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                        count += len;
                        sendProgressMessage(count, contentLength);
                    }
                    fos.close();
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    onFailure(call, e);
                    e.printStackTrace();
                }
                Log.d(LOG_TAG, "success");
            }
        }).start();
    }

    private void sendProgressMessage(int current, long contentLength) {
        Log.i(LOG_TAG, "current: " + current + "byte in " + contentLength);
        mHandler.send(current, contentLength);
    }

    /**
     * Attempts to delete file with stored response
     *
     * @return false if the file does not exist or is null, true if it was successfully deleted
     */
    public boolean deleteTargetFile() {
        if (call == null) {
            Log.d(LOG_TAG, "no call to download,no file created,so no need to delete ");
            return false;
        }
        String url = call.request().url().url().toString();
        return getTargetFile(url) != null && getTargetFile(url).delete();
    }

    /**
     * Used when there is no file to be used when calling constructor
     *
     * @param context Context, must not be null
     * @return temporary file or null if creating file failed
     */
    protected File getTemporaryFile(Context context) {
        Utils.asserts(context != null, "Tried creating temporary file without having Context");
        try {
            return File.createTempFile("temp_", "_handled", context.getCacheDir());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Cannot create temporary file", e);
        }
        return null;
    }

    /**
     * Retrieves File object in which the response is stored
     *
     * @return File file in which the response was to be stored
     */
    protected File getOriginalFile() {
        Utils.asserts(file != null, "Target file is null, fatal!");
        return file;
    }

    /**
     * Retrieves File which represents response final location after possible renaming
     *
     * @return File final target file
     */
    public File getTargetFile(String url) {
        if (frontendFile == null) {
            frontendFile = getOriginalFile().isDirectory() ? getTargetFileByParsingURL(url)
                    : getOriginalFile();
        }
        return frontendFile;
    }

    /**
     * Will return File instance for file representing last URL segment in given folder.
     * If file already exists and renameTargetFileIfExists was set as true, will try to find file
     * which doesn't exist, naming template for such cases is "filename.ext" =&gt; "filename (%d).ext",
     * or without extension "filename" =&gt; "filename (%d)"
     *
     * @param url
     * @return File in given directory constructed by last segment of request URL
     */
    protected File getTargetFileByParsingURL(String url) {
        Utils.asserts(getOriginalFile().isDirectory(), "Target file is not a directory, cannot proceed");
        Utils.asserts(url != null, "RequestURI is null, cannot proceed");
        String filename = url.substring(url.lastIndexOf('/') + 1, url.length());
        File targetFileRtn = new File(getOriginalFile(), filename);
        if (targetFileRtn.exists() && renameIfExists) {
            String format;
            if (!filename.contains(".")) {
                format = filename + " (%d)";
            } else {
                format = filename.substring(0, filename.lastIndexOf('.')) + " (%d)" + filename.substring(filename.lastIndexOf('.'), filename.length());
            }
            int index = 0;
            while (true) {
                targetFileRtn = new File(getOriginalFile(), String.format(format, index));
                if (!targetFileRtn.exists())
                    return targetFileRtn;
                index++;
            }
        }
        return targetFileRtn;
    }

}
