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

package individual.leobert.retrofitext;

import android.content.Context;

import java.lang.reflect.Constructor;

import individual.leobert.retrofitext.ext.ApiResponseHandler;
import individual.leobert.retrofitext.ext.LifeCycledCallHelper;
import retrofit2.Call;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext </p>
 * <p><b>Project:</b> code </p>
 * <p><b>Classname:</b> RetrofitProxy </p>
 * <p><b>Description:</b> 入口 </p>
 * Created by leobert on 2017/7/17.
 */

public class RetrofitExt {

    private RetrofitExt() {
    }

    private static LifeCycledCallHelper lifeCycledCallHelper = LifeCycledCallHelper.getInstance();

    /**
     * Get proxy class for given retrofitAPI, which will take care of Call's lifecycle
     *
     * @param retrofitAPI          api interface that defines the request
     * @param retrofitAPIImplement api implementation generated by retrofit(result of Retrofit.create())
     * @param <T>                  api interface that defines the request
     * @return proxy class that takes care of Call's lifecycle
     */
    public static <T> T getProxyInterface(Class<T> retrofitAPI, T retrofitAPIImplement) {
        if (retrofitAPIImplement == null) {
            throw new NullPointerException("retrofitAPIImplement can not be null");
        }

        String className = retrofitAPI.getName();
        if (className.startsWith("android.") || className.startsWith("java.")) {
            throwsProxyClassNotFoundException(className);
        }

        T proxyClassInstance = null;
        try {
            Class<?> bindingClass = Class.forName(className + "InvokeProxy");
            Constructor<T> constructor =
                    (Constructor<T>) bindingClass.getConstructor(retrofitAPI);
            proxyClassInstance = constructor.newInstance(retrofitAPIImplement);
        } catch (Exception e) {
            e.printStackTrace();
            throwsProxyClassNotFoundException(className);
        }
        return proxyClassInstance;
    }

    private static void throwsProxyClassNotFoundException(String className) {
        throw new IllegalArgumentException("Cannot find proxy class for " + className
                + ". Did you forget to add @ApiDef?");
    }

    public static final <T> void lifeCycledWithContext(Context context, Call<T> call,
                                                       ApiResponseHandler<T> responseHandler) {
        lifeCycledCallHelper.process(context, call, responseHandler);
    }

    public static final <T> void lifeCycledWithFragment(android.app.Fragment fragment, Call<T> call,
                                                        ApiResponseHandler<T> responseHandler) {
        lifeCycledCallHelper.process(fragment, call, responseHandler);
    }

    public static final <T> void lifeCycledWithFragmentV4(androidx.fragment.app.Fragment fragment,
                                                          Call<T> call,
                                                          ApiResponseHandler<T> responseHandler) {
        lifeCycledCallHelper.process(fragment, call, responseHandler);
    }
}
