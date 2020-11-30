package com.example.testrecipeapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {
    Button pantryBtn, dishSrchBtn, shopListBtn,plannerBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);


        pantryBtn = findViewById(R.id.pantryBtn);
        dishSrchBtn = findViewById(R.id.dishSrchBtn);
        shopListBtn = findViewById(R.id.shopListBtn);
        plannerBtn=findViewById(R.id.plannerBtn);

        dishSrchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreen.this, DishSearch.class);
                startActivity(intent);
            }
        });

        pantryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreen.this, Pantry.class);
                startActivity(intent);
            }
        });

        shopListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreen.this, ShoppingList.class);
                startActivity(intent);
            }
        });

        plannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreen.this, Planner.class);
                startActivity(intent);
            }

        });
    }
}
