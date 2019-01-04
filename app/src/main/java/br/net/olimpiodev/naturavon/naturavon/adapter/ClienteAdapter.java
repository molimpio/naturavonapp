package br.net.olimpiodev.naturavon.naturavon.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.net.olimpiodev.naturavon.naturavon.R;
import br.net.olimpiodev.naturavon.naturavon.model.Cliente;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> clientes;
    private static ClienteAdapter.ItemClickListener clickListener;

    public ClienteAdapter(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cliente_card, viewGroup, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder clienteViewHolder, int position) {
        Cliente cliente = clientes.get(position);

        clienteViewHolder.nome.setText(cliente.getNome());
        clienteViewHolder.referencia.setText(cliente.getReferencia());
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public void setClickListener(ClienteAdapter.ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    public class ClienteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nome;
        final TextView referencia;

        ClienteViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.tv_nome_cliente);
            referencia = view.findViewById(R.id.tv_referencia_cliente);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position, View view);
    }
}
