package com.welder.appdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Detalhes_Produto extends AppCompatActivity {

    private ImageView dt_foto;
    private TextView dt_nome, dt_descricao, dt_preco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);

        IniciarComponentes();

        String foto = getIntent().getExtras().getString("foto");
        String nome = getIntent().getExtras().getString("nome");
        String descricao = getIntent().getExtras().getString("descricao");
        String preco = getIntent().getExtras().getString("preco");



        Glide.with(getApplicationContext()).load(foto).into(dt_foto);
        dt_nome.setText(nome);
        dt_descricao.setText(descricao);
        dt_preco.setText(preco);



    }

    public void IniciarComponentes(){
        dt_foto = findViewById(R.id.dt_foto_prod);
        dt_nome = findViewById(R.id.dt_nome_prod);
        dt_preco = findViewById(R.id.dt_preco_prod);
        dt_descricao = findViewById(R.id.dt_descricao_prod);
    }



}