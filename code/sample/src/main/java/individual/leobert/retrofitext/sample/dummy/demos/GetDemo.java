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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import individual.leobert.retrofitext.sample.ApiClient;
import individual.leobert.retrofitext.sample.DebugUtil;
import individual.leobert.retrofitext.ext.ApiResponseHandler;
import individual.leobert.retrofitext.sample.dummy.DemoSet;
import individual.leobert.retrofitext.sample.dummy.ViewInterface;
import individual.leobert.retrofitext.sample.dummy.SampleServer;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample.dummy </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> GetDemo </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/16.
 */

public class GetDemo implements DemoSet.Demo<String> {
    @Override
    public int getDemoType() {
        return DemoSet.DEMO_GET;
    }

    @Override
    public String getDemoName() {
        return getClass().getSimpleName();
    }

    @Override
    public Call<String> newCallInstance() {
        return ApiClient.getInstance()
                .apiInstance(SampleServer.class)
                .get("value1");
    }

    @Override
    public void startWithDebug(final ViewInterface viewInterface) {
        ApiResponseHandler<String> handler = new ApiResponseHandler<String>() {

            @Override
            public void onSuccess(int code, Call<String> call, Headers headers, String res) {
                viewInterface.outputLogInfo(DebugUtil.debug(code, call, headers, res));
            }

            @Override
            public void onFailure(int code, Call<String> call, Headers headers, ResponseBody res) {
                viewInterface.outputLogInfo(DebugUtil.debug(code, call, headers, res));
            }

            @Override
            public void onFinish(Call<String> call) {
                viewInterface.onCallFinish();
            }
        };

        callHelper.process((Fragment) null, newCallInstance(), handler);
    }

    @NonNull
    @Override
    public DemoSet.ProgressStyle getProgressStyle() {
        return DemoSet.ProgressStyle.DEFAULT;
    }
}
