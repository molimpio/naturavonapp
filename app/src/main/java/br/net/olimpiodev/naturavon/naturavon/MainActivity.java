package br.net.olimpiodev.naturavon.naturavon;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import br.net.olimpiodev.naturavon.naturavon.model.Pedido;
import br.net.olimpiodev.naturavon.naturavon.view.ClienteListaActivity;
import br.net.olimpiodev.naturavon.naturavon.view.PedidoListaActivity;
import br.net.olimpiodev.naturavon.naturavon.view.VendaListaActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppDatabase db;
    private TextView tvTotalVendas, tvTotalClientes, tvInfoPedido;
    private double total = 0.0, totalVendasUltimoPedido = 0.0;
    private int qtdeVendas, totalClientes = 0;
    private Pedido pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvTotalVendas = findViewById(R.id.tv_total_vendas);
        tvTotalClientes = findViewById(R.id.tv_total_clientes);
        tvInfoPedido = findViewById(R.id.tv_info_pedido);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME).build();
        setarPainelHome();
    }

    @SuppressLint("StaticFieldLeak")
    private void setarPainelHome() {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                qtdeVendas = db.vendaDao().getQtdeVendas();
                if (qtdeVendas > 0) {
                    total = db.vendaDao().getTotalVendas();
                    totalClientes = db.clienteDao().getQtdeClientes();
                    pedido = db.pedidoDao().getUltimoPedido();
                    totalVendasUltimoPedido = db.vendaDao().getTotalVendasByPedidoId(pedido.getId());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (qtdeVendas > 0) {
                    @SuppressLint("DefaultLocale")
                    String totalGeral = "R$ " + String.format("%.2f", total);

                    String total = "A soma de todas as vendas realizadas é: \n" + totalGeral;
                    tvTotalVendas.setText(total);

                    String totalC = "Quantidade de clientes cadastrados no sistema: " + totalClientes;
                    tvTotalClientes.setText(totalC);

                    @SuppressLint("DefaultLocale")
                    String totalVendasUltimoPed = "R$ " + String.format("%.2f", totalVendasUltimoPedido);

                    String pedidoC = "Seu último pedido foi " + pedido.getCampanha() +
                            " com total \n" + totalVendasUltimoPed;
                    tvInfoPedido.setText(pedidoC);
                }
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sincronizar) {
            Utils.sincronizar(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_clientes) {
            Intent intentCliente = new Intent(getApplicationContext(), ClienteListaActivity.class);
            startActivity(intentCliente);
        } else if (id == R.id.nav_vendas) {
            Intent intentVenda = new Intent(getApplicationContext(), VendaListaActivity.class);
            startActivity(intentVenda);
        } else if (id == R.id.nav_pedidos) {
            Intent intentPedido = new Intent(getApplicationContext(), PedidoListaActivity.class);
            startActivity(intentPedido);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
