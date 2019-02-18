package org.pursuit.dogbreeds.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {
    private static Retrofit ourInstance;

    public static Retrofit getInstance() {
        if (ourInstance != null) {
            return ourInstance;
        }
        ourInstance = new Retrofit.Builder()
                .baseUrl("https://dog.ceo/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return ourInstance;
    }

    private RetrofitSingleton() {
    }
}
