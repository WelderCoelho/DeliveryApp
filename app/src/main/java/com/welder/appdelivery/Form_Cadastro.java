package com.welder.appdelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class Form_Cadastro extends AppCompatActivity {

    private CircleImageView perfilCadastro;
    private Button selecionarImg;
    private EditText nomeCadastro;
    private EditText emailCadastro;
    private EditText senhaCadastro;
    private TextView erroCadastro;
    private Button cadastrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        getSupportActionBar().hide();



        IniciarCadastro();
        nomeCadastro.addTextChangedListener(cadastroTextWatcher);
        emailCadastro.addTextChangedListener(cadastroTextWatcher);
        senhaCadastro.addTextChangedListener(cadastroTextWatcher);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CadastrarUsuario(view);
            }
        });


    }

    public void CadastrarUsuario(View view){

        String email = emailCadastro.getText().toString();
        String senha = senhaCadastro.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(view, "Cadastro realizado com sucesso!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                }
                            });

                    snackbar.show();
                }
            }
        });

    }



    TextWatcher cadastroTextWatcher = new TextWatcher() {   //observador
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {  //Caso todos os campos estejam preenchido, habilita o botao cadastrar.

            String nome = nomeCadastro.getText().toString();
            String email = emailCadastro.getText().toString();
            String senha = senhaCadastro.getText().toString();

            if (!nome.isEmpty() && !email.isEmpty()  && !senha.isEmpty()){
                cadastrar.setEnabled(true);
                cadastrar.setBackgroundColor(getResources().getColor(R.color.vermelho));
            } else{
                cadastrar.setEnabled(false);
                cadastrar.setBackgroundColor(getResources().getColor(R.color.gray));

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };


    public void IniciarCadastro(){

        perfilCadastro = findViewById(R.id.img_perfil);
        selecionarImg = findViewById(R.id.selecionarimg);
        nomeCadastro = findViewById(R.id.editText_Nome);
        emailCadastro = findViewById(R.id.editText_email_cadastro);
        senhaCadastro = findViewById(R.id.edit_Text_Senha_cadastro);
        erroCadastro = findViewById(R.id.textErro_cadastro);
        cadastrar = findViewById(R.id.cadastrar);
    }
}

