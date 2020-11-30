package com.example.testrecipeapi;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Planner extends AppCompatActivity {

    EditText receipe;
    Button add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        receipe=findViewById(R.id.receipe);
        add_btn=findViewById(R.id.add_btn);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!receipe.getText().toString().isEmpty()){
                    Intent intent=new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);

                    intent.putExtra(CalendarContract.Events.TITLE ,receipe.getText().toString());
                    intent.putExtra(CalendarContract.Events.ALL_DAY,"false");

                    if(intent.resolveActivity(getPackageManager())==null) {
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(Planner.this,"This app cannot support this action",Toast.LENGTH_SHORT ).show();
                    }

                }
                else{
                    Toast.makeText(Planner.this, "Please write the receipe name",Toast.LENGTH_SHORT).show();
                }


            }
        });







    }
}
