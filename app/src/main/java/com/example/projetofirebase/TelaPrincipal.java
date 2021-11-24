package com.example.projetofirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class TelaPrincipal extends AppCompatActivity {
    private TextView userName;
    private Button deslogar;

    private String nome;
    String usuarioID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        userName = findViewById(R.id.textNomeUsuario);


        //Essa parte do codigo estou usando para manipular dados que estão no banco.☜(ﾟヮﾟ☜)

        //Nessa estou selecionando o id do usuário que está logado
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //crio uma conexão com o banco
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Seleciono o documendo do usuário logado
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        //Seleciono as colecoes de documentos, que fazem referencia a usuarios.
        CollectionReference db2 = db.collection("Usuarios");

        //crio um metodo para mostrar todos os documentos de usuários.
        db2.
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("nome", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("nome", "Error getting documents: ", task.getException());
                        }
                    }
        });



        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    // seleciona campo de um documento no firebase
                    userName.setText(documentSnapshot.getData().get("nome").toString());
                    //userName.setText("user");
                }
            }
        });



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
        view.getContext().startActivity(intent);
//        startActivity(intent);
//        finish();
    }

}