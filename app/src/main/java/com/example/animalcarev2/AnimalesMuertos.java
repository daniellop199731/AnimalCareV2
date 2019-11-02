package com.example.animalcarev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnimalesMuertos extends AppCompatActivity {


    ListView listaAnimalesMuertos;

    private final String C_ANIMALES = "Animales";
    private final String C_ANIMALES_MUERTOS = "Muertes";

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseReference refAnimales = database.getReference(C_ANIMALES);
    private DatabaseReference refAnimalesMuertos = database.getReference(C_ANIMALES_MUERTOS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animales_muertos);
        iniViwes();
        obtenerAnimales();
    }

    private void obtenerAnimales() {
        // final ArrayList<String> lista = new ArrayList<>();
        //Para obtener datos de la base de datos
        refAnimalesMuertos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final long size = dataSnapshot.getChildrenCount();
                final ArrayList<String> animalesAdaptador = new ArrayList<>();

                for (long i = 0; i < size; i++) {
                    final String code = dataSnapshot.child("" + i).child("codigo").getValue(String.class);
                    //String descripcion = dataSnapshot.child("" + i).child("descripcion").getValue(String.class);
                    final String fechaMuerte = dataSnapshot.child("" + i).child("fecha_muerte").getValue(String.class);
                    final long finalI = i;
                    refAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long sizeTodos = dataSnapshot.getChildrenCount();

                            String animal;
                            for (long j = 0; j < sizeTodos; j++) {
                                String codeTodos = dataSnapshot.child("" + j).child("codigo").getValue(String.class);
                                if (codeTodos.equals(code)) {
                                    String nombre = dataSnapshot.child("" + j).child("nombre").getValue(String.class);
                                    String tipo = dataSnapshot.child("" + j).child("tipo").getValue(String.class);
                                    String sexo = dataSnapshot.child("" + j).child("sexo").getValue(String.class);

                                    animal = code + " | " + nombre + " | " + tipo + " | " + sexo + "  |  " + fechaMuerte;
                                    animalesAdaptador.add(animal);
                                    Log.i("----------->", nombre);
                                }
                            }
                            if (finalI == (size - 1)) {
                                adaptar(animalesAdaptador);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void adaptar(ArrayList<String> lista) {
        try {

            ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, lista);
            listaAnimalesMuertos.setAdapter(adapter);

        } catch (Exception err) {
            Log.i("error", err.toString());
        }
    }

    public void iniViwes() {

        listaAnimalesMuertos = (ListView) findViewById(R.id.listaAnimalesMuertos);

    }
}
