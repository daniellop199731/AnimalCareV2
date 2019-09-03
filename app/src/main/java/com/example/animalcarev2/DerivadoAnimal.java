package com.example.animalcarev2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class DerivadoAnimal extends AppCompatActivity {

   Spinner spntipoAnimal;
   Button btnRegistrar;
   EditText txtNombre, txtUnidad;

   private final String C_TIPOSANIMALES = "TiposAnimales";


    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseReference refTiposAnimales = database.getReference(C_TIPOSANIMALES);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_derivado_animal);
        iniViwes();
        obtenerTiposAnimales();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nombre = txtNombre.getText().toString();
                final String unidad = txtUnidad.getText().toString();
                final int posicion = spntipoAnimal.getSelectedItemPosition();
                refTiposAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long valorNuevo = dataSnapshot.child(String.valueOf(posicion)).child("derivadosAnimal").getChildrenCount();
                        boolean existe = false;

                        for (int i = 0; i< valorNuevo; i++){
                            String nombreFirebase = dataSnapshot.child(String.valueOf(posicion)).child("derivadosAnimal").child(String.valueOf(i)).child("nombre").getValue(String.class);
                            if(nombreFirebase.equalsIgnoreCase(nombre)){
                                existe = true;
                            }
                        }
                        if(existe){
                            Toast.makeText(getApplicationContext(), "Derivado ya existe", Toast.LENGTH_SHORT).show();
                        }else {
                            Derivado nuevoDerivado = new Derivado(nombre, unidad);
                            refTiposAnimales.child(String.valueOf(posicion)).child("derivadosAnimal").child(String.valueOf(valorNuevo)).setValue(nuevoDerivado);
                            Toast.makeText(getApplicationContext(), "Derivado registrado", Toast.LENGTH_SHORT).show();
                            txtNombre.setText("");
                            txtUnidad.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }


    private ArrayAdapter crearAdapter(ArrayList<String>list){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        return adapter;
    }

    private void obtenerTiposAnimales() {
        final ArrayList<String> listaAnimales = new ArrayList<>();

        //Para obtener datos de la base de datos
        refTiposAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //AQUI: lo que pasa cuando se cambian los datos
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Sse obtiene el tamaño del arbol de la referencia
                long size = dataSnapshot.getChildrenCount();

                //Se crea variable nombre para ir agregando los datos que se obtienen de ahí y luego agregarlos a la lista.
                String nombre;


                //Se recorre el arbol
                for (int i = 0; i < size; i++){
                    //se obtienen los nombres por cada iteración
                    nombre = dataSnapshot.child(String.valueOf(i)).child("nombre").getValue(String.class);

                    //Se añaden esos nombres a la lista
                    listaAnimales.add(nombre);

                }
                spntipoAnimal.setAdapter(crearAdapter(listaAnimales));
            }

            @Override
            //AQUI: cuando hay algun error (Cancelacion de la referencia)
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void iniViwes(){

        spntipoAnimal = (Spinner) findViewById(R.id.spntipoAnimal);
        btnRegistrar = (Button) findViewById(R.id.btnalmacenarTipoDerivado);
        txtNombre = (EditText) findViewById(R.id.txtnombreDerivado);
        txtUnidad = (EditText) findViewById(R.id.txtunidadDerivado);
    }
}
