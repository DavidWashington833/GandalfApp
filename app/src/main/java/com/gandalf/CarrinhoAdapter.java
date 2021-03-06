package com.gandalf;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gandalf.activitys.MainActivity;
import com.gandalf.ProdutoUnicoFragment;
import com.gandalf.R;
import com.gandalf.helpers.CarrinhoSingletonHelper;
import com.gandalf.models.Product;

import java.util.List;

/**
 * Created by gabriel.elmontibelle on 23/11/2017.
 */

public class CarrinhoAdapter  extends BaseAdapter {

    private final List<Product> produtos;
    private final Context context;

    public CarrinhoAdapter(Context context, List<Product> produtos) {
        this.context = context;
        this.produtos = produtos;
    }

    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Object getItem(int position) {
        return produtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(produtos.get(position).getIdProduto());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Product product = produtos.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.cardview_carrinho, parent, false);
        }

        final ViewGroup parentView = parent;

        TextView campoNome = (TextView) view.findViewById(R.id.txtNomeProdCart);
        campoNome.setText(product.getNomeProduto());

        TextView campoPreco = (TextView) view.findViewById(R.id.txtPrecoCart);
        campoPreco.setText(product.getPrecProduto());

        ImageView campoImg = (ImageView) view.findViewById(R.id.imagemProdCart);
        String imagem = product.getImagem();
        if (imagem != null) {
            byte[] imageAsBytes = Base64.decode(imagem.getBytes(), Base64.DEFAULT);
            campoImg.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }

        final EditText campoId = (EditText) view.findViewById(R.id.hiddenIdProdCarrinho);
        campoId.setText(product.getIdProduto());

        Button botaoDetalhes = (Button) view.findViewById(R.id.btnVisualizarCarrinho);

        botaoDetalhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaFragment(new ProdutoUnicoFragment(campoId.getText().toString()));
//                Toast.makeText(v.getContext(), campoId.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });

        Button botaoRemover = (Button) view.findViewById(R.id.btnRemoverProduto);

        botaoRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarrinhoSingletonHelper.getInstance().removeProduto(product);
                for (int i = 0; i < produtos.size(); i++){
                    if(product.getIdProduto().equals(produtos.get(i).getIdProduto())){
                        produtos.remove(i);
                        break;
                    }
                }
                notifyDataSetChanged();
                Toast.makeText(context, "Produto removido com sucesso!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void chamaFragment(Fragment fragment){
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = ((MainActivity)context).getSupportFragmentManager();
        manager.popBackStackImmediate(backStateName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.add(R.id.activity_main_tabs, fragment);
//        transaction.addToBackStack(backStateName);
//        transaction.commit();
    }
}
