package org.hackmit.workshopproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //define our textviews and imageviews

        TextView myText1 = findViewById(R.id.myText1);
        TextView myText2 = findViewById(R.id.myText2);
        TextView myCustomText = findViewById(R.id.myCustomText);

        ImageView myImage1 = findViewById(R.id.myImage1);
        ImageView myImage2 = findViewById(R.id.myImage2);
        ImageView myImage3 = findViewById(R.id.myImage3);

        setJournalEntry(myText1, myImage1, "4343");
        setJournalEntry(myText2, myImage2, "5499");

        setJournalEntry(myCustomText, myImage3, "7580");
        //overwrite with text
        myCustomText.setText("Empty journal entry");

        EditText myCustomEditText = findViewById(R.id.myCustomEditText);
        Button confirmEditTextButton = findViewById(R.id.confirmEditTextButton);
        confirmEditTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCustomText.setText(myCustomEditText.getText().toString());

                Toast.makeText(MainActivity.this, "Wrote a new journal entry!", Toast.LENGTH_SHORT).show();
            }
        });

        myCustomEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                myText2.setText(myCustomEditText.getText().toString());
            }
        });

        Button randomizeEntriesButton = findViewById(R.id.randomizeEntriesButton);
        randomizeEntriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setJournalEntry(myText1, myImage1, ids.get((int) (Math.random() * ids.size())));
                setJournalEntry(myText2, myImage2, ids.get((int) (Math.random() * ids.size())));
                setJournalEntry(myCustomText, myImage3, ids.get((int) (Math.random() * ids.size())));
                myCustomText.setText("Empty journal entry");
            }
        });

        //load ids
        try {
            ids = new ArrayList<>();
            String[] fileNames = getAssets().list("Datapack/data/");

            for(String fileName : fileNames) {
                String id = fileName.substring(0, fileName.lastIndexOf("."));
                ids.add(id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setJournalEntry(TextView textView, ImageView imageView, String id) {
        try {
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(getAssets().open("Datapack/data/" + id + ".json"));
            JsonObject data = gson.fromJson(reader, JsonObject.class);
            reader.close();

            String name = data.get("Name").getAsString();
            String year = data.has("Year") ? data.get("Year").getAsString() : "Unknown year";
            String commonName = data.has("Common name") ? data.get("Common name").getAsString() : "Unknown common name";

            textView.setText(name + ", " + year + "\n" + commonName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Picasso.get().load("file:///android_asset/Datapack/images/" + id + ".jpg").fit().centerInside().into(imageView);
    }
}