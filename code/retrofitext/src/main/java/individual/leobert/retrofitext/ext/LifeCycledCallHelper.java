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

import android.content.Context;

import retrofit2.Call;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.ext</p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> ApiModel </p>
 * <p><b>Description:</b> helper for making life-cycled retrofit call </p>
 * Created by leobert on 2017/6/21.
 */

public final class LifeCycledCallHelper {

    private static LifeCycledCallHelper INSTANCE;

    private LifeCycledCallHelper() {}

    public static LifeCycledCallHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LifeCycledCallHelper();
        }
        return INSTANCE;
    }

    public final <T> void process(Context context, Call<T> call,
                                  ApiResponseHandler<T> responseHandler) {
        RequestManager.getInstance().add(context, call);
        enqueue(call, responseHandler);
    }

    public final <T> void process(android.app.Fragment fragment, Call<T> call,
                                  ApiResponseHandler<T> responseHandler) {
        RequestManager.getInstance().add(fragment, call);
        enqueue(call, responseHandler);
    }

    public final <T> void process(androidx.fragment.app.Fragment fragment,
                                  Call<T> call,
                                  ApiResponseHandler<T> responseHandler) {
        RequestManager.getInstance().add(fragment, call);
        enqueue(call, responseHandler);
    }

    private <T> void enqueue(Call<T> call,
                             ApiResponseHandler<T> responseHandler) {
        LifeCycleCallbackComposite<T> composite = new LifeCycleCallbackComposite<>();
        composite.add(responseHandler);
        call.enqueue(composite);
    }

}
