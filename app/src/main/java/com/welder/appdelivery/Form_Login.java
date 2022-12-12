package com.welder.appdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Form_Login extends AppCompatActivity {

    private TextView criar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        getSupportActionBar().hide();
        IniciarComponentes();

        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Form_Login.this, Form_Cadastro.class);
                startActivity(intent);
            }
        });
    }

    public void IniciarComponentes(){

        criar = findViewById(R.id.criarcontaForm);
    }
}
