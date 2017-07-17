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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * <p><b>Package:</b> individual.leobert.retrofitext.core.</p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> AsyncFileResponseHandler </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/19.
 */

public abstract class AsyncFileResponseHandler implements Callback<ResponseBody> {
    private static final String LOG_TAG = "lmsg";
    protected final File file;
    protected final boolean append;
    protected final boolean renameIfExists;
    protected File frontendFile;

    private Call<ResponseBody> call;

    private MHandler mHandler = new MHandler() {
        @Override
        protected void onProgressChanged(long current, long total) {
            if (current < total)
                AsyncFileResponseHandler.this.onProgress(call, current, total);
            else
                AsyncFileResponseHandler.this.onResponse(call, frontendFile);
        }
    };

    private abstract static class MHandler extends Handler {
        public static final int WHAT_PROGRESS_CHANGE = 1;
        public static final String KEY_CURRENT = "k_c";
        public static final String KEY_TOTAL = "k_t";

        public MHandler() {
            super(Looper.getMainLooper());
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

        protected abstract void onProgressChanged(long current, long total);
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
     * @param file   File to store response within, must not be null
     * @param append whether data should be appended to existing file
     */
    public AsyncFileResponseHandler(File file, boolean append) {
        this(file, append, false);
    }

    /**
     * Obtains new FileAsyncHttpResponseHandler and stores response in passed file
     *
     * @param file                     File to store response within, must not be null
     * @param append                   whether data should be appended to existing file
     * @param renameTargetFileIfExists whether target file should be renamed if it already exists
     */
    public AsyncFileResponseHandler(File file, boolean append, boolean renameTargetFileIfExists) {
        this(file, append, renameTargetFileIfExists, false);
    }


    /**
     * Obtains new FileAsyncHttpResponseHandler and stores response in passed file
     *
     * @param file                     File to store response within, must not be null
     * @param append                   whether data should be appended to existing file
     * @param renameTargetFileIfExists whether target file should be renamed if it already exists
     * @param usePoolThread            Whether to use the pool's thread to fire callbacks
     */
    public AsyncFileResponseHandler(File file, boolean append, boolean renameTargetFileIfExists, boolean usePoolThread) {
        Utils.asserts(file != null, "File passed into FileAsyncHttpResponseHandler constructor must not be null");
        if (!file.isDirectory() && !file.getParentFile().isDirectory()) {
            Utils.asserts(file.getParentFile().mkdirs(), "Cannot create parent directories for requested File location");
        }
        if (file.isDirectory()) {
            if (!file.mkdirs()) {
                Log.d(LOG_TAG, "Cannot create directories for requested Directory location, might not be a problem");
            }
        }
        this.file = file;
        this.append = append;
        this.renameIfExists = renameTargetFileIfExists;
    }

    /**
     * Obtains new FileAsyncHttpResponseHandler against context with target being temporary file
     *
     * @param context Context, must not be null
     */
    public AsyncFileResponseHandler(Context context) {
        this.file = getTemporaryFile(context);
        this.append = false;
        this.renameIfExists = false;
    }

    private String getUrl(Call call) {
        return call.request().url().url().toString();
    }

    @Override
    public final void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        this.call = call;
        if (response.isSuccessful()) {
            write(response);
            Log.d(LOG_TAG, "write started");
        } else {
            onFailure(response.code(), call);
        }
    }

    public abstract void onProgress(Call<ResponseBody> call, long current, long total);

    public abstract void onResponse(Call<ResponseBody> call, File file);

    public abstract void onFailure(int code, Call<ResponseBody> call);

    private void write(final Response<ResponseBody> response) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = response.body().byteStream();
                    long contentLength = response.body().contentLength();
                    File file = getTargetFile(getUrl(call));
                    Log.d(LOG_TAG, "write to:" + file.getAbsolutePath());
                    FileOutputStream fos =
                            new FileOutputStream(file,
                                    AsyncFileResponseHandler.this.append);
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

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        this.call = call;
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
            frontendFile = getOriginalFile().isDirectory() ? getTargetFileByParsingURL(url) : getOriginalFile();
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

//    @Override
//    public final void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable) {
//        onFailure(statusCode, headers, throwable, getTargetFile());
//    }

//    /**
//     * Method to be overriden, receives as much of file as possible Called when the file is
//     * considered failure or if there is error when retrieving file
//     *
//     * @param statusCode http file status line
//     * @param headers    file http headers if any
//     * @param throwable  returned throwable
//     * @param file       file in which the file is stored
//     */
//    public abstract void onFailure(int statusCode, Header[] headers, Throwable throwable, File file);

//    @Override
//    public final void onSuccess(int statusCode, Header[] headers, byte[] responseBytes) {
//        onSuccess(statusCode, headers, getTargetFile());
//    }

//    /**
//     * Method to be overriden, receives as much of response as possible
//     *
//     * @param statusCode http response status line
//     * @param headers    response http headers if any
//     * @param file       file in which the response is stored
//     */
//    public abstract void onSuccess(int statusCode, Header[] headers, File file);

//    protected byte[] getResponseData(HttpEntity entity) throws IOException {
//        if (entity != null) {
//            InputStream instream = entity.getContent();
//            long contentLength = entity.getContentLength();
//            FileOutputStream buffer = new FileOutputStream(getTargetFile(), this.append);
//            if (instream != null) {
//                try {
//                    byte[] tmp = new byte[BUFFER_SIZE];
//                    int l, count = 0;
//                    // do not send messages if request has been cancelled
//                    while ((l = instream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
//                        count += l;
//                        buffer.write(tmp, 0, l);
//                        sendProgressMessage(count, contentLength);
//                    }
//                } finally {
//                    instream.close();
//                    buffer.flush();
//                    buffer.close();
//                }
//            }
//        }
//        return null;
//    }

}
