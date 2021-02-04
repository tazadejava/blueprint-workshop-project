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

        // declaring a RecyclerView and initializing its Adapter and LayoutManager
        RecyclerView foodList = findViewById(R.id.foodList);
        FoodListAdapter adapter = new FoodListAdapter(this);
        foodList.setLayoutManager(new LinearLayoutManager(this));
        foodList.setAdapter(adapter);

        // declaring the randomize Button and its action
        Button randomizeButton = findViewById(R.id.button);
        randomizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.randomizeItems();
            }
        });

        // declaring the search EditText and result count TextView, and the search action
        EditText search = findViewById(R.id.search);
        TextView searchResultsCount = findViewById(R.id.searchResultsCount);
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

        // declaring the sort by Spinner and its Adapter
        Spinner sortBySpinner = findViewById(R.id.sortBySpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(spinnerAdapter);

        // populating the spinnerAdapter
        List<String> categories = adapter.getCategories();
        Collections.sort(categories);
        spinnerAdapter.add("All");
        spinnerAdapter.addAll(categories);

        // setting the action of the Spinner
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
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