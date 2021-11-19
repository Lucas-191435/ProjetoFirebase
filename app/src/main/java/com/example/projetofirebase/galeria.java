package com.example.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import adapters.UsuarioAdapter;

import models.Hero;
import service.ServiceAPI;

public class galeria extends AppCompatActivity {

    ProgressDialog dialog;
    ArrayList<Hero> listaUsuarios;
    RecyclerView recyclerUsuario;
    UsuarioAdapter usuarioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        recyclerUsuario = (RecyclerView) findViewById(R.id.recyclerHerois);
        listaUsuarios = new ArrayList<>();


        //GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        //recyclerUsuario.setLayoutManager(layoutManager);
    }

    public void telaFavoritos(View view) {
        Intent intent = new Intent(this, GaleriaFavoritos.class);
        view.getContext().startActivity(intent);
    }



    @Override
    protected void onResume(){
        super.onResume();
        buscarUsuarios();
    }

    private void buscarUsuarios(){
        new UsuarioAPI("GET").execute("Hero","");
    };

    public void setupRecyclerUsuario(){
        //Configurando o layout manager
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //recyclerUsuario.setLayoutManager(layoutManager);


        //Cria o sistema de grid da tela com duas colunas
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerUsuario.setLayoutManager(layoutManager);

        // add adapter
        usuarioAdapter = new UsuarioAdapter(listaUsuarios);
        recyclerUsuario.setAdapter(usuarioAdapter);

        //------divisor entre linhas -- Nele posso usar o grade para mudar a origentação
        //recyclerUsuario.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    public class UsuarioAPI extends AsyncTask<String,String,String>{

        private String metodo;
        public UsuarioAPI(String metodo){
            this.metodo = metodo;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog = ProgressDialog.show(galeria.this,"Aguarde", "Por favor aguarde...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String data = ServiceAPI.getService(strings[0],metodo,strings[1]);
            return data;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            if(metodo == "GET"){
                System.out.println("---ESTOU VENDO O QUE É O 'S'---");
                System.out.println(s);
                listaUsuarios = Hero.parseObject(s);
                setupRecyclerUsuario();

                System.out.println("---Esse É O listaUsuario");
                System.out.println(listaUsuarios.toString());
                dialog.dismiss();
            }
            else if ( s == "OK"){
                Toast.makeText(galeria.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                buscarUsuarios();
            }
        }
    }
}