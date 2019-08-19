package android.text;

/**
 * @Author: Jereme
 * @CreateDate: 2019-08-19
 */
public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.equals("");
    }
}
