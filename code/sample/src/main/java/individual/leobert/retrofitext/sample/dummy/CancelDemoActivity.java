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

package individual.leobert.retrofitext.sample.dummy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import individual.leobert.retrofitext.ext.ApiResponseHandler;
import individual.leobert.retrofitext.ext.RequestManager;
import individual.leobert.retrofitext.sample.ApiClient;
import individual.leobert.retrofitext.sample.DebugUtil;
import individual.leobert.retrofitext.sample.R;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class CancelDemoActivity extends AppCompatActivity implements ViewInterface {
    private TextView detail;

    private Button btnStart;

    private ProgressDialog progressDialog;

    private Call call;

    SampleServer impl = ApiClient.getInstance()
            .apiInstance(SampleServer.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_demo);

        progressDialog = new ProgressDialog(this);

        detail = (TextView) findViewById(R.id.item_detail);
        btnStart = (Button) findViewById(R.id.start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                startCall();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                process.cancel();
                RequestManager.getInstance().cancelAll(impl);
            }
        });
    }

    private void startCall() {
//        process = ApiClient.getInstance()
//                .apiInstance(SampleServer.class)
//                .getDelay(3);

        call = impl.getDelay(3);

        call.enqueue(new ApiResponseHandler<ResponseBody>() {
            @Override
            public void onSuccess(int code, Call<ResponseBody> call,
                                  Headers headers, ResponseBody res) {
                outputLogInfo(DebugUtil.debug(code, call, headers, res));
            }

            @Override
            public void onFailure(int code, Call<ResponseBody> call,
                                  Headers headers, ResponseBody res) {
                outputLogInfo(DebugUtil.debug(code, call, headers, res));
            }

            @Override
            public void onCancel(Call<ResponseBody> call) {
                super.onCancel(call);
                outputLogInfo("canceled");
                Log.d("lmsg", "canceled" + CancelDemoActivity.this);
            }

            @Override
            public void onFinish(Call<ResponseBody> call) {
                Log.i("lmsg", "on process finish"+ CancelDemoActivity.this);
                CancelDemoActivity.this.onCallFinish();
            }

        });

    }

    @Override
    public void finish() {
        super.finish();
//        if (process != null && !process.isCanceled()) {
//            process.cancel();
//            Log.d("lmsg", "process cancel on finish");
//        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.i("lmsg", "gc success");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lmsg", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lmsg","onDestroy");
//        System.gc();
//        Runtime.getRuntime().gc();
//        System.runFinalization();
    }

    private void cancelWait() {
        if (progressDialog != null) {//&& progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void outputLogInfo(CharSequence info) {
//        cancelWait();
        detail.setText(info);
    }

    @Override
    public void onCallFinish() {
        cancelWait();
    }

}
