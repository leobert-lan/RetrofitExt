package individual.leobert.retrofitext.compiler;

import java.util.Iterator;

class Utils {
    public static String join(Iterator iterator, String separator) {
        if (separator == null) {
            separator = "";
        }

        StringBuilder buf = new StringBuilder(256);

        while (iterator.hasNext()) {
            buf.append(iterator.next());
            if (iterator.hasNext()) {
                buf.append(separator);
            }
        }

        return buf.toString();
    }
}
