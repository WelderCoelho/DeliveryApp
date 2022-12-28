package com.welder.appdelivery;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Editar_Perfil extends AppCompatActivity {

    private CircleImageView fotoUsuario;
    private EditText edit_nome;
    private Button bt_atualizarDados, selecionarFoto;
    private Uri mSelecionarUri;
    private String UsuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        IniciarComponentes();


        selecionarFoto.setOnClickListener(view -> SelecionarFotoGaleria());

        bt_atualizarDados.setOnClickListener(view -> {
            String nome = edit_nome.getText().toString();

            if (nome.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {
                AtualizarDados(view);

                Snackbar snackbar = Snackbar.make(view, "Cadastro realizado com sucesso!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });

                snackbar.show();
            }
        });


    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        mSelecionarUri = data.getData();

                        try {

                            fotoUsuario.setImageURI(mSelecionarUri);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    public void SelecionarFotoGaleria() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activityResultLauncher.launch(intent);

    }

    public void AtualizarDados(View view) {

        String nomeArquivo = UUID.randomUUID().toString();

        final StorageReference reference = FirebaseStorage.getInstance().getReference("/imagens/" + nomeArquivo);
        reference.putFile(mSelecionarUri)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(uri -> {

                            String foto = uri.toString();

                            // Iniciar o banco de dados no firebase

                            String nome = edit_nome.getText().toString();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            Map<String, Object> usuarios = new HashMap<>();
                            usuarios.put("nome", nome);
                            usuarios.put("foto", foto);

                            UsuarioID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                            db.collection("Usuarios").document(UsuarioID)
                                    .update("nome", nome, "foto", foto)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });

                        });

                    }



                });
    }

    public void IniciarComponentes() {
        fotoUsuario = findViewById(R.id.img_atualizar);
        edit_nome = findViewById(R.id.atualizar_nome);
        bt_atualizarDados = findViewById(R.id.atualiza_perfil);
        selecionarFoto = findViewById(R.id.atualizar_foto);


    }
}