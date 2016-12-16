package com.kuhar.kos.tvspored;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class API {
    public static String GET(OkHttpClient client, String url) throws IOException {
        String result = "";
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        return res;
    }
}
