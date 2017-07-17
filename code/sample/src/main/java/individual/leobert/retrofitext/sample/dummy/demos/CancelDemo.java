package individual.leobert.retrofitext.sample.dummy.demos;

import android.os.Handler;

import individual.leobert.retrofitext.sample.ApiClient;
import individual.leobert.retrofitext.sample.DebugUtil;
import individual.leobert.retrofitext.core.ApiResponseHandler;
import individual.leobert.retrofitext.sample.dummy.DemoSet;
import individual.leobert.retrofitext.sample.dummy.IDebugDisplay;
import individual.leobert.retrofitext.sample.dummy.SampleServer;
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
    public void startWithDebug(final IDebugDisplay iDebugDisplay) {
        final Call<ResponseBody> call = newCallInstance();

        call.enqueue(new ApiResponseHandler<ResponseBody>() {
            @Override
            public void onSuccess(int code, Call<ResponseBody> call,
                                  Headers headers, ResponseBody res) {
                iDebugDisplay.displayInfo(DebugUtil.debug(code,call,headers,res));
            }

            @Override
            public void onFailure(int code, Call<ResponseBody> call,
                                  Headers headers, ResponseBody res) {
                iDebugDisplay.displayInfo(DebugUtil.debug(code,call,headers,res));
            }

            @Override
            public void onCancel(Call<ResponseBody> call) {
                super.onCancel(call);
                iDebugDisplay.displayInfo("canceled");
            }

            @Override
            public void onFinish(Call<ResponseBody> call) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                call.cancel();
            }
        }, (delay - 1) * 1000);

    }
}
