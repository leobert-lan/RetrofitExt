package individual.leobert.retrofitext.coverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample.coverter </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> FastJsonConverterFactory </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/16.
 */

public class FastJsonConverterFactory extends Converter.Factory {
    public static FastJsonConverterFactory create() {
        return new FastJsonConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new FastJsonResponseBodyConverter<>(type);
    }


    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        return new FastJsonRequestBodyConverter<>();
    }

}
