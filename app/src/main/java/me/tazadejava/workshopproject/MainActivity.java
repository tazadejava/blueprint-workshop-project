package me.tazadejava.workshopproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView foodList = findViewById(R.id.foodList);
        foodList.setLayoutManager(new LinearLayoutManager(this));
        FoodListAdapter adapter;
        foodList.setAdapter(adapter = new FoodListAdapter(this));

        Button randomizeButton = findViewById(R.id.button);
        randomizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.randomizeItems();
            }
        });

        TextView searchResultsCount = findViewById(R.id.searchResultsCount);
        EditText search = findViewById(R.id.search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.filterItemsByText(s.toString());
                searchResultsCount.setText(adapter.getItemCount() + " result" + (adapter.getItemCount() == 1 ? "" : "s"));
            }
        });

        Spinner sortBySpinner = findViewById(R.id.sortBySpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(spinnerAdapter);

        List<String> categories = adapter.getCategories();
        Collections.sort(categories);
        spinnerAdapter.add("All");
        spinnerAdapter.addAll(categories);

        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    adapter.sortItemsByKey("");
                } else {
                    adapter.sortItemsByKey(spinnerAdapter.getItem(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}