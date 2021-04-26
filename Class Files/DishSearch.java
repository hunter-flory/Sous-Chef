package com.example.testrecipeapi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DishSearch extends AppCompatActivity {
    // Only way I could get data to move outside of the onResponse function
    // was to use ingredientList as global var
    public static ArrayList<String> ingredientList = new ArrayList<>();
    ArrayList<String> dishList;

    // when returning string from onResponse it is in JSON format
    // must alternate between jsonArray and JsonObject to get to data
    JSONObject object = null;
    JSONArray jArr = null;

    TextView mPlateText, dishLabel;
    Button searchBtn, recipeLink, addShop;
    ListView listView;
    String myResponse, foodLabel, ingredients, url;

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ingredientList);
        editor.putString(ShoppingList.listKey, json);
        editor.apply();
    }

    // add items to end of list
    private void addItems(ArrayList<String> newItems) {
        //original shopping list = what's in shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(ShoppingList.listKey, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> firstList = gson.fromJson(json, type);

        // stick what's in current shopping list into end of original
        // skipping duplicates
        for (int i = 0; i<newItems.size(); i++) {
            if (firstList.contains(newItems.get(i)))
                continue;
            else
                firstList.add(newItems.get(i));

        }

        // shopping list = to first list (now updated)
        ingredientList = firstList;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishsearch);
        listView = findViewById(R.id.listView);
        searchBtn = findViewById(R.id.searchButton);
        recipeLink = findViewById(R.id.recipeLink);
        addShop = findViewById(R.id.addShop);
        dishLabel = findViewById(R.id.dishLabel);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Initialize ingredientList and dishList to new ArrayList every time user
                // presses search button to clear previous lists searched to clear previous
                // search ingredients
                ingredientList = new ArrayList<>();
                dishList = new ArrayList<>();
                mPlateText = findViewById(R.id.plateToSearch);
                foodLabel = ""; // set food label to blank
                dishLabel.setText(foodLabel);
                url = "";
                // Copied code from RapidAPI to retrieved string file in JSON format
                // and will be stored in myResponse

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://recipe-puppy.p.rapidapi.com/?q=" + mPlateText.getText()) //mPlateText is the user input of dish name
                        .get()
                        .addHeader("x-rapidapi-host", "recipe-puppy.p.rapidapi.com")
                        .addHeader("x-rapidapi-key", "5b4a968887msh5c694e10c26e3bep19e01ajsn63e0b5c4c9b3")
                        .build();

                mPlateText.setText("");

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            myResponse = response.body().string();

                            // so onResponse runs on a different thread so any of these variables
                            // will return null is used outside of this space
                            // by calling runOnUIThread we force the data to be ran
                            // in main thread and thus we can see the results on the screen
                            DishSearch.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        object = new JSONObject(myResponse);            // convert myResponse to JsonObject to parse
                                        jArr = object.getJSONArray("results");
                                        for (int i = 0; i < 10; i ++){
                                            object = jArr.getJSONObject(i);
                                            dishList.add(object.getString("title"));
                                        }
                                        foodLabel = "Results";
                                        dishLabel.setText(foodLabel);

                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, dishList);
                                        listView.setAdapter(arrayAdapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                try {
                                                    object = jArr.getJSONObject(i);
                                                    url = object.getString("href");
                                                    foodLabel = object.getString("title");
                                                    dishLabel.setText(foodLabel);
                                                    ingredients = object.getString("ingredients");
                                                    ingredientList = new ArrayList<String>(Arrays.asList(ingredients.split(",")));
                                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, ingredientList);
                                                    listView.setAdapter(arrayAdapter);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    // instead of sending the list as an extra intent (causes errors with null referencing in shopping list)
                                    // I am saving ingredientList to shared preferences, passing intent to shopping list,
                                    // and loading the list from shared preferences from within shopping list.
                                    // this will make it to where the list is automatically saved once the user clicks
                                    // "add to shopping list"

                                    addShop.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(DishSearch.this, ShoppingList.class);
                                            addItems(ingredientList);
                                            saveData();
                                            //intent.putExtra("ingredientList", ingredientList);
                                            startActivity(intent);
                                        }
                                    });

                                    recipeLink.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Uri uriUrl = Uri.parse(url);
                                            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                            startActivity(launchBrowser);
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }

        });

    }
}