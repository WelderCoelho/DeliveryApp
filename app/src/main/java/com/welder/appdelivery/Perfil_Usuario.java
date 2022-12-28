package com.welder.appdelivery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil_Usuario extends AppCompatActivity {

    private CircleImageView img_edit_perfil;
    private TextView nome_edit_perfil;
    private TextView email_edit_perfil;
    private Button editar_perfil;
    private String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        IniciarComponentes();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseFirestore db =FirebaseFirestore.getInstance();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener((documentSnapshot, error) -> {

            if (documentSnapshot !=null){

                Glide.with(getApplicationContext()).load(documentSnapshot.getString("foto")).into(img_edit_perfil);
                nome_edit_perfil.setText(documentSnapshot.getString("nome"));
                email_edit_perfil.setText(email);

            }

        });

        editar_perfil.setOnClickListener(view -> {
            Intent intent = new Intent(Perfil_Usuario.this, Editar_Perfil.class);
            startActivity(intent);
        });



    }

    public void IniciarComponentes(){

        img_edit_perfil = findViewById(R.id.img_edit_perfil);
        nome_edit_perfil = findViewById(R.id.nome_editar_perfil);
        email_edit_perfil = findViewById(R.id.email_editar_perfil);
        editar_perfil = findViewById(R.id.editar_perfil);


    }
}