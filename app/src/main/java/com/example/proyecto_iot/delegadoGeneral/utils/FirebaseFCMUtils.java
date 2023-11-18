package com.example.proyecto_iot.delegadoGeneral.utils;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FirebaseFCMUtils {

    public static void callApi(JSONObject jsonObject) {
        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAZ6GvChg:APA91bGcMRy5DGWg6JBjNDsXSzKHFqrcZ9nE-blBNJGw9BPHXYAlHW1pdzn0n0BSf-PrhRjg_PO7qUmZPWhL4mEGuoHO-Sk7u8Ui1UZU4pIKSKplfE7wxQO6c1wiY073Jm6fzkR2Kg0q")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });

    }
}
