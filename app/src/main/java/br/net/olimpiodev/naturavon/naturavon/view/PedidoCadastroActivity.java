package br.net.olimpiodev.naturavon.naturavon.view;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.net.olimpiodev.naturavon.naturavon.AppDatabase;
import br.net.olimpiodev.naturavon.naturavon.R;
import br.net.olimpiodev.naturavon.naturavon.Utils;
import br.net.olimpiodev.naturavon.naturavon.model.Pedido;

public class PedidoCadastroActivity extends AppCompatActivity {

    private AppDatabase db;
    private TextInputLayout tilCampanha;
    private EditText etCampanha;
    private int pedidoId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_cadastro);

        getSupportActionBar().setTitle("Cadastro Pedido");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        etCampanha = findViewById(R.id.et_campanha_pedido);
        tilCampanha = findViewById(R.id.til_campanha_pedido);

        Button btCadastrarPedido = findViewById(R.id.btCadastrarPedido);
        btCadastrarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarPedido();
            }
        });

        if (getIntent().hasExtra("pedido")) {
            Pedido p = (Pedido) getIntent().getSerializableExtra("pedido");
            etCampanha.setText(p.getCampanha());
            pedidoId = p.getId();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void cadastrarPedido() {
        String campanha = etCampanha.getText().toString().trim().toUpperCase();

        if (validar(campanha)) {
            final Pedido pedido = new Pedido();
            pedido.setCampanha(campanha);
            pedido.setData(Utils.getDataNow());

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    if (pedidoId == 0) {
                        db.pedidoDao().insert(pedido);
                    } else {
                        pedido.setId(pedidoId);
                        db.pedidoDao().update(pedido);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Intent resultIntent = new Intent();
                    if (pedidoId == 0) setResult(1, resultIntent);
                    else setResult(2, resultIntent);
                    finish();
                }
            }.execute();
        }
    }

    private boolean validar(String campanha) {
        if (campanha.length() < 3) {
            tilCampanha.setErrorEnabled(true);
            tilCampanha.setError("Campanha dever ter mais que 2 caracteres");
            return false;
        } else {
            return true;
        }
    }
}
