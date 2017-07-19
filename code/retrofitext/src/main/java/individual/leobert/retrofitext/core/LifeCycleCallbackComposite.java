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

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.core.</p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> CallbackComposite </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/21.
 */

/*public*/ final class LifeCycleCallbackComposite<T> extends ApiResponseHandler<T> {
    private List<ApiResponseHandler<T>> callbacks = new ArrayList<>();

    public LifeCycleCallbackComposite() {
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
    public void onCancel(Call<T> call) {
        for (ApiResponseHandler<T> callback : callbacks) {
            callback.onCancel(call);
        }
    }

    @Override
    public void onThrow(Throwable t) {
        for (ApiResponseHandler<T> callback : callbacks) {
            callback.onThrow(t);
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
