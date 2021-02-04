package me.tazadejava.workshopproject;

import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStreamReader;

public class FoodItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);

        // getting the id, which is passed from when we started this activity
        String id = getIntent().getStringExtra("id");

        ImageView image = findViewById(R.id.image);
        TextView description = findViewById(R.id.description);

        Picasso.get().load("file:///android_asset/Datapack/images/" + id + ".jpg").fit().centerInside().into(image);

        try {
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(getAssets().open("Datapack/data/" + id + ".json"));
            JsonObject data = gson.fromJson(reader, JsonObject.class);
            reader.close();

            StringBuilder dataString = new StringBuilder();

            //alternative way to format text, using HTML and Html.fromHtml
            //note that this is slower and less recommended. use spannable when possible instead.
            for(String key : data.keySet()) {
                dataString.append("<b>").append(key).append(": </b><br></br>").append(data.get(key).getAsString()).append("<br></br>");
            }

            description.setText(Html.fromHtml(dataString.toString()));
            //allow the textview to scroll, along with the "scrollbars" setting set to vertical in the TextView
            description.setMovementMethod(new ScrollingMovementMethod());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}