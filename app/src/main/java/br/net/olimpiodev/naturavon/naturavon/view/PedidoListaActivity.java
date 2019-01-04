package br.net.olimpiodev.naturavon.naturavon.view;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import br.net.olimpiodev.naturavon.naturavon.AppDatabase;
import br.net.olimpiodev.naturavon.naturavon.R;
import br.net.olimpiodev.naturavon.naturavon.adapter.PedidoAdapter;
import br.net.olimpiodev.naturavon.naturavon.model.Pedido;

public class PedidoListaActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView rvPedidos;
    private FloatingActionButton fabPedido;
    private PedidoAdapter pedidoAdapter;
    private List<Pedido> pedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_lista);

        getSupportActionBar().setTitle("Pedidos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME).build();
        rvPedidos = findViewById(R.id.rv_pedidos);

        fabPedido = findViewById(R.id.fab_cadastro_pedido);
        fabPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadastroIntent = new Intent(getApplicationContext(), PedidoCadastroActivity.class);
                startActivityForResult(cadastroIntent, 1);
            }
        });

        GetPedidos getPedidos = new GetPedidos();
        getPedidos.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 1) {
            Toast.makeText(getApplicationContext(), "Pedido cadastrado", Toast.LENGTH_LONG).show();
        } else if (resultCode == 2) {
            Toast.makeText(getApplicationContext(), "Pedido atualizado", Toast.LENGTH_LONG).show();
        }
        GetPedidos getPedidos = new GetPedidos();
        getPedidos.execute();
    }

    private void startRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvPedidos.setLayoutManager(layoutManager);

        pedidoAdapter = new PedidoAdapter(pedidos);
        rvPedidos.setAdapter(pedidoAdapter);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        pedidoAdapter.setClickListener(new PedidoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                final Pedido pedido = pedidos.get(position);
                Intent cadastroIntent = new Intent(getApplicationContext(), PedidoCadastroActivity.class);
                cadastroIntent.putExtra("pedido", pedido);
                startActivityForResult(cadastroIntent, 2);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class GetPedidos extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            pedidos = db.pedidoDao().getPedidos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startRecyclerView();
        }
    }
}
