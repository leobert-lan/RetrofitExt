package individual.leobert.retrofitext.sample.coverter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.sample.coverter </p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> StringResponseBodyConverter </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/21.
 */

class StringResponseBodyConverter implements Converter<ResponseBody, String> {
    @Override
    public String convert(ResponseBody value) throws IOException {
        try {
            return value.string();
        } finally {
            value.close();
        }
    }
}
