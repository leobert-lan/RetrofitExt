package individual.leobert.retrofitext.sample.dummy.demos;

import android.os.Environment;

import java.io.File;

import individual.leobert.retrofitext.sample.ApiClient;
import individual.leobert.retrofitext.sample.DebugUtil;
import individual.leobert.retrofitext.core.AsyncFileResponseHandler;
import individual.leobert.retrofitext.sample.dummy.DemoSet;
import individual.leobert.retrofitext.sample.dummy.IDebugDisplay;
import individual.leobert.retrofitext.sample.dummy.SampleServer;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample.dummy.demos </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> FileDownloadDemo </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/19.
 */

public class FileDownloadDemo implements DemoSet.Demo<ResponseBody> {
    @Override
    public int getDemoType() {
        return DemoSet.DEMO_FILE_DOWNLOAD;
    }

    @Override
    public String getDemoName() {
        return getClass().getSimpleName();
    }

    @Override
    public Call<ResponseBody> newCallInstance() {
        return ApiClient.getInstance()
                .apiInstance(SampleServer.class)
                .download();
    }

    @Override
    public void startWithDebug(final IDebugDisplay iDebugDisplay) {

        File file = new File(Environment
                .getExternalStorageDirectory(),"robot.txt");

        newCallInstance()
                .enqueue(new AsyncFileResponseHandler(file) {

                    @Override
                    public void onProgress(Call<ResponseBody> call, long current, long total) {

                    }

                    @Override
                    public void onResponse(Call<ResponseBody> call, File file) {
                        String log = debugFile(file);
                        iDebugDisplay.displayInfo(log);
                    }



                    @Override
                    public void onThrow(Throwable t) {
                        super.onThrow(t);
                        iDebugDisplay.displayInfo(DebugUtil.parseFailure(call,t));
                    }

                    @Override
                    public void onFailure(int code, Call<ResponseBody> call) {
                        iDebugDisplay.displayInfo("code:"+code);
                    }

                    private String debugFile(File file) {
                        StringBuilder stringBuilder = new StringBuilder();
                        if (file == null || !file.exists()) {
                            stringBuilder.append("Response is null");
                            return stringBuilder.toString();
                        }
                        try {
                            stringBuilder.append(file.getAbsolutePath()).append("\r\n\r\n")
                                    .append(Utils.getStringFromFile(file));
                        } catch (Throwable t) {
                            stringBuilder.append("Cannot debug file contents").append(t.getMessage());
                        }
                        if (!deleteTargetFile()) {
                            stringBuilder.append("Could not delete response file ")
                                    .append(file.getAbsolutePath());
                        }

                        return stringBuilder.toString();
                    }
                });
    }


}
