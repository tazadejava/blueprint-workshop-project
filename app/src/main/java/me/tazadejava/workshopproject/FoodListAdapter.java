package me.tazadejava.workshopproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ConstraintLayout layout;
        public ImageView image;
        public TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.foodImage);
            name = itemView.findViewById(R.id.name);
        }
    }

    private Context context;
    private List<String> allIds, selectedIds;

    private Set<String> categories = new HashSet<>();

    private String lastFilterText = "";
    private String lastKeyText = "";

    private HashMap<String, LinkedHashMap<String, String>> itemData = new HashMap<>();
    private HashMap<String, String> itemSearchData = new HashMap<>();

    public FoodListAdapter(Context context) {
        this.context = context;

        //load the database into memory
        try {
            AssetManager assetManager = context.getAssets();
            Gson gson = new Gson();
            String[] fileNamesJson = assetManager.list("Datapack/data/");

            allIds = new ArrayList<>();
            for (String filename : fileNamesJson) {
                String id = filename.substring(0, filename.lastIndexOf("."));
                allIds.add(id);

                InputStreamReader reader = new InputStreamReader(assetManager.open("Datapack/data/" + filename));
                JsonObject data = gson.fromJson(reader, JsonObject.class);
                reader.close();

                LinkedHashMap<String, String> mappedData = new LinkedHashMap<>();
                StringBuilder searchQuery = new StringBuilder(); //the search query is used when searching for specific text

                for (String key : data.keySet()) {
                    categories.add(key);
                    mappedData.put(key, data.get(key).getAsString());
                    searchQuery.append(data.get(key).getAsString().toLowerCase());
                }

                itemData.put(id, mappedData);
                itemSearchData.put(id, searchQuery.toString());
            }

            selectedIds = new ArrayList<>(allIds);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String id = selectedIds.get(position);
        HashMap<String, String> mappedData = itemData.get(id);

        holder.name.setText(id);
        Picasso.get().load("file:///android_asset/Datapack/images/" + id + ".jpg").fit().centerInside().into(holder.image);

        SpannableStringBuilder dataString = new SpannableStringBuilder();
        for(String key : mappedData.keySet()) {
            int length = dataString.length();
            dataString.append(key).append(": ").append("\n");
            dataString.setSpan(new StyleSpan(Typeface.BOLD), length, length + key.length() + 2, 0);
            dataString.append("\t\t").append(mappedData.get(key)).append("\n");
        }

        holder.name.setText(dataString);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showFoodItemIntent = new Intent(context, FoodItemActivity.class);
                showFoodItemIntent.putExtra("id", id);
                context.startActivity(showFoodItemIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedIds.size();
    }

    public List<String> getCategories() {
        return new ArrayList<>(categories);
    }

    public void randomizeItems() {
        Collections.shuffle(selectedIds);
        notifyDataSetChanged();
    }

    public void filterItemsByText(@NonNull String filterText) {
        filterItems(filterText, lastKeyText);
    }

    public void sortItemsByKey(@NonNull String keyText) {
        filterItems(lastFilterText, keyText);
    }

    /**
     * Sorts first by key, then filters by the text
     * @param filterText
     * @param key
     */
    private void filterItems(@NonNull String filterText, @NonNull String key) {
        if(!key.isEmpty()) {
            this.lastKeyText = key;
            //if the key exists, then sort by that; if it doesn't exist, default to being last
            Collections.sort(allIds, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    String val1 = itemData.get(o1).containsKey(key) ? itemData.get(o1).get(key) : "zzzzzz";
                    String val2 = itemData.get(o2).containsKey(key) ? itemData.get(o2).get(key) : "zzzzzz";

                    return val1.compareTo(val2);
                }
            });
        }

        //no filter text means don't filter at all
        if(filterText.isEmpty()) {
            selectedIds = allIds;
            lastFilterText = "";
            notifyDataSetChanged();
            return;
        }

        //otherwise, filter the ids
        filterText = filterText.toLowerCase();
        lastFilterText = filterText;

        List<String> result = new ArrayList<>();
        HashMap<String, Integer> idIndexOf = new HashMap<>();
        int index;
        for(String id : allIds) {
            if((index = itemSearchData.get(id).indexOf(filterText)) != -1) {
                result.add(id);
                idIndexOf.put(id, index);
            }
        }

        //don't sort if we are already sorting by key
        if(key == null) {
            Collections.sort(result, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return Integer.compare(idIndexOf.get(o1), idIndexOf.get(o2));
                }
            });
        }

        selectedIds = result;
        notifyDataSetChanged();
    }
}
