package org.hackmit.workshopproject;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class FoodItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);

        // getting the id, which is passed from when we started this activity
        String id = getIntent().getStringExtra("id");
        //same deal for the description
        String desc = getIntent().getStringExtra("desc");

        ImageView image = findViewById(R.id.image);
        TextView description = findViewById(R.id.description);

        Picasso.get().load("file:///android_asset/Datapack/images/" + id + ".jpg").fit().centerInside().into(image);
        description.setText(desc);
    }
}