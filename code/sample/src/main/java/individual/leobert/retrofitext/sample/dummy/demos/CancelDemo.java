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

package individual.leobert.retrofitext.sample.dummy.demos;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import individual.leobert.retrofitext.core.ApiResponseHandler;
import individual.leobert.retrofitext.sample.ApiClient;
import individual.leobert.retrofitext.sample.DebugUtil;
import individual.leobert.retrofitext.sample.dummy.DemoSet;
import individual.leobert.retrofitext.sample.dummy.SampleServer;
import individual.leobert.retrofitext.sample.dummy.ViewInterface;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample.dummy.demos </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> CancelDemo </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/20.
 */

public class CancelDemo implements DemoSet.Demo<ResponseBody> {
    int delay = 3;

    @Override
    public int getDemoType() {
        return DemoSet.DEMO_CANCEL;
    }

    @Override
    public String getDemoName() {
        return getClass().getSimpleName();
    }

    @Override
    public Call<ResponseBody> newCallInstance() {
        return ApiClient.getInstance()
                .apiInstance(SampleServer.class)
                .getDelay(delay);
    }

    @Override
    public void startWithDebug(final ViewInterface viewInterface) {
        final Call<ResponseBody> call = newCallInstance();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                call.cancel();
            }
        }, (delay - 1) * 1000);
        ApiResponseHandler<ResponseBody> responseHandler = new ApiResponseHandler<ResponseBody>() {
            @Override
            public void onSuccess(int code, Call<ResponseBody> call,
                                  Headers headers, ResponseBody res) {
                viewInterface.outputLogInfo(DebugUtil.debug(code, call, headers, res));
            }

            @Override
            public void onFailure(int code, Call<ResponseBody> call,
                                  Headers headers, ResponseBody res) {
                viewInterface.outputLogInfo(DebugUtil.debug(code, call, headers, res));
            }

            @Override
            public void onCancel(Call<ResponseBody> call) {
                super.onCancel(call);
                viewInterface.outputLogInfo("canceled");
            }

            @Override
            public void onFinish(Call<ResponseBody> call) {
                viewInterface.onCallFinish();
            }
        };

        if (viewInterface instanceof Fragment) {
            callHelper.process((Fragment) viewInterface,call,responseHandler);
        } else {
            callHelper.process((Context) viewInterface,call,responseHandler);
        }
    }

    @NonNull
    @Override
    public DemoSet.ProgressStyle getProgressStyle() {
        return DemoSet.ProgressStyle.DEFAULT;
    }
}
