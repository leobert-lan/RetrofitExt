package individual.leobert.retrofitext.core;

import java.lang.annotation.Annotation;

/**
 * <p><b>Package:</b> individual.leobert.retrofitext.core.</p>
 * <p><b>Project:</b> PermissionDemo </p>
 * <p><b>Classname:</b> ApiDefCheckUtil </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/6/16.
 */

public class ApiDefCheckUtil {
    public static boolean isAllowedApi(Class clazz) {
        Annotation apiDef = clazz.getAnnotation(ApiDef.class);
        return apiDef != null;
    }
}
