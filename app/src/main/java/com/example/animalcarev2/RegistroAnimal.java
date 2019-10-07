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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegistroAnimal extends AppCompatActivity {

    //Vistas o componentes que se usaran
    EditText txtNombreAnimal, txtCodigoAnimal;
    Spinner spTipoAnimal, spRazaAnimal, spSexo;
    Button btnAlmacenarAnimal;
    DatePicker cvFechaNacimiento;

    //Constantes
    private final String SEXO_HEMBRA = "Hembra";
    private final String SEXO_MACHO = "Macho";

    private final String C_ANIMALES = "Animales";
    private final String C_TIPOSANIMALES = "TiposAnimales";
    private final String C_CRIAS = "CriasV2";

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
    private DatabaseReference refCrias = database.getReference(C_CRIAS);

    //Variable que almacena el conigo autogenerado
    private String codigo;

    //Tamaño del arbol de Animales
    long sizeAnimales;

    //Control para verificar si no es un reporte de Animal-Cria
    private String codigoMadre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_animal);

        //METODOS INICIALES PARA REALIZAR LAS OPERACIONES
        iniViwes();
        obtenerTiposAnimal();
        llenarListSexoAnimal();

        /**
         * Accion que determina el tamaño del arbol de animales (Canidad de animales registrados)
         */
        refAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sizeAnimales = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //codigo = refAnimales.push().getKey();

        /**
         * Accion al selecionar un tipo de animal
         */
        spTipoAnimal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                obtenerRazasAnimal(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /**
         * Accion al seleccionar una raza de un tipo de animal
         * Lo ideal es que en esta parte, los datos de TIPO DE ANIMAL y de RAZA DE ANIMAL
         * esten bien definidos para generar un codigo en base a estos datos
         */
        spRazaAnimal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                char part1 = spTipoAnimal.getSelectedItem().toString().charAt(0);
                char part2 = spRazaAnimal.getSelectedItem().toString().charAt(0);
                long part3 = sizeAnimales;
                codigo = part1+""+part2+""+part3;
                txtCodigoAnimal.setText(codigo);
                Log.i("---->", codigo);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /**
         * Accion del boton de "Registrar animal"
         */
        btnAlmacenarAnimal.setOnClickListener(new View.OnClickListener() {
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
                //registrarAnimal();
                registrarAnimalV2(  txtNombreAnimal.getText().toString(),
                                    codigo,
                                    spTipoAnimal.getSelectedItem().toString(),
                                    spRazaAnimal.getSelectedItem().toString(),
                                    spSexo.getSelectedItem().toString(),
                        cvFechaNacimiento.getYear() + "/" +
                                    cvFechaNacimiento.getMonth() + "/" +
                                    cvFechaNacimiento.getDayOfMonth()
                                );
            }
        });
    }

    /**
     * Metodo que determina si el activity, presenta funcion para reportar Animal-Cria
     * @return
     */
    private boolean esReporteCria(){
        boolean exito = false;
        Bundle bundle = this.getIntent().getExtras();
        try{
            codigoMadre = bundle.getString("codigoAnimal");
            exito = true;
            return exito;
        }catch (Exception e){
            return exito;
        }
    }

    private void registrarAnimalV2(String nombre, String codigo, String tipo, String raza, String sexo, String feNacimiento){

        if(nombre.isEmpty()){
            Toast.makeText(this, "Falta el nombre", Toast.LENGTH_SHORT).show();
        }else{
            if (tipo.isEmpty()){
                Toast.makeText(this, "Falta el tipo", Toast.LENGTH_SHORT).show();
            }else{
                if (raza.isEmpty()){
                    Toast.makeText(this, "Falta la raza", Toast.LENGTH_SHORT).show();
                }else{
                    if (sexo.isEmpty()){
                        Toast.makeText(this, "Falta el sexo", Toast.LENGTH_SHORT).show();
                    }else{

                        //String codigo = animal.push().getKey();
                        Animal nuevoAnimal = new Animal(nombre, codigo, tipo, raza, sexo, feNacimiento);
                        /**
                         * A continuacion, semuestra como se añade un registro con sus respectivos hijos
                         */
                        refAnimales.child(String.valueOf(sizeAnimales)).setValue(nuevoAnimal);

                        /**
                         * Modelo de JSON en firebase
                         * Animales
                         *         "codigo"
                         *                  <el objeto del animal
                         */

                        Toast.makeText(this, "Animal registrado con exito", Toast.LENGTH_SHORT).show();
                        if(esReporteCria()){
                            reportarCria(codigo, codigoMadre);
                            Toast.makeText(this, "Cria reportada con exito", Toast.LENGTH_SHORT).show();
                        }
                        this.finish();
                    }
                }
            }
        }
    }

    public void reportarCria(final String codigoCria, final String codigoMadre){
        refCrias.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long size = dataSnapshot.getChildrenCount();
                refCrias.child(codigoMadre).child("listaCrias").child(String.valueOf(size))
                        .child("codigo").setValue(codigoCria);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Metodo que llena la lista de tipos de animal, con los tipos de animal que hay en firebase
     */
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

    /**
     * Metodo que llena una lista con las razas del tipo de animal selecionado
     */
    private void obtenerRazasAnimal(final int index){
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

    /**
     * Metodo para llenar el Spinner con las opciones del sexo del animal
     */
    private void llenarListSexoAnimal(){
        final ArrayList<String> lista = new ArrayList<>();
        lista.add(SEXO_HEMBRA);
        lista.add(SEXO_MACHO);
        spSexo.setAdapter(crearAdapter(lista));
    }

    /**
     * Metodo que crea un adaptador para los diferentes tipos de lista (en la vista)
     * @param list ArrayList que se desea mostrar en la vista
     * @return el adaptador del ArrayList
     */
    private ArrayAdapter crearAdapter(ArrayList<String>list){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        return adapter;
    }

    /**
     *  Metodo que se usa para iniciar las vistas o componentes, en el metodo OnCreate
     */
    private void iniViwes(){
        txtNombreAnimal = (EditText) findViewById(R.id.txtNombreAnimal);
        txtCodigoAnimal = (EditText) findViewById(R.id.txtCodigoAnimal);

        spRazaAnimal = (Spinner) findViewById(R.id.spRazaAnimal);

        spTipoAnimal = (Spinner) findViewById(R.id.spTipoAnimal);
        spSexo = (Spinner)findViewById(R.id.spSexo);
        //spTipoAnimal.setAdapter(crearAdapter(tiposAnimales));

        //rbSexoHembra = (RadioButton) findViewById(R.id.rbSexoHembra);
        //rbSexoMacho = (RadioButton) findViewById(R.id.rbSexoMacho);

        cvFechaNacimiento = (DatePicker)findViewById(R.id.cvFechaNacimiento);

        btnAlmacenarAnimal = (Button) findViewById(R.id.btnAlmacenarAnimal);
    }
}
