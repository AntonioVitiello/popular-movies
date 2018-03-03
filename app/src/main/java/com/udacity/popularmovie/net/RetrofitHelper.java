package com.udacity.popularmovie.net;

import com.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Antonio on 20/02/2018.
 */

public class RetrofitHelper {
    public static final String SERVICE_PARAM = "service";
    private static final String LOG_TAG = "RetrofitHelper";
    private String mBaseUrl;
    private int mServiceId;

    public RetrofitHelper(String baseUrl, int serviceId) {
        mBaseUrl = baseUrl;
        mServiceId = serviceId;
    }

    public Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new AddQueryInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .build();
    }

    private class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.d(LOG_TAG, "Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers());

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.d(LOG_TAG, "Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers());

            return response;
        }
    }

    private class AddQueryInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            HttpUrl newUrl = request.url().newBuilder()
                    .addQueryParameter(SERVICE_PARAM, Integer.toString(mServiceId))
                    .build();

            Request newRequest = request.newBuilder().url(newUrl).build();
            return chain.proceed(newRequest);
        }
    }

    private class AddHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request newRequest = chain.request()
                    .newBuilder()
                    .header("Service-Identifier", Integer.toString(mServiceId))
                    .build();

            return chain.proceed(newRequest);
        }
    }

}
