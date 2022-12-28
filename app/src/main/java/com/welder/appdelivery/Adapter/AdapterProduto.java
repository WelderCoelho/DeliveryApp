package com.welder.appdelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.welder.appdelivery.Model.Produtos;
import com.welder.appdelivery.R;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.ProdutoViewHolder> {

    private Context context;
    private List<Produtos>produtosList;

    public AdapterProduto(Context context, List<Produtos> produtosList) {
        this.context = context;
        this.produtosList = produtosList;
    }




    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista;
        LayoutInflater layoutInflater =LayoutInflater.from(context);
        itemLista = layoutInflater.inflate(R.layout.produto_item, parent, false);
        return new ProdutoViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {

            Glide.with(context).load(produtosList.get(position).getFoto()).into(holder.foto);
            holder.nome.setText(produtosList.get(position).getNome());
            holder.preco.setText(produtosList.get(position).getPreco());

    }

    @Override
    public int getItemCount() {
        return produtosList.size();
    }

    public class ProdutoViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView foto;
        private TextView nome;
        private TextView preco;
        private TextView descricao;


        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.fotoProduto);
            nome = itemView.findViewById(R.id.nomeProduto);
            preco = itemView.findViewById(R.id.precoProduto);
            descricao = itemView.findViewById(R.id.dt_descricao_prod);

        }
    }
}
