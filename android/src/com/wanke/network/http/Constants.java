package com.wanke.network.http;

public class Constants {

    public static final String BASE_HOST = "http://54.64.105.44";//"http://192.168.41.101:9257";

    public static String buildImageUrl(String imageId) {
        return BASE_HOST + "/wanketv/static/images/cover/" + imageId;
    }

    public static final String BASE_SDCARD = "file://";

    public static String SDImageUrl(String imageId) {
        return BASE_SDCARD + imageId;
    }
}
