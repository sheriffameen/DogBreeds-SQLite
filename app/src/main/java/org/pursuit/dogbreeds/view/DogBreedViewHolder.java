package org.pursuit.dogbreeds.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.pursuit.dogbreeds.DisplayActivity;
import org.pursuit.dogbreeds.R;
import org.pursuit.dogbreeds.model.DatabaseHelper;
import org.pursuit.dogbreeds.model.Dog;
import org.pursuit.dogbreeds.network.DogService;
import org.pursuit.dogbreeds.network.RetrofitSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DogBreedViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "image_call";
    private SharedPreferences sharedPreferences;
    private TextView breedTextView;
    private Intent intent;
    DatabaseHelper databaseHelper;

    public DogBreedViewHolder(@NonNull View itemView) {
        super(itemView);
        breedTextView = itemView.findViewById(R.id.breed_textview);
        sharedPreferences = itemView.getContext().getApplicationContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        databaseHelper = DatabaseHelper.getInstance(itemView.getContext().getApplicationContext());
    }

    public void onBind(final String breed) {
        breedTextView.setText(breed);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(itemView.getContext(), DisplayActivity.class);
                intent.putExtra("breed", breed);
                Log.d(TAG, String.valueOf(sharedPreferences.contains((breed + "_image"))));
                if (sharedPreferences.contains((breed + "_image"))) {
                    intent.putExtra("image", sharedPreferences.getString((breed + "_image"), null));
                    itemView.getContext().startActivity(intent);
                } else {
                    Retrofit retrofit = RetrofitSingleton.getInstance();
                    DogService dogService = retrofit.create(DogService.class);
                    Call<Dog> puppy = dogService.getDogImage(breed);

                    puppy.enqueue(new Callback<Dog>() {
                        @Override
                        public void onResponse(Call<Dog> call, Response<Dog> response) {
                            Log.d(TAG, "onResponse: " + response.body().getMessage());
                            String imageUrl = response.body().getMessage();

                            databaseHelper.addUrlToBreed(breed,imageUrl);

                            intent.putExtra("image", databaseHelper.getImageUrl(imageUrl));
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(breed+"_image", response.body().getMessage());
                            editor.commit();
                            itemView.getContext().startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Dog> call, Throwable t) {
                            Log.d(TAG, "onResponse: " + t.toString());
                        }
                    });
                }
            }
        });
    }
}
