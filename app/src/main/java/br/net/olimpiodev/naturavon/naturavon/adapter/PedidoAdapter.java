package br.net.olimpiodev.naturavon.naturavon.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.net.olimpiodev.naturavon.naturavon.R;
import br.net.olimpiodev.naturavon.naturavon.model.Pedido;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private List<Pedido> pedidos;
    private static PedidoAdapter.ItemClickListener clickListener;

    public PedidoAdapter(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pedido_card, viewGroup, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder pedidoViewHolder, int position) {
        Pedido pedido = pedidos.get(position);

        pedidoViewHolder.campanha.setText(pedido.getCampanha());
        pedidoViewHolder.data.setText(pedido.getData());
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public void setClickListener(PedidoAdapter.ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    public class PedidoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView campanha;
        final TextView data;

        PedidoViewHolder(View view) {
            super(view);
            campanha = view.findViewById(R.id.tv_campanha_pedido);
            data = view.findViewById(R.id.tv_data_pedido);
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
