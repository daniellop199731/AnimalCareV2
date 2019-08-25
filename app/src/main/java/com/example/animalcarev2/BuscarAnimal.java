package com.example.animalcarev2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;

public class BuscarAnimal extends AppCompatActivity {
    //Vistas o componentes que se usaran
    EditText txtresultado, txtcodigoAnimal;
    Button btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_animal);
        iniViwes();
    }
    /**
     *  Metodo que se usa para iniciar las vistas o componentes, en el metodo OnCreate
     */
    public void iniViwes(){
        txtcodigoAnimal = (EditText) findViewById(R.id.txtcodigoAnimal);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        txtresultado = (EditText) findViewById(R.id.txtresultado);

    }
}
