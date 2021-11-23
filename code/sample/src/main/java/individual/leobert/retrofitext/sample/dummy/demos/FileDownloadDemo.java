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

import android.os.Environment;
import androidx.annotation.NonNull;

import java.io.File;

import individual.leobert.retrofitext.sample.ApiClient;
import individual.leobert.retrofitext.sample.DebugUtil;
import individual.leobert.retrofitext.ext.AsyncFileResponseHandler;
import individual.leobert.retrofitext.sample.dummy.DemoSet;
import individual.leobert.retrofitext.sample.dummy.ViewInterface;
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
    public void startWithDebug(final ViewInterface viewInterface) {

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
                        viewInterface.outputLogInfo(log);
                    }



                    @Override
                    public void onThrow(Throwable t) {
                        super.onThrow(t);
                        viewInterface.outputLogInfo(DebugUtil.parseFailure(call,t));
                    }

                    @Override
                    public void onFailure(int code, Call<ResponseBody> call) {
                        viewInterface.outputLogInfo("code:"+code);
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

    @NonNull
    @Override
    public DemoSet.ProgressStyle getProgressStyle() {
        return DemoSet.ProgressStyle.DOWNLOAD;
    }

}
