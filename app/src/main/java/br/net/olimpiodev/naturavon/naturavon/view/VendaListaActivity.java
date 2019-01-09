package br.net.olimpiodev.naturavon.naturavon.view;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import br.net.olimpiodev.naturavon.naturavon.AppDatabase;
import br.net.olimpiodev.naturavon.naturavon.R;
import br.net.olimpiodev.naturavon.naturavon.model.ChaveValor;
import br.net.olimpiodev.naturavon.naturavon.model.Venda;
import br.net.olimpiodev.naturavon.naturavon.model.VendaClientePedido;

public class VendaListaActivity extends AppCompatActivity {

    private AppDatabase db;
    private List<VendaClientePedido> vendas;
    private List<ChaveValor> clientes;
    private List<ChaveValor> pedidos;
    private Spinner spCliente, spPedidos;
    private int clienteId, pedidoId;
    private TableLayout tbVendas;
    private TextView tvTotal;
    private Switch swPedido, swCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_lista);

        getSupportActionBar().setTitle("Vendas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        tbVendas = findViewById(R.id.tl_vendas);
        tvTotal = findViewById(R.id.tv_total);
        swPedido = findViewById(R.id.sw_pedido);
        swCliente = findViewById(R.id.sw_cliente);

        spCliente = findViewById(R.id.sp_cliente_venda);
        spCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clienteId = clientes.get(position).getChave();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPedidos = findViewById(R.id.sp_pedido_venda);
        spPedidos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pedidoId = pedidos.get(position).getChave();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btPesquisarVendas = findViewById(R.id.bt_pesquisar_vendas);
        btPesquisarVendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetVendas getVendas = new GetVendas();
                getVendas.execute();
            }
        });

        GetDadosDropDowns getDadosDropDowns = new GetDadosDropDowns();
        getDadosDropDowns.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_venda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.adicionarVenda:
                Intent cadastroIntent = new Intent(getApplicationContext(), VendaCadastroActivity.class);
                startActivityForResult(cadastroIntent, 1);
                return true;
            case R.id.removerVenda:
                removerVenda();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removerVenda() {
        AlertDialog alertDialog;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Remover Venda");
        alertDialogBuilder.setMessage("Para remover a venda preencha o ID");

        final EditText etVendaId = new EditText(this);

        etVendaId.setHint("ID da venda");

        etVendaId.setInputType(InputType.TYPE_CLASS_NUMBER);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(etVendaId);

        alertDialogBuilder.setView(linearLayout);

        alertDialogBuilder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String vendaId = etVendaId.getText().toString();
                if (vendaId.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Campo ID é obrigatório", Toast.LENGTH_SHORT).show();
                } else {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            int id = Integer.parseInt(vendaId);
                            Venda venda = db.vendaDao().getVendaById(id);
                            db.vendaDao().delete(venda);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Venda excluída com sucesso", Toast.LENGTH_SHORT).show();
                            clearRows();
                        }
                    }.execute();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Ação cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void startSpinners() {
        ArrayAdapter<ChaveValor> adapterClientes = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, clientes);
        spCliente.setAdapter(adapterClientes);

        ArrayAdapter<ChaveValor> adapterPedidos = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, pedidos);
        spPedidos.setAdapter(adapterPedidos);
    }

    private void clearRows() {
        tbVendas.removeAllViews();
        tvTotal.setText("");
    }

    private void addRows() {
        clearRows();
        TextView textViewC0 = new TextView(this);
        textViewC0.setText(getResources().getString(R.string.id));
        textViewC0.setPadding(10, 10, 10, 10);

        TextView textViewC1 = new TextView(this);
        textViewC1.setText(getResources().getString(R.string.cliente));
        textViewC1.setPadding(10, 10, 10, 10);

        TextView textViewCamp = new TextView(this);
        if (!swPedido.isChecked() && swCliente.isChecked()) {
            textViewCamp.setText("C");
            textViewCamp.setPadding(10, 10, 10, 10);
        }

        TextView textViewC2 = new TextView(this);
        textViewC2.setText(getResources().getString(R.string.produto));
        textViewC2.setPadding(10, 10, 10, 10);

        TextView textViewC3 = new TextView(this);
        textViewC3.setText(getResources().getString(R.string.valor));
        textViewC3.setPadding(10, 10, 10, 10);

        TextView textViewC4 = new TextView(this);
        textViewC4.setText(getResources().getString(R.string.qtde));
        textViewC4.setPadding(10, 10, 10, 10);

        TextView textViewC5 = new TextView(this);
        textViewC5.setText(getResources().getString(R.string.total));
        textViewC5.setPadding(10, 10, 10, 10);

        TableRow tableRowHeader = new TableRow(this);
        tableRowHeader.addView(textViewC0);
        tableRowHeader.addView(textViewC1);

        if (!swPedido.isChecked() && swCliente.isChecked()) tableRowHeader.addView(textViewCamp);

        tableRowHeader.addView(textViewC2);
        tableRowHeader.addView(textViewC3);
        tableRowHeader.addView(textViewC4);
        tableRowHeader.addView(textViewC5);

        tbVendas.addView(tableRowHeader);

        double total = 0.0;

        for (VendaClientePedido v: vendas) {

            total += v.getTotal();

            TextView textViewID = new TextView(this);
            textViewID.setText(String.valueOf(v.getIdVenda()));
            textViewID.setPadding(10, 10, 10, 10);

            TextView textViewCliente = new TextView(this);
            textViewCliente.setText(v.getCliente());
            textViewCliente.setPadding(10, 10, 10, 10);

            TextView textViewCampanha = new TextView(this);

            if (!swPedido.isChecked() && swCliente.isChecked()) {
                String pedido = v.getPedido().split(" ")[1];
                textViewCampanha.setText(pedido);
                textViewCampanha.setPadding(10, 10, 10, 10);
            }

            TextView textViewProduto = new TextView(this);
            String produto = v.getProduto();
            if (v.getProduto().length() > 10) {
                produto = v.getProduto().substring(0, 10);
            }
            textViewProduto.setText(produto);
            textViewProduto.setPadding(10, 10, 10, 10);

            TextView textViewValor = new TextView(this);
            textViewValor.setText(String.valueOf(v.getValor()));
            textViewProduto.setPadding(10, 10, 10, 10);

            TextView textViewQtde = new TextView(this);
            textViewQtde.setText(String.valueOf(v.getQuantidade()));
            textViewQtde.setPadding(10, 10, 10, 10);

            TextView textViewTotal = new TextView(this);
            textViewTotal.setText(String.valueOf(v.getTotal()));
            textViewTotal.setPadding(10, 10, 10, 10);

            TableRow tableRow = new TableRow(this);
            tableRow.addView(textViewID);
            tableRow.addView(textViewCliente);

            if (!swPedido.isChecked() && swCliente.isChecked()) tableRow.addView(textViewCampanha);

            tableRow.addView(textViewProduto);
            tableRow.addView(textViewValor);
            tableRow.addView(textViewQtde);
            tableRow.addView(textViewTotal);

            tbVendas.addView(tableRow);
        }

        @SuppressLint("DefaultLocale") String totalGeral = "R$ " + String.format("%.2f", total);
        tvTotal.setText(totalGeral);
    }

    @SuppressLint("StaticFieldLeak")
    private class GetVendas extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (swPedido.isChecked() && swCliente.isChecked()) {
                vendas = db.vendaDao().getVendas(pedidoId, clienteId);
            } else if (swPedido.isChecked() && !swCliente.isChecked()) {
                vendas = db.vendaDao().getVendasByPedidoId(pedidoId);
            } else if (!swPedido.isChecked() && swCliente.isChecked()) {
                vendas = db.vendaDao().getVendasByClienteId(clienteId);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            addRows();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetDadosDropDowns extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            clientes = db.clienteDao().getClientesDropDown();
            pedidos = db.pedidoDao().getPedidosDropDown();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startSpinners();
        }
    }
}
