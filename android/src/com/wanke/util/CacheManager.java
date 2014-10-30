package com.wanke.util;

import java.io.File;
import java.util.Random;

import com.wanke.WankeTVApplication;

public class CacheManager {

    private static CacheManager mInstance = new CacheManager();

    private CacheManager() {

    }

    public static CacheManager getInstance() {
        return mInstance;
    }

    /**
     * �ڻ����д����ļ�
     * 
     * @param filename
     * @return
     */
    public File mkCacheFile(String filename) {
        File file = null;

        filename = makeRandomName(4);

        filename = "gezi_" + filename + "_" + System.currentTimeMillis();
        file = new File(WankeTVApplication.getApplication()
                .getExternalCacheDir(),
                filename);

        return file;
    }

    private String makeRandomName(int number) {
        StringBuilder name = new StringBuilder("zxs");
        Random random = new Random(System.currentTimeMillis());

        for (int i = 0; i < number; i++) {
            name.append((char) ('a' + random.nextInt(26)));
        }

        return name.toString();
    }
}
