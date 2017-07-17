package individual.leobert.retrofitext.coverter;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample.coverter </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> FastJsonRequsetBodyConverter </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/16.
 */

public class FastJsonRequestBodyConverter<T>  implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE =
            MediaType.parse("application/json; charset=UTF-8");

    @Override
    public RequestBody convert(T value) throws IOException {
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value));
    }
}