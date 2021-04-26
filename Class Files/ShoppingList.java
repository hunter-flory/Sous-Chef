package com.example.testrecipeapi;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ShoppingList extends AppCompatActivity {
    public static ArrayList<String> shoppingList;
    Button addToPantryBtn, deleteItemBtn, clearListBtn, addIngredientBtn, saveListBtn ;
    ListView shopView;
    TextView ingredientTxt;
    public static final String listKey = "shopping list";
    @Override


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist);
        loadData();


        // i am simply going to load the list from shared preferences here
        /*
        // get the value of the extra bundled from dish search
        // and set shoppingList = to it
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                shoppingList = null;
            }
            else {
                shoppingList = (ArrayList<String>) savedInstanceState.getSerializable("ingredientList");
            }
        }
        */


        shopView = findViewById(R.id.shopView);
        addToPantryBtn = findViewById(R.id.addToPantryBtn);
        deleteItemBtn = findViewById(R.id.deleteItemBtn);
        clearListBtn =  findViewById(R.id.clearListBtn);
        addIngredientBtn = findViewById(R.id.addIngredientBtn);
        saveListBtn = findViewById(R.id.saveListBtn);
        ingredientTxt = findViewById(R.id.ingredientTxt);




        // load data removes need to create class variable
        /*
        if(shoppingList.isEmpty()){
            shoppingList.add("Shopping list is empty");
        }
        // Create local shoppingList array and pass ingredientList
        if(!DishSearch.ingredientList.isEmpty()) {
            if(shoppingList.contains("Shopping list is empty"))
                shoppingList.remove(0);

           shoppingList.addAll((ArrayList<String>) getIntent().getSerializableExtra("ingredientList"));
        }
         */




        // Set shopView to display shoppingList items in selectable ListView
        // making this shopView(List View) selectable because I want user to be able
        // to select items and delete them once purchased or if they already have
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, shoppingList);
        shopView.setAdapter(arrayAdapter);

        //remove item from shopping list
        shopView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                deleteItemBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shoppingList.remove(i);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, shoppingList);
                        shopView.setAdapter(arrayAdapter);
                    }
                });
            }
        });

        addToPantryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingList.this, Pantry.class);

                // again, here i'm just going to save the list to the pantry key in shared preferences and load in pantry
                //intent.putExtra("shoppingList", shoppingList);

                addItems(Pantry.pantryKey, shoppingList);
                saveData(Pantry.pantryKey);
                startActivity(intent);
            }
        });

        clearListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoppingList = new ArrayList<>();
                //shoppingList.add("List is empty.");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, shoppingList);
                shopView.setAdapter(arrayAdapter);
                // overwrite on clear
                saveData(listKey);
            }
        });

        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shoppingList.contains("List is empty"))
                    shoppingList.remove(0);

                shoppingList.add(ingredientTxt.getText().toString());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, shoppingList);
                shopView.setAdapter(arrayAdapter);
            }
        });

        saveListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add whats now in shopping list at end of new shopping list and save
                // into listKey key address in shared preferences
                addItems(listKey, shoppingList);
                saveData(listKey);
            }
        });
    }

    private void saveData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(shoppingList);
        editor.putString(key, json);
        editor.apply();
    }


    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(listKey, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        shoppingList = gson.fromJson(json, type);

        if (shoppingList == null) {
            shoppingList = new ArrayList<>();

        }
    }

    // add items to end of list
    private void addItems(String key, ArrayList<String> newItems) {
        //original shopping list = what's in shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
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
        shoppingList = firstList;

    }

}
