package com.example.projetofirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class TelaPrincipal extends AppCompatActivity {

    private Button deslogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        IniciaComponents();
        getSupportActionBar().hide();

        deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("OI");

                FirebaseAuth.getInstance().signOut();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        irFormLogin();
                    }
                }, 2000);
            }
        });
    }

    private void IniciaComponents(){
        deslogar = findViewById(R.id.deslogar);
    }

    private void irFormLogin(){
        Intent intent = new Intent(TelaPrincipal.this, FormLogin.class);
        startActivity(intent);
        finish();
    }
    public void irGaleria(View view) {
        Intent intent = new Intent(this, galeria.class);
        startActivity(intent);
        finish();
    }

}