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

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import individual.leobert.retrofitext.sample.ApiClient;
import individual.leobert.retrofitext.sample.DebugUtil;
import individual.leobert.retrofitext.sample.dummy.DemoSet;
import individual.leobert.retrofitext.sample.dummy.ViewInterface;
import individual.leobert.retrofitext.sample.dummy.SampleServer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample.dummy.demos </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> FileUploadDemo </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/20.
 */

public class FileUploadDemo implements DemoSet.Demo<ResponseBody> {
    @Override
    public int getDemoType() {
        return DemoSet.DEMO_FILE_UPLOAD;
    }

    @Override
    public String getDemoName() {
        return getClass().getSimpleName();
    }

    @Override
    public Call<ResponseBody> newCallInstance() {
        Map<String , RequestBody> param = new HashMap<>();
        File test = Utils.createTempFile("test_file_2",2000);//2kb

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"),test);

        param.put("attachment",requestBody);
        //------------------


        List<MultipartBody.Part> parts = new ArrayList<>();
        parts.add(MultipartBody.Part.createFormData("object_type","feedback"));
        parts.add(MultipartBody.Part.createFormData("insert_only","1"));
        parts.add(MultipartBody.Part.createFormData("attachment",
                test.getName(),requestBody));

        return ApiClient.getInstance()
                .apiInstance(SampleServer.class)
                .vsoUpload(parts);//vso demo
//                .upload(param); //httpbin demo
    }

    @Override
    public void startWithDebug(final ViewInterface viewInterface) {

        newCallInstance().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                viewInterface.outputLogInfo(DebugUtil.parseSuccess1(call, response));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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
