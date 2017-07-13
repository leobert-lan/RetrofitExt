package individual.leobert.retrofitext.sample.dummy.demos;

import individual.leobert.retrofitext.sample.ApiClient;
import individual.leobert.retrofitext.sample.DebugUtil;
import individual.leobert.retrofitext.sample.dummy.DemoSet;
import individual.leobert.retrofitext.sample.dummy.IDebugDisplay;
import individual.leobert.retrofitext.sample.dummy.SampleServer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample.dummy.demos </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> DeleteDemo </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/19.
 */

public class DeleteDemo implements DemoSet.Demo<ResponseBody> {
    @Override
    public int getDemoType() {
        return DemoSet.DEMO_DELETE;
    }

    @Override
    public String getDemoName() {
        return getClass().getSimpleName();
    }

    @Override
    public Call<ResponseBody> newCallInstance() {
        return ApiClient.getInstance()
                .apiInstance(SampleServer.class)
                .delete(SampleServer.EntityData.demo);
    }

    @Override
    public void startWithDebug(final IDebugDisplay iDebugDisplay) {
        newCallInstance().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                iDebugDisplay.displayInfo(DebugUtil.parseSuccess1(call, response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iDebugDisplay.displayInfo(DebugUtil.parseFailure(call, t));
            }
        });
    }
}
