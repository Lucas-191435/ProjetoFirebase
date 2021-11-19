package models;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Hero {

    private  int id;
    private String NomeHero, Descricao, UrlImgHero;

    public Hero(int id, String nomeHero, String descricao, String urlImgHero) {
        this.id = id;
        NomeHero = nomeHero;
        Descricao = descricao;
        UrlImgHero = urlImgHero;
    }
    public Hero() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeHero() {
        return NomeHero;
    }

    public void setNomeHero(String nomeHero) {
        NomeHero = nomeHero;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getUrlImgHero() {
        return UrlImgHero;
    }

    public void setUrlImgHero(String urlImgHero) {
        UrlImgHero = urlImgHero;
    }

    public static ArrayList<Hero> parseObject(String json) {
        System.out.println(json);
        ArrayList<Hero> heros = new ArrayList<>();
        try {

            JSONArray jsonArray = null;
            JSONObject jsonObject = null;

            JSONObject obj = new JSONObject(json);

            JSONObject data = obj.getJSONObject("data");
            jsonArray = data.getJSONArray("results");

            System.out.println(jsonArray);

            for (int i = 0; i < jsonArray.length(); i++) {
                Hero hero = new Hero();
                JSONObject objHero = jsonArray.getJSONObject(i);
                System.out.println(objHero);
                hero.setNomeHero(objHero.getString("name"));
                hero.setDescricao(objHero.getString("description"));
                hero.setId(objHero.getInt("id"));

                JSONObject ojbThumbnail = objHero.getJSONObject("thumbnail");

                String urlImg = ojbThumbnail.getString("path");
                String extension = ojbThumbnail.getString("extension");

                String urlIMG = urlImg + "." + extension;

                hero.setUrlImgHero(urlIMG); // Adiciona imagem com extenção

                heros.add(hero);

                //System.out.println(heros);
            }
            return heros;
        } catch (Exception ex) {
            return heros;
        }
    }

    public static ArrayList<Hero> parseObjectFavorito(String json) {
        System.out.println(json);
        ArrayList<Hero> heros = new ArrayList<>();
        try {

            System.out.println("NÂO PASSA");
            JSONArray jsonArray = new JSONArray(json);
            System.out.println("NÂO PASSA 2");
            for (int i = 0; i < jsonArray.length(); i++) {
                System.out.println("DENTRO DO FOR");
                Hero hero = new Hero();
                JSONObject obj = jsonArray.getJSONObject(i);
                System.out.println("etapa 1 ");
                hero.setNomeHero(obj.getString("name"));
                System.out.println("etapa 2 ");
                hero.setDescricao(obj.getString("descricao"));
                System.out.println("etapa 3 ");
                hero.setId(1);
                System.out.println("etapa 4 ");
                hero.setUrlImgHero(obj.getString("imgHero").toString());
                System.out.println("etapa 5 ");
                heros.add(hero);
                System.out.println("etapa 6 ");
                System.out.println("---AQUI ESTOU TENTANDO MOSTRAR O HEROI QUE FOI ADICIONADO");
                System.out.println(hero);


            }

            return heros;
        } catch (Exception ex) {

            System.out.println("NÃO FOI O JSON DO HEROfirebase");
            System.out.println(ex);
            return heros;
        }
    }
}
