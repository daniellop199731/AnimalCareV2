package com.example.animalcarev2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuRegistroAnimal extends AppCompatActivity {

    //////////////////////////////DECLARACION DE VISTAS
    private TextView txtDatosAnimal;

    private Button btnReportarProduccion, btnReportatMuerte, btnReportarCria;

    //////////////////////////////DECLARACION DE VARIABLES OPERATIVAS Y DE CONTROL
    //Codigo del animal
    private String codigoAimal;

    private String tipoAnimal;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_registro_animal);
        iniViews();

        //Accion del boton "Reportar produccion"
        btnReportarProduccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("codigoAnimal", codigoAimal);
                bundle.putString("tipoAnimal", tipoAnimal);
                intent = new Intent(getApplicationContext(), ReporteProduccion.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnReportatMuerte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnReportarCria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        //Se obtiene los datos enviados en "BuscarAnimal.java"
        Bundle bundle = this.getIntent().getExtras();
        if (!bundle.equals(null)){
            codigoAimal = bundle.getString("codigo");
            tipoAnimal = bundle.getString("tipo");
            datos =     "Nombre : " + bundle.getString("nombre") + "\n\n" +
                        "Codigo  : " + bundle.getString("codigo") + "\n\n" +
                        "Tipo      : " + bundle.getString("tipo") + "\n\n" +
                        "Raza     : " + bundle.getString("raza") + "\n\n" +
                        "Sexo     : " + bundle.getString("sexo") + "\n\n" +
                        "Fecha de nacimiento : " + bundle.getString("fecha_nacimiento") + "\n";
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
        txtDatosAnimal.setText(obtenerDatosAnimal());

        btnReportarProduccion = findViewById(R.id.btnReporteProduccion);
        btnReportatMuerte = findViewById(R.id.btnReporteMuerte);
        btnReportarCria = findViewById(R.id.btnReporteCria);
    }
}
