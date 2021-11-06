package com.example.projetofirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

        recyclerUsuario = (RecyclerView) findViewById(R.id.recyclerUsuario);
        listaUsuarios = new ArrayList<>();

        //GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        //recyclerUsuario.setLayoutManager(layoutManager);
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
                listaUsuarios = Hero.parseObject(s);
                setupRecyclerUsuario();
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