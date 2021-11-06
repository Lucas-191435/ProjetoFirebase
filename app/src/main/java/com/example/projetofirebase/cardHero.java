package com.example.projetofirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import adapters.UsuarioAdapter;

public class cardHero extends AppCompatActivity{

    public TextView nameHero, descricao;
    public ImageView imgHero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_hero);

        nameHero = findViewById(R.id.nameHero);
        descricao = findViewById(R.id.descricao);
        imgHero = findViewById(R.id.imghero);


        nameHero.setText(getIntent().getStringExtra("nome"));
        descricao.setText(getIntent().getStringExtra("descricao"));
        String img = getIntent().getStringExtra("img");


        new UsuarioAdapter.DownloadImageTask(imgHero).execute(img);
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