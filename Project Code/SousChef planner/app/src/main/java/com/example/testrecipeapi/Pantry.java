package com.example.testrecipeapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Pantry extends AppCompatActivity {
    ArrayList<String> ingredientList = new ArrayList<>();
    ListView pantryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);
        pantryListView = findViewById(R.id.pantryListView);
        // made ingredient list a global var in DishSearch, assigning it to local
        // ingredientList var
        ingredientList = DishSearch.ingredientList;

        //putting in array adapater to then display in pantryListView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, ingredientList);
        pantryListView.setAdapter(arrayAdapter);
    }
}
