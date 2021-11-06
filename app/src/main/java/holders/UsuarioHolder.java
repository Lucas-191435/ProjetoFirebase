package holders;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.projetofirebase.R;
import com.example.projetofirebase.TelaPrincipal;
import com.example.projetofirebase.cardHero;
import com.example.projetofirebase.galeria;

import androidx.recyclerview.widget.RecyclerView;

public class UsuarioHolder extends RecyclerView.ViewHolder{

    public TextView txtNome, txtEmail ;
    public ImageView imageHero;

    //Estou selecionando os items do item_lista_usuario;
    public UsuarioHolder(View view){
        super(view);
        txtNome = (TextView)view.findViewById(R.id.txtNome);
        //txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        imageHero = (ImageView) view.findViewById(R.id.imageHero);

        /*imageHero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), cardHero.class);
                view.getContext().startActivity(intent);;
            }
        });*/
    }
}
