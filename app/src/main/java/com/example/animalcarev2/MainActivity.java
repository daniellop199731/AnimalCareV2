package com.example.animalcarev2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnRegistrar;
    Button btnBuscar;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniViews();

        /* Mensaje de prueba
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("message"); // Key
        ref.setValue("This is a test message"); // Value
        */

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), RegistroAnimal.class);
                startActivity(intent);
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), BuscarAnimal.class);
                startActivity(intent);
            }
        });
    }

    public void iniViews(){

        btnRegistrar = (Button)findViewById(R.id.btnRegistrarAnimal);
        btnBuscar = (Button)findViewById(R.id.btnBuscarAnimal);
    }
}
