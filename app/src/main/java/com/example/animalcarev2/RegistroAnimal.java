package com.example.animalcarev2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroAnimal extends AppCompatActivity {

    //Vistas o componentes que se usaran
    EditText txtNombreAnimal, txtCodigoAnimal, txtTipoAnimal, txtRazaAnimal;
    RadioButton rbSexoHembra, rbSexoMacho;
    Button btnAlmacenarAnimal;
    CalendarView cvFechaNacimiento;

    //Constantes
    private final String SEXO_HEMBRA = "Hembra";
    private final String SEXO_MACHO = "Macho";

    //Referencia a la colecion Animal
    private DatabaseReference animal;

    private String codigo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_animal);
        iniViwes();
        animal = FirebaseDatabase.getInstance().getReference("Animales");
        codigo = animal.push().getKey();
        txtCodigoAnimal.setText("Codigo: "+ codigo);


        btnAlmacenarAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarAnimal();
            }
        });

    }

    public void registrarAnimal(){
        String nombre = txtNombreAnimal.getText().toString();
        String tipo = txtTipoAnimal.getText().toString();
        String raza = txtRazaAnimal.getText().toString();
        String sexo;
        if (rbSexoHembra.isChecked()){
            sexo = SEXO_HEMBRA;
        } else {
            sexo = SEXO_MACHO;
        }
        long fecha_nacimiento = cvFechaNacimiento.getDate();

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

                        //String codigo = animal.push().getKey();
                        Animal nuevoAnimal = new Animal(nombre, codigo, tipo, raza, sexo, fecha_nacimiento);
                        animal.child(codigo).setValue(nuevoAnimal);

                        Toast.makeText(this, "Animal registrado con exito", Toast.LENGTH_SHORT).show();
                        this.finish();
                    }
                }
            }
        }

    }

    /**
     *  Metodo que se usa para iniciar las vistas o componentes, en el metodo OnCreate
     */
    public void iniViwes(){
        txtNombreAnimal = (EditText) findViewById(R.id.txtNombreAnimal);
        txtCodigoAnimal = (EditText) findViewById(R.id.txtCodigoAnimal);
        txtTipoAnimal = (EditText) findViewById(R.id.txtTipoAnimal);
        txtRazaAnimal = (EditText) findViewById(R.id.txtRazaAnimal);

        rbSexoHembra = (RadioButton) findViewById(R.id.rbSexoHembra);
        rbSexoMacho = (RadioButton) findViewById(R.id.rbSexoMacho);

        cvFechaNacimiento = (CalendarView)findViewById(R.id.cvFechaNacimiento);

        btnAlmacenarAnimal = (Button) findViewById(R.id.btnAlmacenarAnimal);
    }
}
