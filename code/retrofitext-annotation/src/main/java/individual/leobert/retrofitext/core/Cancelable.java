package individual.leobert.retrofitext.core;

import retrofit2.Call;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.core </p>
 * <p><b>Project:</b> code </p>
 * <p><b>Classname:</b> Cancelable </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/7/17.
 */

public interface Cancelable {
    void cancelAll(Call... excludes);
}
