package com.example.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import adapters.UsuarioAdapter;
import models.Hero;

public class GaleriaFavoritos extends AppCompatActivity {
    ProgressDialog dialog;
    ArrayList<Hero> listaUsuarios;
    RecyclerView recyclerUsuario;
    UsuarioAdapter usuarioAdapter;

    public String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_favoritos);
        recyclerUsuario = (RecyclerView) findViewById(R.id.recyclerHerois);
        listaUsuarios = new ArrayList<>();

        //buscaFavoritos();
        buscaFavoritos2();

    }

    private void buscaFavoritos2() {
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);

        documentReference.collection("listHero").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            JSONArray jsonArray = new JSONArray();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("-----");
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                JSONObject obj = new JSONObject(document.getData());

                                jsonArray.put(obj);

                            }

                            listaUsuarios = Hero.parseObjectFavorito(jsonArray.toString());
                            setupRecyclerUsuario();

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("FOIIIII");
        //buscarUsuarios();
    }

    public void setupRecyclerUsuario(){

        //Cria o sistema de grid da tela com duas colunas
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerUsuario.setLayoutManager(layoutManager);

        // add adapter
        usuarioAdapter = new UsuarioAdapter(listaUsuarios);
        recyclerUsuario.setAdapter(usuarioAdapter);

    }


    //PRIMEIRO METODO PARA CRIAR OS CARDS COM OS HEROIS NO BANCO
    private void buscaFavoritos() {
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);

        documentReference.collection("listHero").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("-----");
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                //listaUsuarios = Hero.parseObjectFavorito(document.getData());
                                setupRecyclerUsuario();
                                //dialog.dismiss();
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void telaGaleria(View view) {
        Intent intent = new Intent(this, galeria.class);
        view.getContext().startActivity(intent);
    }
}