package individual.leobert.retrofitext.sample.dummy.demos;

import individual.leobert.retrofitext.sample.ApiClient;
import individual.leobert.retrofitext.sample.DebugUtil;
import individual.leobert.retrofitext.core.ApiModel;
import individual.leobert.retrofitext.core.ApiResponseHandler;
import individual.leobert.retrofitext.sample.dummy.DemoSet;
import individual.leobert.retrofitext.sample.dummy.IDebugDisplay;
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

public class GetDemo extends ApiModel implements DemoSet.Demo<String> {
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
    public void startWithDebug(final IDebugDisplay iDebugDisplay) {
        ApiResponseHandler<String> handler = new ApiResponseHandler<String>() {

            @Override
            public void onSuccess(int code, Call<String> call, Headers headers, String res) {
                iDebugDisplay.displayInfo(DebugUtil.debug(code, call, headers, res));
            }

            @Override
            public void onFailure(int code, Call<String> call, Headers headers, ResponseBody res) {
                iDebugDisplay.displayInfo(DebugUtil.debug(code, call, headers, res));
            }

            @Override
            public void onFinish(Call<String> call) {
                iDebugDisplay.onFinish();
            }
        };

        makeCall(null, newCallInstance(), handler);
    }


}
