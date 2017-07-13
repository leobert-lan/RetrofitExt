package individual.leobert.retrofitext.sample;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import individual.leobert.retrofitext.sample.core.ApiDefCheckUtil;
import individual.leobert.retrofitext.sample.core.DebugHandler;
import individual.leobert.retrofitext.sample.core.RequestManager;
import individual.leobert.retrofitext.sample.coverter.FastJsonConverterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;


/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> HttpUtil </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/16.
 */

public class ApiClient {
    private static boolean isLogEnable = false;
    private static final String TAG  = "ApiClient";

    public static void setLogEnable(boolean isLogEnable) {
        ApiClient.isLogEnable = isLogEnable;
        DebugHandler.setDebugEnable(isLogEnable);
    }

    private Retrofit retrofit = null;

    private static ApiClient apiClient;
    private static final String KEY_USER_AGENT = "User-Agent";

    // TODO: custom ua
    private static final String CUSTOM_USER_AGENT = "MY CUSTOM UA";

    private ApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://httpbin.org/")
                .client(newCustomUaClient())
//                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();
    }

    public static ApiClient getInstance() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        return apiClient;
    }

    public <T> T apiInstance(Class<T> remoteApiClazz) {
        if (!ApiDefCheckUtil.isAllowedApi(remoteApiClazz))
            throw new IllegalArgumentException("must give a interface with apiDef annotation");
        T api = retrofit.create(remoteApiClazz);
        return api;
    }

    private OkHttpClient newCustomUaClient() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader(KEY_USER_AGENT,CUSTOM_USER_AGENT)
                                .build();
                        return chain.proceed(request);
                    }
                }).build();
    }

    public static void shutdown() {
        if (apiClient == null)
            return;
        apiClient.retrofit = null;
        RequestManager.shutdown();
    }

    public static void cancel(Activity activity) {
        RequestManager.getInstance().cancel(activity);
    }

    public static void cancel(Fragment fragment) {
        RequestManager.getInstance().cancel(fragment);
    }

    public static void cancel(android.support.v4.app.Fragment fragment) {
        RequestManager.getInstance().cancel(fragment);
    }

}