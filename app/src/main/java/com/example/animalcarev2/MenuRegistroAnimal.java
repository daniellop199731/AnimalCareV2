package com.example.animalcarev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuRegistroAnimal extends AppCompatActivity {

    //////////////////////////////DECLARACION DE VISTAS
    private TextView txtDatosAnimal;

    private Button btnReportarProduccion, btnReportatMuerte, btnReportarCria;

    //Instansia de la base de datos (Contiene todos los datos)
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private final String C_CRIAS = "CriasV2";

    private DatabaseReference refCrias = database.getReference(C_CRIAS);
    //////////////////////////////DECLARACION DE VARIABLES OPERATIVAS Y DE CONTROL
    //Codigo del animal
    private String codigoAimal;

    private String tipoAnimal;

    private String sexoAnimal;

    private Intent intent;

    //Tamaño del arbol de Animales
    long sizeAnimales = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_registro_animal);
        iniViews();
        final Bundle bundle = this.getIntent().getExtras();
        /**
         * Accion que determina el tamaño del arbol de animales (Canidad de animales registrados)
         */
        refCrias.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!bundle.equals(null)){

                    if(bundle.getString("tipoAccion").equals("muerte")){
                        //Consultar por el codigo los datos del animal
                    }else{
                        //...
                    }
                    String datos = "ERROR";
                    //Se obtiene los datos enviados en "BuscarAnimal.java"

                    codigoAimal = bundle.getString("codigo");
                    tipoAnimal = bundle.getString("tipo");
                    sexoAnimal = bundle.getString("sexo");

                    if(sexoAnimal.equals("Macho")){
                        btnReportarCria.setVisibility(View.INVISIBLE);
                    }

                    sizeAnimales = dataSnapshot.child(codigoAimal).child("listaCrias").getChildrenCount();
                    Log.i("Crias: ", sizeAnimales+"");

                    if(sexoAnimal.equals("Hembra")){
                        datos = "Nombre : " + bundle.getString("nombre") + "\n\n" +
                                "Codigo  : " + bundle.getString("codigo") + "\n\n" +
                                "Tipo      : " + bundle.getString("tipo") + "\n\n" +
                                "Raza     : " + bundle.getString("raza") + "\n\n" +
                                "Sexo     : " + bundle.getString("sexo") + "\n\n" +
                                "Fecha de nacimiento : " + bundle.getString("fecha_nacimiento") + "\n\n" +
                                "Número crias : " + sizeAnimales + "\n";
                        txtDatosAnimal.setText(datos);
                    }else{
                        datos = "Nombre : " + bundle.getString("nombre") + "\n\n" +
                                "Codigo  : " + bundle.getString("codigo") + "\n\n" +
                                "Tipo      : " + bundle.getString("tipo") + "\n\n" +
                                "Raza     : " + bundle.getString("raza") + "\n\n" +
                                "Sexo     : " + bundle.getString("sexo") + "\n\n" +
                                "Fecha de nacimiento : " + bundle.getString("fecha_nacimiento") + "\n";
                        txtDatosAnimal.setText(datos);
                    }

                }else{
                    txtDatosAnimal.setText("Error al obtener los datos");
                    Toast.makeText(getApplicationContext(), "Error: No se pudo obtener los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Accion del boton "Reportar produccion"
        btnReportarProduccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("codigoAnimal", codigoAimal);
                bundle.putString("tipoAnimal", tipoAnimal);
                intent = new Intent(getApplicationContext(), RegistroProduccion.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //Accion del boton "Reportar muerte"
        btnReportatMuerte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("codigoAnimal", codigoAimal);
                intent = new Intent(getApplicationContext(), RegistroMuerte.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //Accion del boton "Reportar cria"
        btnReportarCria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("codigoAnimal", codigoAimal);
                intent = new Intent(getApplicationContext(), RegistroAnimal.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * Metodo que obtiene los contenidos del objeto Bundle, y construye un String, con los datos
     * del animal seleccionado en el Spinner "txtresultado" en "BuscarAnimal.java"
     * @return El String con los datos organizados por lineas.
     */
    private String obtenerDatosAnimal(){
        String datos = "ERROR";
        int numCrias = 0;
        //Se obtiene los datos enviados en "BuscarAnimal.java"
        Bundle bundle = this.getIntent().getExtras();
        if (!bundle.equals(null)){
            codigoAimal = bundle.getString("codigo");
            tipoAnimal = bundle.getString("tipo");
            sexoAnimal = bundle.getString("sexo");

            if(sexoAnimal.equals("Hembra")){
                datos = "Nombre : " + bundle.getString("nombre") + "\n\n" +
                        "Codigo  : " + bundle.getString("codigo") + "\n\n" +
                        "Tipo      : " + bundle.getString("tipo") + "\n\n" +
                        "Raza     : " + bundle.getString("raza") + "\n\n" +
                        "Sexo     : " + bundle.getString("sexo") + "\n\n" +
                        "Fecha de nacimiento : " + bundle.getString("fecha_nacimiento") + "\n\n" +
                        "Número crias : " + sizeAnimales + "\n";
            }else{
                datos = "Nombre : " + bundle.getString("nombre") + "\n\n" +
                        "Codigo  : " + bundle.getString("codigo") + "\n\n" +
                        "Tipo      : " + bundle.getString("tipo") + "\n\n" +
                        "Raza     : " + bundle.getString("raza") + "\n\n" +
                        "Sexo     : " + bundle.getString("sexo") + "\n\n" +
                        "Fecha de nacimiento : " + bundle.getString("fecha_nacimiento") + "\n";
            }
        }else{
            Toast.makeText(this, "Error: No se pudo obtener los datos", Toast.LENGTH_SHORT).show();
        }
        return datos;
    }

    /**
     * Metodo que inicializa, todas las vistas del Activity
     */
    private void iniViews(){
        txtDatosAnimal = findViewById(R.id.txtDatosAnimal);
        //txtDatosAnimal.setText(obtenerDatosAnimal());

        btnReportarProduccion = findViewById(R.id.btnReporteProduccion);
        btnReportatMuerte = findViewById(R.id.btnReporteMuerte);

        btnReportarCria = findViewById(R.id.btnReporteCria);
    }
}
