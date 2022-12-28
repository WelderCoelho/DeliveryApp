package com.welder.appdelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Form_Login extends AppCompatActivity {

    private TextView criar;
    private ImageView logo;
    private EditText email_login;
    private EditText senha_login;
    private TextView erro_login;
    private Button entrar_login;
    private ProgressBar ProgressForm;
    String[] mensagens = {"Preencha todos os campos", "Login  realizado com sucesso"};

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


                entrar_login.setOnClickListener(view -> {

                    String email = email_login.getText().toString();
                    String senha = senha_login.getText().toString();

                    if (email.isEmpty() || senha.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(view,mensagens[0],Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    } else {
                        AutenticarUsuario(view);
                    }
                });





    }


    public  void AutenticarUsuario(View view){

        String email = email_login.getText().toString();
        String senha = senha_login.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ProgressForm.setVisibility(View.VISIBLE);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        IniciarMainActivity();
                    }
                }, 1000);

            }else{

                String erro;


                try {
                    throw task.getException();
                }catch (Exception e){
                    erro = "Erro ao logar usuário!";
                }

                Snackbar snackbar = Snackbar.make(view, erro, Snackbar. LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }

           });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioAtual != null){
            IniciarMainActivity();

        }

    }


    public void IniciarMainActivity(){
        Intent intent = new Intent(Form_Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



    public void IniciarComponentes() {

        criar = findViewById(R.id.criarcontaForm);
        logo = findViewById(R.id.logo);
        email_login = findViewById(R.id.editText_email);
        senha_login = findViewById(R.id.edit_Text_Senha);
        erro_login = findViewById(R.id.textErro);
        entrar_login = findViewById(R.id.entrar);
        ProgressForm = findViewById(R.id.progressForm);


    }


}
