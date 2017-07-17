package individual.leobert.retrofitext.sample.dummy.demos;

import individual.leobert.retrofitext.sample.ApiClient;
import individual.leobert.retrofitext.sample.DebugUtil;
import individual.leobert.retrofitext.sample.dummy.DemoSet;
import individual.leobert.retrofitext.sample.dummy.IDebugDisplay;
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
    public void startWithDebug(final IDebugDisplay iDebugDisplay) {
        newCallInstance().enqueue(new Callback<SampleServer.ResData>() {
            @Override
            public void onResponse(Call<SampleServer.ResData> call, Response<SampleServer.ResData> response) {
                iDebugDisplay.displayInfo(DebugUtil.parseSuccess2(call, response));
            }

            @Override
            public void onFailure(Call<SampleServer.ResData> call, Throwable t) {
                iDebugDisplay.displayInfo(DebugUtil.parseFailure(call, t));
            }

        });
    }
}
