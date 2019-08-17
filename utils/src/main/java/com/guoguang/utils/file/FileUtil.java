package com.guoguang.utils.file;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * @Author: Jereme
 * @CreateDate: 2019-08-18
 */
public class FileUtil {

    /**
     * copyFile
     * @param path
     * @param target
     * @throws IOException
     */
    public static void copyFile(String path, File target) throws IOException {
        String[] sources = new File(path).list();
        if (sources == null) {
            target.getParentFile().mkdirs();
            if (!path.contains("._")) {
                BufferedSource bufferedSource = Okio.buffer(Okio.source(new File(path)));
                BufferedSink bufferedSink = Okio.buffer(Okio.sink(target));
                bufferedSource.readAll(bufferedSink);
                bufferedSink.flush();
                bufferedSink.close();
                bufferedSource.close();
            }
        } else {
            for (String source : sources) {
                File dir = new File(target, source);
                copyFile(path + File.separator + source, dir);
            }
        }
    }

    /**
     * copyAssets
     * @param context
     * @param path
     * @param target
     * @throws IOException
     */
    public static void copyAssets(Context context, String path, File target) throws IOException {
        String[] sources = context.getAssets().list(path);
        if (sources.length == 0) {
            target.getParentFile().mkdirs();
            BufferedSource bufferedSource = Okio.buffer(Okio.source(context.getAssets().open(path)));
            BufferedSink bufferedSink = Okio.buffer(Okio.sink(target));
            bufferedSource.readAll(bufferedSink);
            bufferedSink.flush();
            bufferedSink.close();
            bufferedSource.close();
        } else {
            for (String source : sources) {
                File dir = new File(target, source);
                copyAssets(context, path + File.separator + source, dir);
            }
        }
    }
}
