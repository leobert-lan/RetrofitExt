package individual.leobert.retrofitext.sample.dummy;

import java.util.List;
import java.util.Map;

import individual.leobert.retrofitext.core.ApiDef;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample.dummy </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> GetSampleServer </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/16.
 */
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
