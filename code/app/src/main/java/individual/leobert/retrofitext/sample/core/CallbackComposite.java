package individual.leobert.retrofitext.sample.core;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample.core </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> CallbackComposite </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/21.
 */

public final class CallbackComposite<T> extends ApiResponseHandler<T> {
    private List<ApiResponseHandler<T>> callbacks = new ArrayList<>();

    public CallbackComposite() {
        add(new DebugHandler<T>());
    }

    public void add(ApiResponseHandler<T> callback) {
        if (callback == null)
            return;
        callbacks.add(callback);
    }

    @Override
    public void onSuccess(int code, Call<T> call, Headers headers, T res) {
        for (ApiResponseHandler<T> callback : callbacks) {
            callback.onSuccess(code, call, headers, res);
        }
    }

    @Override
    public void onFailure(int code, Call<T> call, Headers headers, ResponseBody res) {
        for (ApiResponseHandler<T> callback : callbacks) {
            callback.onFailure(code, call, headers, res);
        }
    }

    @Override
    public void onFinish(Call<T> call) {
        RequestManager.getInstance().remove(call);
        for (ApiResponseHandler<T> callback : callbacks) {
            callback.onFinish(call);
        }
    }

}
