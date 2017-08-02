# RetrofitExt
A library in android focus on simplifying the management of retrofit and the life-cycle of it's calls, support file uploading &amp; downloading

## Write at the starting
Because my mistake, I forgot to create a repo in github, so many hours spent to find the project. Awrrr...
I will never do anything such stupid! Project will be push tomotomorrow.


## Get Started With RetrofitExt

use jcenter

```
allprojects {
    repositories {
        jcenter {
            url "http://jcenter.bintray.com/"
        }
    }
}
```

define dependence of retrofit:

```
compile 'com.squareup.retrofit2:retrofit:2.3.0'
```

release 1.0.1 depends on retrofit-2.3.0,but just use "Provided" not compile

define dependences of RetrofitExt:

```
ompile 'individual.leobert.libs:retrofitext:1.0.1'
compile 'individual.leobert.libs:retrofitext-core:1.0.1'
```
both required!

and AA:

```
annotationProcessor 'individual.leobert.libs:retrofitext-compiler:1.0.1'
```

## What can RetrofitExt do for you?

* FastJsonConverter, if you are using Alibaba's FastJson
* StringConverter，
* **Two ways to manage the lifecycle of retrofit calls**
* easy download, **not support for resume from break point**

## How to use?
Just like retrofit,and works via callback,better for MVC/MVP

### First at all,define a singelton to manage retrofit.
Maybe named ApiClient or something others

```
public class ApiClient {
    private static boolean isLogEnable = false;
    private static final String TAG = "ApiClient";

    private static final Map<Class, Object> sInterfaceImplementCache =
            new ConcurrentHashMap<>();


    public static void setLogEnable(boolean isLogEnable) {
        ApiClient.isLogEnable = isLogEnable;
        individual.leobert.retrofitext.ext.Debug.setDebugEnable(isLogEnable);
    }

    private Retrofit retrofit = null;

    private static ApiClient apiClient;
    private static final String KEY_USER_AGENT = "User-Agent";

    // TODO: custom ua
    private static final String CUSTOM_USER_AGENT = "MY CUSTOM UA";

    private ApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://httpbin.org/")
                .client(newCustomUaClient())
//                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();
    }

    public static ApiClient getInstance() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        return apiClient;
    }

    public <T> T apiInstance(Class<T> remoteApiClazz) {
        if (!ApiDefCheckUtil.isAllowedApi(remoteApiClazz))
            throw new IllegalArgumentException("must give a interface with @apiDef annotation");
//        T api = retrofit.create(remoteApiClazz);
        T proxyApi = RetrofitExt.getProxyInterface(remoteApiClazz, getImpl(remoteApiClazz));
        return proxyApi;
    }

    private <T> T getImpl(Class<T> remoteApiClazz) {
        T apiImplement;
        Object cacheApiImplement = sInterfaceImplementCache.get(remoteApiClazz);
        if (cacheApiImplement != null) {
            apiImplement = remoteApiClazz.cast(cacheApiImplement);
        } else {
            apiImplement = retrofit.create(remoteApiClazz);
            sInterfaceImplementCache.put(remoteApiClazz, apiImplement);
        }
        return apiImplement;
    }

    private OkHttpClient newCustomUaClient() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader(KEY_USER_AGENT, CUSTOM_USER_AGENT)
                                .build();
                        return chain.proceed(request);
                    }
                }).build();
    }

    public static void shutdown() {
        if (apiClient == null)
            return;
        apiClient.retrofit = null;
        RequestManager.shutdown();
    }

    public static void cancel(Activity activity) {
        RequestManager.getInstance().cancel(activity);
    }

    public static void cancel(Fragment fragment) {
        RequestManager.getInstance().cancel(fragment);
    }

    public static void cancel(android.support.v4.app.Fragment fragment) {
        RequestManager.getInstance().cancel(fragment);
    }

}
```

In this case, you can use a custom User-Agent and a FastJsonConverter.

*Now leave all functions aside, we will explain those later.*

###  Then,define a retrofitInterface as usual

we use RestfulApis of httpbin for demo,we have done this:
`baseUrl("http://httpbin.org/")` to define the base url

```
@ApiDef
public interface SampleServer {


    @GET("get")
    Call<String> get(@Query("param1") String value1);

    @GET("get")
    Call<ResData> getAsEntity(@Query("param1") String value1);

    @GET("delay/{n}")
    Call<ResponseBody> getDelay(@Path("n") int sec);


    @POST("post")
    Call<ResponseBody> post(@Body EntityData entityData);

    @PUT("put")
    Call<ResponseBody> put(@Body EntityData entityData);

    //    @DELETE("delete")
    @HTTP(method = "DELETE", path = "delete", hasBody = true)
    Call<ResponseBody> delete(@Body EntityData entityData);

    @GET("robots.txt")
    @Streaming
    Call<ResponseBody> download();

    @POST("post")
    @Multipart
    @Streaming
    Call<ResponseBody> upload(@PartMap Map<String, RequestBody> params);

    @POST("https://maker.vsochina.com/api/vso/file/upload")
    @Multipart
    @Streaming
    Call<ResponseBody> vsoUpload(@Part List<MultipartBody.Part> params);


    class EntityData {
        public static EntityData demo = new EntityData();

        static {
            demo.setP1("v1");
            demo.setP2(2);
        }

        private String p1;
        private int p2;

        public String getP1() {
            return p1;
        }

        public void setP1(String p1) {
            this.p1 = p1;
        }

        public int getP2() {
            return p2;
        }

        public void setP2(int p2) {
            this.p2 = p2;
        }
    }

    class ResData {
        private String args;
        private String headers;
        private String origin;
        private String url;

        public String getArgs() {
            return args;
        }

        public void setArgs(String args) {
            this.args = args;
        }

        public String getHeaders() {
            return headers;
        }

        public void setHeaders(String headers) {
            this.headers = headers;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
```

**you must use annotation:** `@ApiDef`,this is different from retrofit. By the way,this annotation is use to generate ProxyImpl for the Implementions of RetrofitInterface, which can manage lifecycle of calls.

### Generate a Call Instance

```
public class GetDemo implements DemoSet.Demo<String> {
    
    @Override
    public Call<String> newCallInstance() {
        return ApiClient.getInstance()
                .apiInstance(SampleServer.class)
                .get("value1");
    }
    
    //...
}
```

Just like retrofit!

### define a callback

```
ApiResponseHandler<String> handler = new ApiResponseHandler<String>() {

            @Override
            public void onSuccess(int code, Call<String> call, Headers headers, String res) {
                viewInterface.outputLogInfo(DebugUtil.debug(code, call, headers, res));
            }

            @Override
            public void onFailure(int code, Call<String> call, Headers headers, ResponseBody res) {
                viewInterface.outputLogInfo(DebugUtil.debug(code, call, headers, res));
            }

            @Override
            public void onFinish(Call<String> call) {
                viewInterface.onCallFinish();
            }
        };
```

> implement and handle 200+: onSuccess {@link #onSuccess(int, Call, Headers, Object)}
> 
> implement and handle 400+&500+ types: onFailure {@link #onFailure(int, Call, Headers, ResponseBody)}
> 
> implement and handle "finish" signal: onFinish {@link #onFinish(Call)}
> 
> other listeners:
> 
> listen cancel: override onCancel {@link #onCancel(Call)}
> 
> listen throw: override onThrow {@link #onThrow(Throwable)}


If you have use Android-Async-Http,you will feel familier with this.

### launch call
We said there are two ways to manage retrofit calls ahead, one way is use the **Context** , **android.app.Fragment** or **android.support.v4.app.Fragment**.But we won't keep the instance of Context or Fragment.

In this way for management

you should get an instance of LifeCycledCallHelper:

```
 LifeCycledCallHelper callHelper = LifeCycledCallHelper.getInstance();
```

and launch call with those api:

* void process(android.content.Context context, retrofit2.Call<T> call, individual.leobert.retrofitext.ext.ApiResponseHandler<T> responseHandler)
* void process(android.app.Fragment fragment, retrofit2.Call<T> call, individual.leobert.retrofitext.ext.ApiResponseHandler<T> responseHandler)
* void process(android.support.v4.app.Fragment fragment, retrofit2.Call<T> call, individual.leobert.retrofitext.ext.ApiResponseHandler<T> responseHandler)

**we strongly suggest to use the real Context or Fragment.** By the way, "null" will not throw exception, `callHelper.process((Fragment) null, newCallInstance(), handler);`

### How to manage calls
In retrofit, you should keep the instance of retrofitCall and use `cancel()` to cancel it.

In RetrofitExt

* way1:

use RequestManager:

```
public static void cancel(Activity activity) {
        RequestManager.getInstance().cancel(activity);
    }

    public static void cancel(Fragment fragment) {
        RequestManager.getInstance().cancel(fragment);
    }

    public static void cancel(android.support.v4.app.Fragment fragment) {
        RequestManager.getInstance().cancel(fragment);
    }
```
you can find those in ApiClient.

when you call finish to a activity,you can call `RequestManager.getInstance().cancel(activity);`
to cancel all RetrofitCalls binding to this activity

Fragment may called at onStop.

* way2
when create a RetrofitInterface Impl,like:`ApiClient.getInstance()
                .apiInstance(SampleServer.class)`
                
actually, we get a ProxyImpl:

```
public class SampleServerInvokeProxy implements SampleServer, Cancelable {
  private SampleServer mInterfaceImpl;

  private List<Call> mCallList = Collections.synchronizedList(new ArrayList<Call>());

  public SampleServerInvokeProxy(SampleServer apiImplGeneratedByRetrofit) {
    mInterfaceImpl = apiImplGeneratedByRetrofit;
  }

  public Call<String> get(String value1) {
    Call<String> call = mInterfaceImpl.get(value1);
    mCallList.add(call);
    return call;
  }

  public Call<SampleServer.ResData> getAsEntity(String value1) {
    Call<SampleServer.ResData> call = mInterfaceImpl.getAsEntity(value1);
    mCallList.add(call);
    return call;
  }

 //...
 
  public void cancelAll(Call... excludes) {
    if (excludes.length > 0) {
        mCallList.removeAll(Arrays.asList(excludes));
    }
    if (mCallList != null) {
       for (Call call : mCallList) {
           if (call != null && !call.isCanceled()) {
               call.cancel();
           }
     }
     mCallList.clear();
     mCallList = null;
    }
  }
}
```

it will keep calls, so you should keep the instance of SampleServerInvokeProxy, and call cancelAll when you want to exit. Excellent in MVP, list and separate your RemoteApi into RetrofitInterfaces as proprietary Models selectively(Normally, one module have one RetrofitInterface)。


### Something verbose
you can choose one way to manage the calls.

But i strongly suggest you to use LifeCycledCallHelper to launch call, it will do many things at the backend. Yes, as you guess, we keep the calls in RequestManager,but for any call launched by LifeCycledCallHelpler,it will be droped out when cancel called!

you can learn how to use download and upload in the sample. but the sample is too simple to display the lifecycle management.

## Future Plans
* Add BaseUrl Manage Feature
* Add Converter Manage Feature
	* If your remote RestApi server returns simple words,nor Json, exception may occur with FastJsonConverter.
* optimize the sample,with MVP architecture。 	
