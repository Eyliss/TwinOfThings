package com.twinofthings.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    public static String API_BASE_URL = "http://34.251.133.19:8001/v1/";
    public static String AUTH_TOKEN = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0OTA4MTA1NjcsImlhdCI6MTQ5MDgxMDI2NywibmJmIjoxNDkwODEwMjY3LCJpZGVudGl0eSI6MTIzNH0.UkhNR7KzrtahqWAxHjOzJz6fsOEwtZh49gRvl_llW8w";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
          new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

    public static Retrofit retrofit() {
        OkHttpClient client = httpClient.build();
        return builder.client(client).build();
    }

    public static <S> S createService(Class<S> serviceClass, final boolean authRequired) {
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                      .header("Content-Type", "application/json")
                      .method(original.method(), original.body());

                if(authRequired){
                    requestBuilder.header("Authorization",AUTH_TOKEN);
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}