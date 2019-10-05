package com.example.animalcarev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegistrarParto extends AppCompatActivity {
    //Vistas o componentes que se usaran
    EditText txtNombreCria, txtCodigoCria, txtCodigoPadre, txtCodigoMadre, txtTipoAnimal, txtRazaAnimal;
    Spinner spTipoAnimal, spRazaAnimal;
    RadioButton rbSexoHembra, rbSexoMacho;
    Button btnAlmacenarCria;
    DatePicker cvFechaNacimiento;
    TextView tvTipoCria, tvRazaCria;
    //Constantes
    private final String SEXO_HEMBRA = "Hembra";
    private final String SEXO_MACHO = "Macho";

    private final String C_ANIMALES = "Animales";
    private final String C_TIPOSANIMALES = "TiposAnimales";
    private final String C_CRIAS = "CRIAS";


    //Instansia de la base de datos (Contiene todos los datos)
    private FirebaseDatabase database = FirebaseDatabase.getInstance();


    private DatabaseReference refTiposAnimales = database.getReference(C_TIPOSANIMALES);
    private DatabaseReference refAnimales = database.getReference(C_ANIMALES);
    private DatabaseReference refCrias = database.getReference(C_CRIAS);

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_parto);
        iniViwes();
        obtenerTiposAnimal();

        refCrias.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sizeAnimales = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /**
         * Accion del boton de "Registrar animal"
         */
        btnAlmacenarCria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sizeAnimales = dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                registrarCria();
            }
        });

        /**
         * Accion al selecionar un tipo de animal
         */
        spTipoAnimal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                obtenerRazas(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spRazaAnimal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                char part1 = spTipoAnimal.getSelectedItem().toString().charAt(0);
                char part2 = spRazaAnimal.getSelectedItem().toString().charAt(0);
                long part3 = sizeAnimales;
                codigo = part1+""+part2+""+part3;
                txtCodigoCria.setText(codigo);
                Log.i("---->", codigo);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void registrarCria(){
        String nombre = txtNombreCria.getText().toString();
        String tipo = spTipoAnimal.getSelectedItem().toString();
        String raza;
        String codigoP = txtCodigoPadre.getText().toString();
        String codigoM = txtCodigoMadre.getText().toString();
        if(spRazaAnimal.getSelectedItem() != null){
            raza = spRazaAnimal.getSelectedItem().toString();
        }else{
            raza = "N/A";
        }
        String sexo;

        if (rbSexoHembra.isChecked()){
            sexo = SEXO_HEMBRA;
        } else {
            sexo = SEXO_MACHO;
        }
        String fecha_nacimiento = cvFechaNacimiento.getYear() + "/" + cvFechaNacimiento.getMonth() + "/" + cvFechaNacimiento.getDayOfMonth();
        if(nombre.isEmpty()){
            Toast.makeText(this, "Falta el nombre", Toast.LENGTH_SHORT).show();
        }else{
            if (tipo.isEmpty()){
                Toast.makeText(this, "Falta el tipo", Toast.LENGTH_SHORT).show();
            }else{
                if (raza.isEmpty()){
                    Toast.makeText(this, "Falta la raza", Toast.LENGTH_SHORT).show();
                }else{
                    if (!rbSexoHembra.isChecked() && !rbSexoMacho.isChecked()){
                        Toast.makeText(this, "Falta el sexo", Toast.LENGTH_SHORT).show();
                    }else{
                        if(codigoP.isEmpty()){
                            Toast.makeText(this, "Falta el codigo del padre", Toast.LENGTH_SHORT).show();
                        }else{
                            if(codigoM.isEmpty()){
                                Toast.makeText(this, "Falta el codigo de la madre", Toast.LENGTH_SHORT).show();
                            }else{
                                //String codigo = animal.push().getKey();
                                Cria nuevoAnimal = new Cria(nombre, codigo, codigoP, codigoM, tipo, raza, sexo, fecha_nacimiento);
                                /**
                                 * A continuacion, semuestra como se añade un registro con sus respectivos hijos
                                 */
                                refCrias.child(String.valueOf(sizeAnimales)).setValue(nuevoAnimal);

                                /**
                                 * Modelo de JSON en firebase
                                 * Animales
                                 *         "codigo"
                                 *                  <el objeto del animal
                                 */

                                Toast.makeText(this, "Animal registrado con exito", Toast.LENGTH_SHORT).show();
                                this.finish();

                            }
                        }
                    }
                }
            }
        }
    }

    private void obtenerRazas(final int index){
        //Lista donde se guardan las razas del tipo de animal seleccionado
        final ArrayList<String> lista = new ArrayList<String>();
        //Para obtener datos de la base de datos
        refTiposAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //AQUI: lo que pasa cuando se cambian los datos
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Se optiene la cantidad registrada de razas del tipo de animal seleccionado
                long size = dataSnapshot.child(String.valueOf(index)).child("razas").getChildrenCount();

                //Variable para almacenar la raza por cada iteracion
                String nombre;

                //Ciclo para recorrer el arbol de razas
                for(int i = 0; i < size; i++){
                    //Se optiene el nombre de la raza
                    nombre = dataSnapshot.child(String.valueOf(index)).child("razas")
                            .child(String.valueOf(i)).child("nombre").getValue(String.class);

                    //Se almacena en la lista
                    lista.add(nombre);
                }
                //Se configura el adaptador del Spinner en cuestion
                spRazaAnimal.setAdapter(crearAdapter(lista));
            }

            @Override
            //AQUI: cuando hay algun error (Cancelacion de la referencia
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void obtenerTiposAnimal(){
        final ArrayList<String> lista = new ArrayList<>();
        //Para obtener datos de la base de datos
        refTiposAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //AQUI: lo que pasa cuando se cambian los datos
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Se optiene el tamaño del arbol de la referencia
                long size = dataSnapshot.getChildrenCount();

                //Se crea variable nombre, para ir añadiendo a la lista
                String nombre;
                //Log.i("Tamaño", size+"");

                //Se recorre el arbol
                for (int i = 0; i < size; i++){
                    //Se optienen los nombre por cada iteracion
                    nombre = dataSnapshot.child(String.valueOf(i)).child("nombre").getValue(String.class);

                    //Se añaden a la lista
                    lista.add(nombre);

                    //Log.i(String.valueOf(i), nombre);
                }
                //Se configura la lista del Spinner en cuestion
                spTipoAnimal.setAdapter(crearAdapter(lista));
            }

            @Override
            //AQUI: cuando hay algun error (Cancelacion de la referencia)
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private ArrayAdapter crearAdapter(ArrayList<String>list){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        return adapter;
    }

    private void iniViwes(){
        txtNombreCria = (EditText) findViewById(R.id.txtNombreCria);
        txtCodigoCria = (EditText) findViewById(R.id.txtCodigoCria);
        txtCodigoPadre = (EditText) findViewById(R.id.txtCodigoPadre);
        txtCodigoMadre = (EditText) findViewById(R.id.txtCodigoMadre);
        spRazaAnimal = (Spinner) findViewById(R.id.spRazaAnimal);

        spTipoAnimal = (Spinner) findViewById(R.id.spTipoAnimal);
        rbSexoHembra = (RadioButton) findViewById(R.id.rbSexoHembra);
        rbSexoMacho = (RadioButton) findViewById(R.id.rbSexoMacho);

        cvFechaNacimiento = (DatePicker)findViewById(R.id.cvFechaNacimiento);

        btnAlmacenarCria = (Button) findViewById(R.id.btnAlmacenarCria);
    }
}


