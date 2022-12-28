package com.welder.appdelivery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.welder.appdelivery.Adapter.AdapterProduto;
import com.welder.appdelivery.Model.Produtos;
import com.welder.appdelivery.RecyclerViewItemClickListener.RecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView_produtos;
    private AdapterProduto adapterProduto;
    private List<Produtos> produtosList;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView_produtos = findViewById(R.id.recycleViewlistaProdutos);
        produtosList = new ArrayList<>();
        adapterProduto = new AdapterProduto(getApplicationContext(),produtosList);
        recyclerView_produtos.setLayoutManager(new LinearLayoutManager(getApplicationContext()) );
        recyclerView_produtos.setHasFixedSize(true);
        recyclerView_produtos.setAdapter(adapterProduto);



        //Evento de click no RecyclerView

        recyclerView_produtos.addOnItemTouchListener(new
                RecyclerViewItemClickListener(
                    getApplicationContext(),
                         recyclerView_produtos,
                            new RecyclerViewItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    //evento de click RecycleView



                                }

                                @Override
                                public void onItemClick(View view, int position) {

                                    Intent intent = new Intent(MainActivity.this, Detalhes_Produto.class);
                                    intent.putExtra("foto", produtosList.get(position).getFoto());
                                    intent.putExtra("nome",produtosList.get(position).getNome());
                                    intent.putExtra("descricao", produtosList.get(position).getDescricao());
                                    intent.putExtra("preco",produtosList.get(position).getPreco());
                                    startActivity(intent);

                                }

                                @Override
                                public void onLongItemClick(View view, int position) {



                                }
                            }

                        )


                );




                    db = FirebaseFirestore.getInstance();
        db.collection("Produtos").orderBy("nome")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            Produtos produtos = queryDocumentSnapshot.toObject(Produtos.class);
                            produtosList.add(produtos);
                            adapterProduto.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemID = item.getItemId();

        if (itemID == R.id.perfil){
            Intent intent = new Intent(MainActivity.this, Perfil_Usuario.class);
            startActivity(intent);

        }else  if (itemID == R.id.carrinho){


        }else if (itemID == R.id.sair){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "Usuário deslogado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Form_Login.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}