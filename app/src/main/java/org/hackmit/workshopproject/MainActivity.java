package org.hackmit.workshopproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView myText1 = findViewById(R.id.myText1);
        TextView myText2 = findViewById(R.id.myText2);
        ImageView imageView1 = findViewById(R.id.myImage1);

        Gson gson = new Gson();
        try {
            InputStreamReader reader = new InputStreamReader(getAssets().open("Datapack/data/0001.json"));
            JsonObject data = gson.fromJson(reader, JsonObject.class);
            reader.close();

            String name = data.get("Name").getAsString();
            String year = data.get("Year").getAsString();

            myText1.setText(name + " " + year);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Picasso.get().load("file:///android_asset/Datapack/images/0001.jpg").fit().into(imageView1);

        final int[] clicks = {0};

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicks[0]++;
                myText1.setText("I have clicked on the button " + clicks[0] + " times!");

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        Switch switch1 = findViewById(R.id.switch1);
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch1.isChecked()) {
                    myText2.setText("The switch is on!");
                }
                else {
                    myText2.setText("The switch is off :(((");
                }
            }
        });
        
        EditText editText = findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = editText.getText().toString();
                myText1.setText("Hello, " + name + "!");
            }
        });
    }
}