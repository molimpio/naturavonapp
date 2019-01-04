package br.net.olimpiodev.naturavon.naturavon.view;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import br.net.olimpiodev.naturavon.naturavon.AppDatabase;
import br.net.olimpiodev.naturavon.naturavon.MainActivity;
import br.net.olimpiodev.naturavon.naturavon.R;
import br.net.olimpiodev.naturavon.naturavon.model.Produto;

public class SplashScreenActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, AppDatabase.DB_NAME).build();
        insertProdutosIniciais();
    }

    @SuppressLint("StaticFieldLeak")
    private void insertProdutosIniciais() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (db.produtoDao().getQtdeProdutos() == 0) {
                    String[] produtos = getResources().getStringArray(R.array.produtos);

                    for (String produto : produtos) {
                        String dados[] = produto.split("-");
                        Produto p = new Produto();
                        p.setCodigo(dados[0]);
                        p.setNome(dados[1]);
                        p.setValor(0.0);
                        db.produtoDao().insert(p);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                loadActivity();
            }
        }.execute();
    }

    private void loadActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
