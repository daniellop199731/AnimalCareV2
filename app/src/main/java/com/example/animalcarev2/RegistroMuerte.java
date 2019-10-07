package com.example.animalcarev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

    public class RegistroMuerte extends AppCompatActivity {

        private EditText txtCodigoAnimalM, txtDescMuerte;
        private Button btnReportarMuerte;
        private DatePicker cvFechaMuerte;

        private String codigoAnimal;

        //Constante para ayudar en la definicion de la referencia(Pentiene para pasar a una clase)
        private final String C_MUERTES = "Muertes";
        private final String C_ANIMALES = "Animales";

        //Instansia de la base de datos (Contiene todos los datos)
        private FirebaseDatabase database = FirebaseDatabase.getInstance();

        private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        private DatabaseReference refMuertes = database.getReference(C_MUERTES);
        private DatabaseReference refAnimales = database.getReference(C_ANIMALES);
        private DatabaseReference enVida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_muerte);
        Bundle bundle = this.getIntent().getExtras();
        codigoAnimal = bundle.getString("codigoAnimal");
        iniViews();
        Log.i("----->", codigoAnimal);

        btnReportarMuerte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refAnimales.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Long size = dataSnapshot.getChildrenCount();
                        String codigo;
                        Log.i("->>>>><", "Holi " + size);
                        for (long i = 0; i < size; i++){
                            codigo = dataSnapshot.child(String.valueOf(i)).child("codigo").getValue(String.class);
                            if(codigo.equals(codigoAnimal)){
                                //refAnimales.child(String.valueOf(i)).child("enVida").setValue("n");
                                enVida = ref.child("Animales").child(String.valueOf(i)).child("enVida");
                                enVida.setValue("n");
                                Log.i("------>", i+"");
                                i = size;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                refMuertes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Long size = dataSnapshot.getChildrenCount();
                        reportarMuerte(
                                String.valueOf(size),
                                codigoAnimal,
                                cvFechaMuerte.getYear()+"/"+
                                        cvFechaMuerte.getMonth()+"/"+
                                        cvFechaMuerte.getDayOfMonth(),
                                txtDescMuerte.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void reportarMuerte(String size, String codigo, String fecha, String desc){
        if (codigo.isEmpty()){
            Toast.makeText(this, "Falta el codigo del animal", Toast.LENGTH_SHORT).show();
        }else {
            if(fecha.isEmpty()){
                Toast.makeText(this, "Error en la fecha actual", Toast.LENGTH_SHORT).show();
            }else{
                if(desc.isEmpty()){
                    Toast.makeText(this, "Falta la descripción", Toast.LENGTH_SHORT).show();
                }else{
                    MuerteAnimal muerte = new MuerteAnimal(codigo, fecha, desc);
                    refMuertes.child(size).setValue(muerte);
                    Toast.makeText(this, "Muerte registrada con exito", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
            }
        }
    }

    private void iniViews(){
        txtCodigoAnimalM = findViewById(R.id.txtCodigoAnimalM);
        txtCodigoAnimalM.setText(codigoAnimal);

        txtDescMuerte = findViewById(R.id.txtDescMuerte);
        btnReportarMuerte = findViewById(R.id.btnReportarMuerte);
        cvFechaMuerte = findViewById(R.id.cvFechaMuerte);
    }
}
