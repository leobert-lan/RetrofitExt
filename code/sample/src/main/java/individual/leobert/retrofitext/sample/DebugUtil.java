package individual.leobert.retrofitext.sample;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> ParserUtil </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/19.
 */

public class DebugUtil {
    private DebugUtil() {
    }

    public static <T> String parseFailure(Call<T> call, Throwable t) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("request:\r\n")
                .append(call.request().toString())
                .append("\r\nheaders:\r\n")
                .append(call.request().headers().toString())
                .append("==================================")
                .append("\r\nthrow:\r\n")
                .append(t.getMessage());
        return stringBuilder.toString();
    }

    public static String parseSuccess1(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("request:\r\n")
                .append(call.request().toString())
                .append("\r\nheaders:\r\n")
                .append(call.request().headers().toString())
                .append("==================================")
                .append("\r\nresponse:\r\n");

        final String isSuccess;
        final ResponseBody resBody;
        if (response.isSuccessful()) {
            isSuccess = "success\r\n";
            resBody = response.body();
        } else {
            isSuccess = "failed\r\n";
            resBody = response.errorBody();
        }
        stringBuilder.append(isSuccess);
        try {
            stringBuilder.append(resBody != null ? resBody.string() : "ResponseBody is null");
        } catch (IOException e) {
            stringBuilder.append("debug resbody io exception occurs");
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }


    public static <T> String parseSuccess2(Call<T> call, Response<T> response) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("request:\r\n")
                .append(call.request().toString())
                .append("\r\nheaders:\r\n")
                .append(call.request().headers().toString())
                .append("==================================")
                .append("\r\nresponse:\r\n");

        final String isSuccess;
        if (response.isSuccessful()) {
            isSuccess = "success\r\n";
            stringBuilder.append(isSuccess);
            final T resBody = response.body();
            stringBuilder.append(JSON.toJSONString(resBody));
        } else {
            isSuccess = "failed\r\n";
            stringBuilder.append(isSuccess);
            final ResponseBody resBody = response.errorBody();
            try {
                stringBuilder.append(resBody != null ? resBody.string() :
                        "ResponseBody is null");
            } catch (IOException e) {
                stringBuilder.append("debug resbody io exception occurs");
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }

    public static <T> Spannable debug(int code, Call<T> call, Headers headers, Object res) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("request:\r\n")
                .append(call.request().toString())
                .append("\r\nheaders:\r\n")
                .append(call.request().headers().toString())
                .append("==================================")
                .append("\r\nresponse:\r\n");

        builder.append(genLog(stringBuilder.toString(), LogLevel.v))
                .append(genLog("code is:"+code+"\r\n", LogLevel.i))
                .append(genLog("headers:\r\n"+headers.toString(), LogLevel.d));
        if (res == null){
            builder.append(genLog("Response is null", LogLevel.e));
        }else if (res instanceof ResponseBody) {
            try {
                String s = ((ResponseBody)res).string() ;

                builder.append(genLog("use responseBody\r\n"+ s, LogLevel.i));
            } catch (IOException e) {
                builder.append(genLog("debug resbody io exception occurs", LogLevel.e));
                e.printStackTrace();
            }
        } else {
            builder.append(genLog("use entity\r\n"+JSON.toJSONString(res), LogLevel.i));
        }
        return builder;
    }


    public static Spannable genLog(String s,LogLevel logLevel) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(s,new ForegroundColorSpan(logLevel.getDisplayColor()),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public enum LogLevel {
        v(Color.GRAY),d(Color.BLUE),i(Color.GREEN),e(Color.RED);
        private final int displayColor;

        LogLevel(int displayColor) {
            this.displayColor = displayColor;
        }

        public int getDisplayColor() {
            return displayColor;
        }
    }
}
