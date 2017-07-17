package individual.leobert.retrofitext.core;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.core.</p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> ApiResponseHandler </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/20.
 */

public abstract class ApiResponseHandler<T> implements Callback<T> {
    @Override
    public final void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.code(),call,response.headers(),response.body());
        } else {
            onFailure(response.code(),call,response.headers(),response.errorBody());
        }
        onFinish(call);
    }

    /**
     * to handle response in 200+
     * @param code 200+ serial codes
     * @param call the original call
     * @param headers response headers
     * @param res response body
     */
    public abstract void onSuccess(int code, Call<T> call, Headers headers, T res);

    /**
     * to handle response in 400+&500+
     * @param code 200+ serial codes
     * @param call the original call
     * @param headers response headers
     * @param res response body
     */
    public abstract void onFailure(int code, Call<T> call, Headers headers, ResponseBody res);

    public void onCancel(Call<T> call) {
    }

    public void onThrow(Throwable t) {
    }

    /**
     * Fired in all cases when the request is finished, override to
     * handle in your own code,best for remove the waiting signal
     * better set call to null to prepare gc in RequestManager
     */
    public abstract void onFinish(Call<T> call);

    @Override
    public final void onFailure(Call<T> call, Throwable t) {
        if (call.isCanceled()) {
            onCancel(call);
        } else {
            onThrow(t);
        }
        onFinish(call);
    }


//    protected final boolean isConnected(Context context) {
//        if (context != null) {
//            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
//            if (mNetworkInfo != null) {
//                return mNetworkInfo.isAvailable();
//            }
//        }
//        return false;
//    }
}
