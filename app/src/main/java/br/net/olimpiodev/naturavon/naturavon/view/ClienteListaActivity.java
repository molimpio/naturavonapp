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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import br.net.olimpiodev.naturavon.naturavon.AppDatabase;
import br.net.olimpiodev.naturavon.naturavon.R;
import br.net.olimpiodev.naturavon.naturavon.adapter.ClienteAdapter;
import br.net.olimpiodev.naturavon.naturavon.model.Cliente;

public class ClienteListaActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView rvClientes;
    private FloatingActionButton fabCliente;
    private ClienteAdapter clienteAdapter;
    private List<Cliente> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_lista);

        getSupportActionBar().setTitle("Clientes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME).build();
        rvClientes = findViewById(R.id.rv_clientes);

        fabCliente = findViewById(R.id.fab_cadastro_cliente);
        fabCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadastroIntent = new Intent(getApplicationContext(), ClienteCadastroActivity.class);
                startActivityForResult(cadastroIntent, 1);
            }
        });

        GetClientes getClientes = new GetClientes();
        getClientes.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 1) {
            Toast.makeText(getApplicationContext(), "Cliente cadastrado", Toast.LENGTH_LONG).show();
        } else if (resultCode == 2) {
            Toast.makeText(getApplicationContext(), "Cliente atualizado", Toast.LENGTH_LONG).show();
        }
        GetClientes getClientes = new GetClientes();
        getClientes.execute();
    }

    private void startRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvClientes.setLayoutManager(layoutManager);

        clienteAdapter = new ClienteAdapter(clientes);
        rvClientes.setAdapter(clienteAdapter);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        clienteAdapter.setClickListener(new ClienteAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                final Cliente cliente = clientes.get(position);
                Intent cadastroIntent = new Intent(getApplicationContext(), ClienteCadastroActivity.class);
                cadastroIntent.putExtra("cliente", cliente);
                startActivityForResult(cadastroIntent, 2);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class GetClientes extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            clientes = db.clienteDao().getClientes();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startRecyclerView();
        }
    }
}
