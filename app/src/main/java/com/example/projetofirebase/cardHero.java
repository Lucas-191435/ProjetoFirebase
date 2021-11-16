package com.example.projetofirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.firestore.SetOptions;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import adapters.UsuarioAdapter;

public class cardHero extends AppCompatActivity{

    public TextView nameHero, descricao;
    public ImageView imgHero;

    String idHero,img;
    public String usuarioID;
    boolean isFavourite = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_hero);

        //Selecino os componentes na activity cardHero
        nameHero = findViewById(R.id.nameHero);
        descricao = findViewById(R.id.descricao);
        imgHero = findViewById(R.id.imghero);

        //Recebe as informações que são passadas na classe 'UsuarioAdapter, para criar uma tela com o meu heroi que aparece na tela galeria'
        nameHero.setText(getIntent().getStringExtra("nome"));
        descricao.setText(getIntent().getStringExtra("descricao"));
        img = getIntent().getStringExtra("img");
        idHero = getIntent().getStringExtra("idHero");

        new UsuarioAdapter.DownloadImageTask(imgHero).execute(img);

        vericaHeroFavorite();

        //Parte do botão de favorito
        final ImageButton imgButton =(ImageButton)findViewById(R.id.favbtn);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //boolean isFavourite = readState();
                //removendo as informações do banco
                if (isFavourite) {
                    imgButton.setBackgroundResource(R.drawable.ic_person);
                    removeHeroFavorite();
                    isFavourite = false;
                    saveState(isFavourite);
                }
                //Aqui estou enviando as informações para o banco
                else {
                    imgButton.setBackgroundResource(R.drawable.ic_email);
                    addHeroFavorite();
                    isFavourite = true;
                    saveState(isFavourite);
                }
            }
        });


    }

    private void vericaHeroFavorite() {

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);

        String nomeDocHero = getIntent().getStringExtra("nome");
        String nomeHero;

        //documentReference.collection("listHero").document(nomeDocHero).get("nome");

        documentReference.collection("listHero").document(nomeDocHero).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("EXISTE O DOCUMENTO");
                        final ImageButton imgButton =(ImageButton)findViewById(R.id.favbtn);
                        imgButton.setBackgroundResource(R.drawable.ic_email);
                        isFavourite = true;
                        saveState(isFavourite);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    private void removeHeroFavorite() {
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);

        String nomeDocHero = getIntent().getStringExtra("nome");

        documentReference.collection("listHero").document(nomeDocHero)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
    }

    private void addHeroFavorite() {

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);

        Map<String,Object> hero = new HashMap<>();
        hero.put("idHero", idHero);
        hero.put("name", getIntent().getStringExtra("nome"));
        hero.put("descricao", getIntent().getStringExtra("descricao"));
        hero.put("imgHero", img);

        String nomeDocHero = getIntent().getStringExtra("nome");

        documentReference.collection("listHero").document(nomeDocHero).set(hero);
    }

    //Metodos para o botão de favorito
    private void saveState(boolean isFavourite) {
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putBoolean("State", isFavourite);
        aSharedPreferencesEdit.commit();
    }
    private boolean readState() {
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        return aSharedPreferences.getBoolean("State", true);
    }

    //Metodo para trazer as imagens
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}


