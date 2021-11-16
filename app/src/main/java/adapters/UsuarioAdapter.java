package adapters;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import holders.UsuarioHolder;

import models.Hero;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetofirebase.R;
import com.example.projetofirebase.TelaPrincipal;
import com.example.projetofirebase.cardHero;
import com.example.projetofirebase.galeria;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/*
* Nessa classe eu passo as informações para tela 'galeria' e 'cardHero';
* */
public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioHolder> {

    private final ArrayList<Hero> heros;
    private Context context;
    public UsuarioAdapter(ArrayList<Hero> heros){
        this.heros = heros;
    }

    @NonNull
    @Override
    public UsuarioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsuarioHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_usuario, parent, false));
    }

    //Aqui que eu passo os dados para as telas
    @Override
    public void onBindViewHolder(@NonNull UsuarioHolder holder, int position) {

        holder.txtNome.setText(heros.get(position).getNomeHero());

        String urlImage = heros.get(position).getUrlImgHero().replace("http://","https://");
        System.out.println(urlImage);

        //Chama a classe para conseguir baixar a imgagem atravez da URL da API da Marvel
        new DownloadImageTask(holder.imageHero).execute(urlImage);

        //Logo abaixo está como é passado as informações para tela cardHero.
        holder.imageHero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), cardHero.class);
                intent.putExtra("nome", heros.get(position).getNomeHero().toString());
                intent.putExtra("descricao", heros.get(position).getDescricao());
                intent.putExtra("img", urlImage);
                intent.putExtra("idHero", heros.get(position).getId());

                view.getContext().startActivity(intent);;
            }
        });
    }

    //Metodo para trazer as imagens
    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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

    public Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            Log.i("erro ",""+e.getMessage());
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return heros != null ? heros.size() : 0;
    }
}
