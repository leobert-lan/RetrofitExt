package individual.leobert.retrofitext.sample.core;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample.core </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> DebugHandler </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/21.
 */

public final class DebugHandler<T> extends ApiResponseHandler<T> {
    private static boolean isDebugEnable;

    private static String TAG = DebugHandler.class.getSimpleName();

    public static void setDebugEnable(boolean isDebugEnable) {
        DebugHandler.isDebugEnable = isDebugEnable;
    }

    static void enableDebug(String tag) {
        isDebugEnable = true;
        DebugHandler.TAG = tag;
    }

    @Override
    public void onThrow(Throwable t) {
        super.onThrow(t);
        genLog(t.getMessage(), LogLevel.e);
        genLog(t.getClass().getSimpleName(), LogLevel.i);
        t.printStackTrace();
    }

    @Override
    public void onSuccess(int code, Call<T> call, Headers headers, T res) {
        if (!isDebugEnable)
            return;
        debug(code, call, headers, res);
    }

    @Override
    public void onFailure(int code, Call<T> call, Headers headers, ResponseBody res) {
        if (!isDebugEnable)
            return;
        debug(code, call, headers, res);
    }

    @Override
    public void onFinish(Call<T> call) {

    }


    public <R> void debug(int code, Call<T> call, Headers headers, R res) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("request:\r\n")
                .append(call.request().toString())
                .append("\r\nheaders:\r\n")
                .append(call.request().headers().toString())
                .append("==================================")
                .append("\r\nresponse:\r\n");

        genLog(stringBuilder.toString(), LogLevel.v);
        genLog("code is:" + code + "\r\n", LogLevel.i);
        genLog("headers:\r\n" + headers.toString(), LogLevel.d);
        if (res == null) {
            genLog("Response is null", LogLevel.e);
        } else if (res instanceof ResponseBody) {
            genLog("debug avoid because use string9() of ResponseBody will close it", LogLevel.i);
//            try {
//                genLog(((ResponseBody) res).string(), LogLevel.i);
//            } catch (IOException e) {
//                genLog("debug resbody io exception occurs", LogLevel.e);
//                e.printStackTrace();
//            }
        } else {
            genLog(JSON.toJSONString(res), LogLevel.i);
        }
    }


    public static void genLog(String msg, LogLevel logLevel) {
        switch (logLevel) {
            case e:
                Log.e(TAG, msg);
                break;
            case i:
                Log.i(TAG, msg);
                break;
            case d:
                Log.i(TAG, msg);
                break;
            case v:
            default:
                Log.v(TAG, msg);
                break;
        }
    }

    public enum LogLevel {
        v, d, i, e
    }
}