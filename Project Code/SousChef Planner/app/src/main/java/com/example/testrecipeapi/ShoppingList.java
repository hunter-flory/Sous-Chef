package com.example.testrecipeapi;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShoppingList extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist);

        // Create local shoppingList array and pass ingredientList
        ArrayList<String> shoppingList = DishSearch.ingredientList;
        ListView shopView = findViewById(R.id.shopView);

        if(shoppingList.isEmpty()){
            shoppingList.add("Shopping List is Empty!");
        }
        



        // Set shopView to display shoppingList items in selectable Listview
        // making this shopView(List View) selectable because I want user to be able
        // to select items and delete them once purchased or if they already have
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, shoppingList);
        shopView.setAdapter(arrayAdapter);
        shopView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}
