package com.example.animalcarev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaCrias extends AppCompatActivity {

    ////////////////////DECLARACION DE VISTAS
    private ListView lvListarCrias;

    ///////////////////DECLARACION DE CONSTANTES
    private final String C_CRIAS = "CriasV2";
    private final String C_ANIMALES = "Animales";

    ////////////////////DECLARACION DE VARIABLES OPERATIVAS
    //Array para almacenar los codigos de las crias
    private ArrayList<String>listCodCrias = new ArrayList<>();

    //Array para almacenar y mostrar la informacion de las crias
    private ArrayList<String>listInfoCrias = new ArrayList<>();

    //Codigo del animal que tiene las crias a listar
    private String codigoAnimal;

    //Instansia de la base de datos (Contiene todos los datos)
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //Declarion de referncia a la coleccion de crias
    private DatabaseReference refCrias = database.getReference(C_CRIAS);
    //Declaracion de referencia a la coleccion de animales
    private DatabaseReference refAnimales = database.getReference(C_ANIMALES);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_crias);
        iniViews();
        obtenerRef();
        Log.i("Lista Crias !!!", "");

        //Al obtener el codigo capturado, se procede a llenar la lista
        refCrias.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Tamaño del arbol de listaCrias del animal
                long size = dataSnapshot.child(codigoAnimal).child("listaCrias").getChildrenCount();

                //Se recorre el arbol de lista crias del animal para obtener los codigos
                for (int i = 0; i < size; i++){
                    //Se añade al Array el codigo de la cria
                    listCodCrias.add(separarCodigo(dataSnapshot.child(codigoAnimal).child("listaCrias").child(String.valueOf(i)).child("codigo")
                    .getValue(String.class)));
                }

                //Se configura el listView para mostrar los codigos (PRUEBAS)
                //lvListarCrias.setAdapter(crearAdapter(listCodCrias));

                refAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Objeto vacio que ayudara al almacenamiento de la informacion
                        Animal animal;

                        //Variable que ayuda a estructuras la informacion
                        String info = "";

                        //Se recorre la lista de los codigos de as crias
                        for (String cons: listCodCrias) {
                            //Se obtiene el objeto completo del animal
                            //animal = dataSnapshot.child(cons).getValue(Animal.class);

                            /*info =  "\nNombre:\n" + dataSnapshot.child(cons).child("nombre").getValue(String.class) + "\n\n" +
                                    "Codigo:\n" + dataSnapshot.child(cons).child("codigo").getValue(String.class) + "\n\n" +
                                    "Tipo:\n" + dataSnapshot.child(cons).child("tipo").getValue(String.class) + "\n\n" +
                                    "Raza:\n" + dataSnapshot.child(cons).child("raza").getValue(String.class) + "\n\n" +
                                    "Sexo:\n" + dataSnapshot.child(cons).child("sexo").getValue(String.class) + "\n\n" +
                                    "F. nacimiento:\n" + dataSnapshot.child(cons).child("fecha_nacimiento").getValue(String.class) + "\n";*/
                            info =  "Nombre  :" + dataSnapshot.child(cons).child("nombre").getValue(String.class) + "\n" +
                                    "Codigo    :" + dataSnapshot.child(cons).child("codigo").getValue(String.class) + "\n" +
                                    "Tipo         :" + dataSnapshot.child(cons).child("tipo").getValue(String.class) + "\n" +
                                    "Raza        :" + dataSnapshot.child(cons).child("raza").getValue(String.class) + "\n" +
                                    "Sexo        :" + dataSnapshot.child(cons).child("sexo").getValue(String.class) + "\n" +
                                    "F. nacimiento: " + dataSnapshot.child(cons).child("fecha_nacimiento").getValue(String.class) + "\n";
                            listInfoCrias.add(info);
                            info = "";
                        }

                        //Se configura el listView para mostrar los codigos (PRUEBAS)
                        lvListarCrias.setAdapter(crearAdapter(listInfoCrias));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Error al obtener info de FireBase", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error al obtener info de FireBase", Toast.LENGTH_SHORT).show();
            }
        });
        //En este punto ya se deben obtener todos los codigos de las crias



    }

    /**
     * Metodo que obtiene el consecutivo de un codigo completo de un animal
     * @param codigoC Codigo completo del animal
     * @return El consecutivo (numeros al final del codigo)
     */
    private String separarCodigo(String codigoC){
        //Tamaño de la cadena (codigoC)
        int size = codigoC.length();
        //Consecutivo que se va a construir
        String consecutivo = "";
        //Caracter que ayuda a la construccion del consecutivo
        char digito;
        //Se recorre unicamente desde el primer numero hacia la derecha del codigoC
        for(int i = 2; i < size; i++){
            digito = codigoC.charAt(i);
            consecutivo += digito;
        }
        return consecutivo;
    }

    /**
     * Metodo que crea un adaptador para los diferentes tipos de lista (en la vista)
     * @param list ArrayList que se desea mostrar en la vista
     * @return el adaptador del ArrayList
     */
    private ArrayAdapter crearAdapter(ArrayList<String>list){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        return adapter;
    }

    /**
     * Metodo que obtiene las referencias que se envian
     */
    private void obtenerRef(){
        Bundle bundle = this.getIntent().getExtras();
        codigoAnimal = bundle.getString("codigoAnimal");
    }

    private void iniViews(){
        lvListarCrias = findViewById(R.id.lvListaCrias);
    }
}
