package com.wanke.network.http;

public class Constants {

    public static final String BASE_HOST = "http://192.168.41.101:9257";

    public static String buildImageUrl(String imageId) {
        return BASE_HOST + "/wanketv/static/images/cover/" + imageId;
    }
}
