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

package individual.leobert.retrofitext.ext;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.ext</p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> DebugHandler </p>
 * <p><b>Description:</b> used to print response </p>
 * Created by leobert on 2017/6/21.
 */

public final class DebugHandler<T> extends ApiResponseHandler<T> {
    private static boolean isDebugEnable;

    private static String TAG = DebugHandler.class.getSimpleName();

    static void setDebugEnable(boolean isDebugEnable) {
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
        //ignore
    }


    private <R> void debug(int code, Call<T> call, Headers headers, R res) {
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
            genLog("debug skipped because use string() of ResponseBody will close it",
                    LogLevel.i);
        } else {
            genLog(JSON.toJSONString(res), LogLevel.i);
        }
    }


    private static void genLog(String msg, LogLevel logLevel) {
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

    private enum LogLevel {
        v, d, i, e
    }
}
