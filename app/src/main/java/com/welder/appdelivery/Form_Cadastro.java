package com.welder.appdelivery;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.grpc.Context;

public class Form_Cadastro extends AppCompatActivity {

    private CircleImageView perfilCadastro;
    private Button selecionarImg;
    private EditText nomeCadastro;
    private EditText emailCadastro;
    private EditText senhaCadastro;
    private TextView erroCadastro;
    private Button cadastrar;
    private Uri mSelecionarUri;
    private String UsuarioID;


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

        selecionarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelecionarFotoGaleria();
            }
        });


    }



    public void CadastrarUsuario(View view){

        String email = emailCadastro.getText().toString();
        String senha = senhaCadastro.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                SalvarDadosUsuario();

                Snackbar snackbar = Snackbar.make(view, "Cadastro realizado com sucesso!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", view1 -> finish());

                snackbar.show();
            }else{

                String erro;

                try {
                    throw task.getException();
                }catch (FirebaseAuthWeakPasswordException e) {
                    erro = "Coloque uma senha com no mínimo 6 caracteres!";

                }catch (FirebaseAuthInvalidCredentialsException e) {
                    erro = "E-mail Invalido!";

                }catch (FirebaseAuthUserCollisionException e) {
                    erro = "Esta conta já foi cadastrada!";

                }catch (FirebaseNetworkException e){
                    erro = "Sem conexão com a internet!";

                } catch (Exception e) {
                   erro = "Erro ao Cadastrar Usuario";
                }

                erroCadastro.setText(erro);
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

    ActivityResultLauncher<Intent>activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        mSelecionarUri = data.getData();

                        try {

                            perfilCadastro.setImageURI(mSelecionarUri);

                        }catch ( Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });


    public void SelecionarFotoGaleria(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activityResultLauncher.launch(intent);

    }

    public void SalvarDadosUsuario(){
        String nomeArquivo = UUID.randomUUID().toString();
        final StorageReference reference = FirebaseStorage.getInstance().getReference("/imagens/" + nomeArquivo);
        reference.putFile(mSelecionarUri)
                .addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {

                    String foto = uri.toString();

                    // Iniciar o banco de dados no firebase

                    String nome = nomeCadastro.getText().toString();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Map<String, Object> usuarios = new HashMap<>();
                    usuarios.put("nome", nome);
                    usuarios.put("foto", foto);

                    UsuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DocumentReference documentReference = db.collection("Usuarios").document(UsuarioID);
                    documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("db", "Sucesso ao salvar dados!");

                        }
                    });

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.i("db_erro", "Erro ao salvar os dados!" + e.toString());

                    }
                })).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }

    public void IniciarCadastro(){

        perfilCadastro=findViewById(R.id.img_perfil);
        selecionarImg=findViewById(R.id.selecionarimg);
        nomeCadastro=findViewById(R.id.editText_Nome);
        emailCadastro=findViewById(R.id.editText_email_cadastro);
        senhaCadastro=findViewById(R.id.edit_Text_Senha_cadastro);
        erroCadastro=findViewById(R.id.textErro_cadastro);
        cadastrar=findViewById(R.id.cadastrar);
    }
}


