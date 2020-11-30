package com.example.testrecipeapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Pantry extends AppCompatActivity {
    ListView pantryListView;
    ArrayList<String> pantryIngredientList = new ArrayList<>();
    ArrayList<String> dishList = new ArrayList<>();
    TextView ingredientSearchTxt, addIngredientTxt;
    Button ingredientSrchBtn, addIngredientBtn, deleteIngredientBtn;
    String myResponse, url;
    JSONObject object = null;
    JSONArray jArr = null;
    public static final String pantryKey = "pantry key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);
        loadData();

        ingredientSearchTxt = findViewById(R.id.ingredientSearchTxt);
        addIngredientTxt = findViewById(R.id.addIngredientTxt);
        addIngredientBtn = findViewById(R.id.addIngredientBtn);
        deleteIngredientBtn = findViewById(R.id.deleteIngredientBtn);
        ingredientSrchBtn = findViewById(R.id.ingredientSrchBtn);
        pantryListView = findViewById(R.id.pantryListView);

        /*
        if(pantryIngredientList.isEmpty()){
            pantryIngredientList.add("Pantry is empty.");
        }
        if(!ShoppingList.shoppingList.isEmpty()) {
            if(pantryIngredientList.contains("Pantry is empty."))
                pantryIngredientList.remove(0);

            pantryIngredientList.addAll((ArrayList<String>) getIntent().getSerializableExtra("shoppingList"));
        }
        */


        //putting in array adapter to then display in pantryListView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, pantryIngredientList);
        pantryListView.setAdapter(arrayAdapter);

        ingredientSrchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dishList = new ArrayList<>();

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://recipe-puppy.p.rapidapi.com/?i=" + ingredientSearchTxt.getText()) //mPlateText is the user input of dish name
                        .get()
                        .addHeader("x-rapidapi-host", "recipe-puppy.p.rapidapi.com")
                        .addHeader("x-rapidapi-key", "5b4a968887msh5c694e10c26e3bep19e01ajsn63e0b5c4c9b3")
                        .build();

                ingredientSearchTxt.setText("");

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            myResponse = response.body().string();

                            Pantry.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        object = new JSONObject(myResponse);            // convert myResponse to JsonObject to parse
                                        jArr = object.getJSONArray("results");
                                        for (int i = 0; i < 10; i++) {
                                            object = jArr.getJSONObject(i);
                                            dishList.add(object.getString("title"));
                                        }

                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, dishList);
                                        pantryListView.setAdapter(arrayAdapter);
                                        pantryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                try {
                                                    object = jArr.getJSONObject(i);
                                                    url = object.getString("href");
                                                    Uri uriUrl = Uri.parse(url);
                                                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                                    startActivity(launchBrowser);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        // This will add ingredient user typed in to pantryIngredientList
        // Then it will clear the text field for users input
        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pantryIngredientList.add(addIngredientTxt.getText().toString());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, pantryIngredientList);
                pantryListView.setAdapter(arrayAdapter);
                addIngredientTxt.setText("");
                saveData(pantryKey);
            }
        });

        //will remove selected ingredient from list
        pantryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                deleteIngredientBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pantryIngredientList.remove(i);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, pantryIngredientList);
                        pantryListView.setAdapter(arrayAdapter);
                        saveData(pantryKey);
                    }
                });
            }
        });
    };

    private void saveData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(pantryIngredientList);
        editor.putString(key, json);
        editor.apply();
    }


    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(pantryKey, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
       pantryIngredientList = gson.fromJson(json, type);

        if (pantryIngredientList == null) {
            pantryIngredientList = new ArrayList<>();
            pantryIngredientList.add("List is empty");
        }
    }

}

