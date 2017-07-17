package individual.leobert.retrofitext.core;

import android.content.Context;

import retrofit2.Call;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.core.</p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> ApiModel </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/21.
 */

public abstract class ApiModel {

    protected final <T> void makeCall(Context context, Call<T> call,
                                      ApiResponseHandler<T> responseHandler) {
        RequestManager.getInstance().add(context, call);

        LifeCycleCallbackComposite<T> composite = new LifeCycleCallbackComposite<>();
        composite.add(responseHandler);
        call.enqueue(composite);
    }

}
