package org.pursuit.dogbreeds;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DisplayActivity extends AppCompatActivity {
    private TextView breedTextView;
    private ImageView breedImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        breedTextView = findViewById(R.id.selected_breed_textview);
        breedImageView = findViewById(R.id.random_dog_imageview);
        Intent intent = getIntent();
        breedTextView.setText(intent.getStringExtra("breed"));
        Picasso.get().load(intent.getStringExtra("image")).into(breedImageView);
    }
}
