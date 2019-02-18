package org.pursuit.dogbreeds;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.pursuit.dogbreeds.controller.DogBreedAdapter;
import org.pursuit.dogbreeds.model.Breeds;
import org.pursuit.dogbreeds.model.DatabaseHelper;
import org.pursuit.dogbreeds.network.DogService;
import org.pursuit.dogbreeds.network.RetrofitSingleton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "breed_all";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.breed_recyclerview);
        Retrofit retrofit = RetrofitSingleton.getInstance();
        DogService dogService = retrofit.create(DogService.class);
        Call<Breeds> puppy = dogService.getDogBreeds();
        puppy.enqueue(new Callback<Breeds>() {
            @Override
            public void onResponse(Call<Breeds> call, Response<Breeds> response) {
                try {
                    final DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());

                    Log.d(TAG, "onResponse: " + response.body().getMessage());
                    databaseHelper.addDogListToDatabase(response.body().getMessage());
                    List<String> newDogList = databaseHelper.getBreedList();
                    recyclerView.setAdapter(new DogBreedAdapter(newDogList));
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Breeds> call, Throwable t) {
                Log.d(TAG, "onResponse: " + t.toString());
            }
        });

    }
}
