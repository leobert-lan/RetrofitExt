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

package individual.leobert.retrofitext.core;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import retrofit2.Call;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.core.</p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> RequsetManager </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/20.
 */

public class RequestManager {
    private static final String LOG_TAG = "RequestManager";

    private final Map<String, List<Call>> requestMap;

    private static RequestManager requestManager;

    private RequestManager() {
        this.requestMap = Collections.synchronizedMap(new WeakHashMap<String, List<Call>>());
    }

    public static RequestManager getInstance() {
        if (requestManager == null) {
            requestManager = new RequestManager();
        }
        return requestManager;
    }

    public synchronized void cancel(Context context) {
        final String key = getKey(context);
        cancel(key);
    }

    public synchronized void cancel(android.app.Fragment fragment) {
        final String key = getKey(fragment);
        cancel(key);
    }

    public synchronized void cancel(android.support.v4.app.Fragment fragment) {
        final String key = getKey(fragment);
        cancel(key);
    }

    private synchronized void cancel(String key) {
        final List<Call> requestList = this.requestMap.get(key);
        this.requestMap.remove(key);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Runnable runnable = new Runnable() {
                public void run() {
                    RequestManager.this.cancelRequests(requestList);
                }
            };
            new Handler().post(runnable);
        } else {
            this.cancelRequests(requestList);
        }
    }

    /**
     * Cancel all Calls of the given proxy class
     *
     * @param retrofitAPI proxy class that returned by getProxyInterface()
     * @param excludes    Calls not to be cancelled
     */
    public void cancelAll(Object retrofitAPI, Call... excludes) {
        if (retrofitAPI instanceof Cancelable) {
            ((Cancelable) retrofitAPI).cancelAll(excludes);
        } else {
            throw new IllegalArgumentException(retrofitAPI.getClass().getName()
                    + " must implement Cancelable");
        }
    }

    private void cancelRequests(List<Call> requestList) {
        if (requestList != null) {
            for (Call requestHandle : requestList) {
                if (requestHandle != null && !requestHandle.isCanceled())
                    requestHandle.cancel();
                else
                    Log.d(LOG_TAG,"cannot process this process:"+requestHandle);
            }
        }
    }

    synchronized void add(Context context, Call call) {
        if (call == null)
            return;
        final String key = getKey(context);
        add(key,call);
    }

    synchronized void add(android.app.Fragment fragment, Call call) {
        if (call == null)
            return;
        final String key = getKey(fragment);
        add(key,call);
    }

    synchronized void add(android.support.v4.app.Fragment fragment, Call call) {
        if (call == null)
            return;
        final String key = getKey(fragment);
        add(key,call);
    }

    private synchronized void add(String key,Call call) {
        List<Call> requestList;
        synchronized (this.requestMap) {
            requestList =  this.requestMap.get(key);
            if (requestList == null) {
                requestList = Collections.synchronizedList(new LinkedList<Call>());
                this.requestMap.put(key, requestList);
            }
        }

        requestList.add(call);
    }

    synchronized void remove(Call call) {
        if (call == null) {
            Log.d(LOG_TAG, "remove, passed process is null, cannot proceed");
        } else {
            Iterator var4 = this.requestMap.values().iterator();

            while (true) {
                List requestList;
                do {
                    if (!var4.hasNext()) {
                        return;
                    }

                    requestList = (List) var4.next();
                } while (requestList == null);

                Iterator var6 = requestList.iterator();

                while (var6.hasNext()) {
                    Call request = (Call) var6.next();
                    if (call.equals(request)) {
                        var6.remove();
                    }
                }
            }
        }
    }

    private <T> String getKey(T t) {
        if (t != null)
            return t.toString();
        else
            return "Null";
    }




    public synchronized static void shutdown() {
        if (requestManager != null) {
            if (getInstance().requestMap != null) {
                while (getInstance().requestMap.size() > 0) {
                    Set<String> keys = getInstance().requestMap.keySet();
                    String key = keys.iterator().next();
                    getInstance().cancel(key);
                }
            }
            getInstance().requestMap.clear();
//            getInstance().requestMap = null;
            requestManager = null;
        }
    }

}
