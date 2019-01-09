package br.net.olimpiodev.naturavon.naturavon.view;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.net.olimpiodev.naturavon.naturavon.AppDatabase;
import br.net.olimpiodev.naturavon.naturavon.R;
import br.net.olimpiodev.naturavon.naturavon.model.Cliente;

public class ClienteCadastroActivity extends AppCompatActivity {

    private AppDatabase db;
    private TextInputLayout tilNome, tilReferencia;
    private EditText etNome, etReferencia, etTelefone;
    private int clienteId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_cadastro);

        getSupportActionBar().setTitle("Cadastro Cliente");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        etNome = findViewById(R.id.et_nome_cliente);
        etReferencia = findViewById(R.id.et_referencia_cliente);
        etTelefone = findViewById(R.id.et_telefone_cliente);

        tilNome = findViewById(R.id.til_nome_cliente);
        tilReferencia = findViewById(R.id.til_referencia_cliente);

        Button btCadastrarCliente = findViewById(R.id.btCadastrarCliente);
        btCadastrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarCliente();
            }
        });

        if (getIntent().hasExtra("cliente")) {
            Cliente c = (Cliente) getIntent().getSerializableExtra("cliente");
            etNome.setText(c.getNome());
            etReferencia.setText(c.getReferencia());
            etTelefone.setText(c.getTelefone());
            clienteId = c.getId();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void cadastrarCliente() {
        String nome = etNome.getText().toString().trim().toUpperCase();
        String referencia = etReferencia.getText().toString().trim().toUpperCase();
        String telefone = etTelefone.getText().toString().trim().toUpperCase();

        if (validar(nome, referencia)) {
            final Cliente cliente = new Cliente();
            cliente.setNome(nome);
            cliente.setReferencia(referencia);
            cliente.setTelefone(telefone);

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    if (clienteId == 0) {
                        db.clienteDao().insert(cliente);
                    } else {
                        cliente.setId(clienteId);
                        db.clienteDao().update(cliente);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Intent resultIntent = new Intent();
                    if (clienteId == 0) setResult(1, resultIntent);
                    else setResult(2, resultIntent);
                    finish();
                }
            }.execute();
        }
    }

    private boolean validar(String nome, String referencia) {
        if (nome.length() < 3) {
            tilNome.setErrorEnabled(true);
            tilNome.setError("Nome dever ter mais que 2 caracteres");
            return false;
        } else if (referencia.length() < 3) {
            tilReferencia.setErrorEnabled(true);
            tilReferencia.setError("Referência dever ter mais que 2 caracteres");
            return false;
        } else {
            return true;
        }
    }
}
