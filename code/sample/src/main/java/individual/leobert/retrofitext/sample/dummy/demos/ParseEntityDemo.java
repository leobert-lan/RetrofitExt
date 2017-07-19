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

import android.support.annotation.NonNull;

import individual.leobert.retrofitext.sample.ApiClient;
import individual.leobert.retrofitext.sample.DebugUtil;
import individual.leobert.retrofitext.sample.dummy.DemoSet;
import individual.leobert.retrofitext.sample.dummy.ViewInterface;
import individual.leobert.retrofitext.sample.dummy.SampleServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample.dummy.demos </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> ParseEntityDemo </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/19.
 */

public class ParseEntityDemo implements DemoSet.Demo<SampleServer.ResData> {
    @Override
    public int getDemoType() {
        return DemoSet.DEMO_PARSEENTITY_GET;
    }

    @Override
    public String getDemoName() {
        return getClass().getSimpleName();
    }

    @Override
    public Call<SampleServer.ResData> newCallInstance() {
        return ApiClient.getInstance()
                .apiInstance(SampleServer.class)
                .getAsEntity("v1");
    }

    @Override
    public void startWithDebug(final ViewInterface viewInterface) {
        newCallInstance().enqueue(new Callback<SampleServer.ResData>() {
            @Override
            public void onResponse(Call<SampleServer.ResData> call, Response<SampleServer.ResData> response) {
                viewInterface.outputLogInfo(DebugUtil.parseSuccess2(call, response));
            }

            @Override
            public void onFailure(Call<SampleServer.ResData> call, Throwable t) {
                viewInterface.outputLogInfo(DebugUtil.parseFailure(call, t));
            }

        });
    }

    @NonNull
    @Override
    public DemoSet.ProgressStyle getProgressStyle() {
        return DemoSet.ProgressStyle.DEFAULT;
    }
}
