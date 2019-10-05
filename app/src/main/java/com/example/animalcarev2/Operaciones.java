package com.example.animalcarev2;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Operaciones {

    //Constantes
    private final String SEXO_HEMBRA = "Hembra";
    private final String SEXO_MACHO = "Macho";

    private final String C_ANIMALES = "Animales";
    private final String C_TIPOSANIMALES = "TiposAnimales";

    //Instansia de la base de datos (Contiene todos los datos)
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //Referencia a toda la pase de datos
    //NOTA: si el getReference(), no tiene parametro(Un string), hara referencia al arbol completo
    //      Si tiene como paramentro ejemplo "Animales", hara referencia al arbol de animales
    /*
        En este caso, se hara referencia, a la coleccion TiposAnimales
     */
    private DatabaseReference refTiposAnimales = database.getReference(C_TIPOSANIMALES);
    private DatabaseReference refAnimales = database.getReference(C_ANIMALES);

    //Referencia a la colecion Animal
    private DatabaseReference animales;

    //Referencia de la raiz de la base de datos de firebase
    private DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();

    //Variable que almacena el conigo autogenerado
    private String codigo;

    //Lista para almacenar los tipos de animales
    private ArrayList<String> tiposAnimales = new ArrayList<String>();

    //Tamaño del arbol de Animales
    long sizeAnimales;

    /**
     * Accion que determina el tamaño del arbol de animales (Canidad de animales registrados)
     */
    private void ref() {
        refAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sizeAnimales = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void registrarAnimalV2(String nombre, String codigo, String tipo, String raza, String sexo, String feNacimiento){
        ref();
        Animal nuevoAnimal = new Animal(nombre, codigo, tipo, raza, sexo, feNacimiento);
        /**
         * A continuacion, semuestra como se añade un registro con sus respectivos hijos
         */
        refAnimales.child(String.valueOf(sizeAnimales)).setValue(nuevoAnimal);
    }

}
