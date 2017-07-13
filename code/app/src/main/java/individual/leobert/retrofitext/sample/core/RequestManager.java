package individual.leobert.retrofitext.sample.core;

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
 * <p><b>Package:</b> individual.leobert.retrofitext.sample.core </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> RequsetManager </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/20.
 */

public class RequestManager {
    private static final String LOG_TAG = "RequestManager";

//    private ConcurrentHashMap<String, List<WeakReference<Call>>> callMap;

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
        final List requestList = (List) this.requestMap.get(key);
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

    private void cancelRequests(List<Call> requestList) {
        if (requestList != null) {
            Iterator iterator = requestList.iterator();
            while (iterator.hasNext()) {
                Call requestHandle = (Call) iterator.next();
                requestHandle.cancel();
            }
        }
    }

    synchronized void add(Context context, Call call) {
        if (call == null)
            return;

        final String key = getKey(context);

//        Map iterator = this.requestMap;
        List requestList;
        synchronized (this.requestMap) {
            requestList = (List) this.requestMap.get(context);
            if (requestList == null) {
                requestList = Collections.synchronizedList(new LinkedList());
                this.requestMap.put(key, requestList);
            }
        }

        requestList.add(call);
    }

    synchronized void remove(Call call) {
        if (call == null) {
            Log.d(LOG_TAG, "remove, passed call is null, cannot proceed");
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


//    synchronized void remove(Call call) {
//        for (callMap.)
//    }

//    private synchronized void removeItemIfNecessary(List<WeakReference<Call>> calls) {
//        Log.d(LOG_TAG,"removeItemIfNecessary:size before handle:"+calls.size());
//        if (calls.size() %10 == 0) {
//            int i = 0;
//            while (i<calls.size()) {
//                WeakReference callRef = calls.get(i);
//                if (callRef == null
//                        || callRef.get() == null) {
//                    calls.remove(i);
//                    continue;
//                }
//                i++;
//            }
//        }
//        Log.d(LOG_TAG,"removeItemIfNecessary:size after handle:"+calls.size());
//    }


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
