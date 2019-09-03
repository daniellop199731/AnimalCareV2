package com.example.animalcarev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BuscarAnimal extends AppCompatActivity {
    //Vistas o componentes que se usaran
    EditText txtcodigoAnimal;
    Button btnBuscar;
    ListView txtresultado;

    private final String C_ANIMALES = "Animales";

    //Instansia de la base de datos (Contiene todos los datos)
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //Referencia a la colecion Animal
    private DatabaseReference animales;

    //Referencia de la raiz de la base de datos de firebase
    private DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();

    //Variable que almacena el conigo autogenerado
    private String codigo;

    //Lista para almacenar los tipos de animales
   // private ArrayList<Animal> animal = new ArrayList<Animal>();
    //ArrayAdapter<Animal> adapter;

    //Tamaño del arbol de Animales
   // long sizeAnimales;
    private DatabaseReference refAnimales = database.getReference(C_ANIMALES);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_animal);
        iniViwes();
        obtenerAnimales();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigo = txtcodigoAnimal.getText().toString();
                buscar(codigo);
            }
        });
    }


    /**
     * Metodo que llena la lista de tipos de animal, con los tipos de animal que hay en firebase
     */
    private void obtenerAnimales() {
       // final ArrayList<String> lista = new ArrayList<>();
        //Para obtener datos de la base de datos
        refAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long size = dataSnapshot.getChildrenCount();
                ArrayList<String> animalesFirebase = new ArrayList<>();
                for (int i = 0; i < size; i++) {

                    String code = dataSnapshot.child("" + i).child("codigo").getValue(String.class);
                    String nombre = dataSnapshot.child("" + i).child("nombre").getValue(String.class);
                    String tipo = dataSnapshot.child("" + i).child("tipo").getValue(String.class);
                    String raza = dataSnapshot.child("" + i).child("raza").getValue(String.class);
                    String animal = code + " | " + nombre + " | " + tipo + " | " + raza;

                    animalesFirebase.add(animal);
                }

                adaptar(animalesFirebase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void buscar(final String filtro) {
        //Para obtener datos de la base de datos
        refAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long size = dataSnapshot.getChildrenCount();
                ArrayList<String> animalesFirebase = new ArrayList<>();
                for (int i = 0; i < size; i++) {

                    String code = dataSnapshot.child("" + i).child("codigo").getValue(String.class);
                    String nombre = dataSnapshot.child("" + i).child("nombre").getValue(String.class);
                    String tipo = dataSnapshot.child("" + i).child("tipo").getValue(String.class);
                    String raza = dataSnapshot.child("" + i).child("raza").getValue(String.class);
                    String animal = code + " | " + nombre + " | " + tipo + " | " + raza;

                    if (code.equalsIgnoreCase(filtro) || nombre.equalsIgnoreCase(filtro) || tipo.equalsIgnoreCase(filtro) || raza.equalsIgnoreCase(filtro)) {
                        animalesFirebase.add(animal);
                    }
                }
                if (animalesFirebase.size() == 0) {
                    animalesFirebase.add("No se encuentran animales con ese código");
                }
                adaptar(animalesFirebase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void adaptar(ArrayList<String> lista) {
        try {

            ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, lista);
            txtresultado.setAdapter(adapter);

        } catch (Exception err) {
            Log.i("error", err.toString());
        }
    }

    /**
     * Metodo que se usa para iniciar las vistas o componentes, en el metodo OnCreate
     */
    public void iniViwes() {
        txtcodigoAnimal = (EditText) findViewById(R.id.txtcodigoAnimal);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        txtresultado = (ListView) findViewById(R.id.txtresultado);

    }
}
