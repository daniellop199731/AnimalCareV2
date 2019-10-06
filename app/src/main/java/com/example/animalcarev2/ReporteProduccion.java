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
import java.util.Date;

public class ReporteProduccion extends AppCompatActivity {

    ////////////////DECLARAION DE VISTAS
    private EditText txtCodigo, txtUnidadMedida, txtCantidad, txtFeProduccion;
    private Spinner spDerivados;
    private DatePicker cvFechaProduccion;
    private Button btnReportarProduccion;

    ////////////////VARIABLES OPERATIVAS Y DE CONTROL
    //Codigo y tipo del animal
    private String codigoAnimal, tipoAnimal;

    //Instansia de la base de datos (Contiene todos los datos)
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //Constante para ayudar en la definicion de la referencia(Pentiene para pasar a una clase)
    private final String C_TIPOS_ANIMALES = "TiposAnimales";
    //Constante para ayudar en la definicion de la referencia(Pentiene para pasar a una clase)
    private final String C_PRODUCCION = "Produccion";

    //Referencia a toda la pase de datos
    //NOTA: si el getReference(), no tiene parametro(Un string), hara referencia al arbol completo
    //      Si tiene como paramentro ejemplo "Animales", hara referencia al arbol de animales
    /*
        En este caso, se hara referencia, a la coleccion TiposAnimales
     */
    private DatabaseReference refTiposAnimales = database.getReference(C_TIPOS_ANIMALES);
    private DatabaseReference refProduccion = database.getReference(C_PRODUCCION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_produccion);
        Bundle bundle = this.getIntent().getExtras();
        codigoAnimal = bundle.getString("codigoAnimal");
        tipoAnimal = bundle.getString("tipoAnimal");
        Log.i("------->", codigoAnimal + " " + tipoAnimal);
        iniViews();
        obtenerDerivados();

        //Accion al seleccionar un derivado
        spDerivados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                obtenerUnidadMedida(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Accion al presionar el boton "Reportar produccion"
        btnReportarProduccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refProduccion.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long size = dataSnapshot.getChildrenCount();
                        reportarProduccion(
                                String.valueOf(size),
                                txtCodigo.getText().toString(),
                                spDerivados.getSelectedItem().toString(),
                                txtUnidadMedida.getText().toString(),
                                txtCantidad.getText().toString(),
                                txtFeProduccion.getText().toString()
                        );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    /**
     * Metodo para reportar una produccion del animal seleccionado.
     * @param size
     * @param codigoAnimal
     * @param derivado
     * @param unidadMedida
     * @param cantidad
     * @param feProduccion
     */
    public void reportarProduccion(String size, String codigoAnimal, String derivado, String unidadMedida,
                                   String cantidad, String feProduccion){
        if(size.isEmpty()){
            Toast.makeText(this, "Error interno", Toast.LENGTH_SHORT).show();
        }else{
            if(codigoAnimal.isEmpty()){
                Toast.makeText(this, "Falta codigo del animal", Toast.LENGTH_SHORT).show();
            }else{
                if(derivado.isEmpty()){
                    Toast.makeText(this, "Falta seleccionar un derivado", Toast.LENGTH_SHORT).show();
                }else{
                    if (unidadMedida.isEmpty()){
                        Toast.makeText(this, "Falta la unidad de medida", Toast.LENGTH_SHORT).show();
                    }else{
                        if(cantidad.isEmpty()){
                            Toast.makeText(this, "Falta ingresar la cantidad", Toast.LENGTH_SHORT).show();
                        }else{
                            if(feProduccion.isEmpty()){
                                Toast.makeText(this, "Falta la fecha de produccion", Toast.LENGTH_SHORT).show();
                            }else{
                                ProduccionDerivado produccion =
                                        new ProduccionDerivado(codigoAnimal, derivado, unidadMedida, Integer.parseInt(cantidad), feProduccion);
                                refProduccion.child(size).setValue(produccion);
                                Toast.makeText(this, "Produccion registrada con exito", Toast.LENGTH_SHORT).show();
                                this.finish();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Metodo que se encarga de obtener los derivados de un tipo de animal y llena una lista con
     * los derivados encontrados
     */
    private void obtenerDerivados(){
        //Lista para almacenar los nombres de los derivados
        final ArrayList<String> lista = new ArrayList<>();
        refTiposAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Cantidad de hijos que tiene la referencia de tipos de animales
                long size = dataSnapshot.getChildrenCount();
                //Variable de ayuda para compara los nombre del arbol, con el nombre del tipo de
                //animal seleccionado
                String nombre;

                //Ciclo para recorrer el arbol de tipos de animal
                for (long i = 0; i < size; i++){

                    //Se optiene el tipo de animal
                    nombre = dataSnapshot.child(String.valueOf(i)).child("nombre")
                            .getValue(String.class);

                    //Se compara el nombre del tipo de animal en cuestion con el nombre de tipo de
                    //animal del animal seleccionado
                    if(nombre.equals(tipoAnimal)){
                        //Se obtiene la cantidad de hijos que tiene los derivados del tipo de animal
                        long sizeD = dataSnapshot.child(String.valueOf(i)).child("derivadosAnimal")
                                .getChildrenCount();
                        //Log.i("------->", sizeD+"");
                        String nombreD;
                        String unidadMedida;
                        for(long f = 0; f < sizeD; f++){
                            nombreD = dataSnapshot.child(String.valueOf(i)).child("derivadosAnimal")
                                    .child(String.valueOf(f)).child("nombre").getValue(String.class);
                            lista.add(nombreD);
                        }
                        i = size;
                    }
                }
                //Se configura la lista del Spinner en cuestion
                spDerivados.setAdapter(crearAdapter(lista));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Metodo para optener la unidad de medida dado un indice del Spinner de derivados
     * @param index inidice del Spinner de derivados
     */
    public void obtenerUnidadMedida(final int index){
        refTiposAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Cantidad de hijos que tiene la referencia de tipos de animales
                long size = dataSnapshot.getChildrenCount();
                //Variable de ayuda para compara los nombre del arbol, con el nombre del tipo de
                //animal seleccionado
                String nombre;

                //Ciclo para recorrer el arbol de tipos de animal
                for (long i = 0; i < size; i++){

                    //Se optiene el tipo de animal
                    nombre = dataSnapshot.child(String.valueOf(i)).child("nombre")
                            .getValue(String.class);

                    //Se compara el nombre del tipo de animal en cuestion con el nombre de tipo de
                    //animal del animal seleccionado
                    if(nombre.equals(tipoAnimal)){
                        //Se obtiene la cantidad de hijos que tiene los derivados del tipo de animal
                        String unidadMedida = dataSnapshot.child(String.valueOf(i)).child("derivadosAnimal")
                                .child(String.valueOf(index)).child("unidadMedida").getValue(String.class);
                        txtUnidadMedida.setText(unidadMedida);
                        i = size;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    private void iniViews(){
        txtCodigo = findViewById(R.id.txtCodigo);
        txtCodigo.setText(codigoAnimal);

        spDerivados = findViewById(R.id.spDerivados);
        txtUnidadMedida = findViewById(R.id.txtUnidadMedida);
        txtCantidad = findViewById(R.id.txtCantidad);
        txtFeProduccion = findViewById(R.id.txtFeProduccion);

        cvFechaProduccion = findViewById(R.id.cvFechaProduccion);
        txtFeProduccion.setText(    cvFechaProduccion.getYear() + "/"+
                                    cvFechaProduccion.getMonth() + "/" +
                                    cvFechaProduccion.getDayOfMonth());

        btnReportarProduccion = findViewById(R.id.btnReportarProduccion);

    }
}
