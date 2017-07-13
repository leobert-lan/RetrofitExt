package individual.leobert.retrofitext.sample.dummy;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import individual.leobert.retrofitext.sample.ApiClient;
import individual.leobert.retrofitext.sample.DebugUtil;
import individual.leobert.retrofitext.sample.R;
import individual.leobert.retrofitext.sample.core.ApiResponseHandler;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class CancelDemoActivity extends AppCompatActivity implements IDebugDisplay {
    private TextView detail;

    private Button btnStart;

    private ProgressDialog progressDialog;

    private Call call;

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
                call.cancel();
            }
        });
    }

    private void startCall() {
        call = ApiClient.getInstance()
                .apiInstance(SampleServer.class)
                .getDelay(3);

        call.enqueue(new ApiResponseHandler<ResponseBody>() {
            @Override
            public void onSuccess(int code, Call<ResponseBody> call,
                                  Headers headers, ResponseBody res) {
                displayInfo(DebugUtil.debug(code, call, headers, res));
            }

            @Override
            public void onFailure(int code, Call<ResponseBody> call,
                                  Headers headers, ResponseBody res) {
                displayInfo(DebugUtil.debug(code, call, headers, res));
            }

            @Override
            public void onCancel(Call<ResponseBody> call) {
                super.onCancel(call);
                displayInfo("canceled");
                Log.d("lmsg", "canceled" + CancelDemoActivity.this);
            }

            @Override
            public void onFinish(Call<ResponseBody> call) {
                Log.i("lmsg", "on call finish"+ CancelDemoActivity.this);
                CancelDemoActivity.this.onFinish();
            }

        });

    }

    @Override
    public void finish() {
        super.finish();
//        if (call != null && !call.isCanceled()) {
//            call.cancel();
//            Log.d("lmsg", "call cancel on finish");
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
    public void displayInfo(CharSequence info) {
//        cancelWait();
        detail.setText(info);
    }

    @Override
    public void onFinish() {
        cancelWait();
    }
}
