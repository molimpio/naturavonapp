package br.net.olimpiodev.naturavon.naturavon.view;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import br.net.olimpiodev.naturavon.naturavon.AppDatabase;
import br.net.olimpiodev.naturavon.naturavon.R;
import br.net.olimpiodev.naturavon.naturavon.model.ChaveValor;
import br.net.olimpiodev.naturavon.naturavon.model.Produto;
import br.net.olimpiodev.naturavon.naturavon.model.Venda;

public class VendaCadastroActivity extends AppCompatActivity {

    private AppDatabase db;
    private TextInputLayout tilCodigo, tilPagina, tilProduto, tilQtde, tilValor, tilTotal;
    private EditText etCodigo, etPagina, etProduto, etQtde, etValor, etTotal;
    private Spinner spCliente, spPedido;
    private List<ChaveValor> clienteList;
    private List<ChaveValor> pedidoList;
    private Venda venda;
    private int produtoId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_cadastro);

        getSupportActionBar().setTitle("Cadastro Venda");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        tilCodigo = findViewById(R.id.til_codigo_venda);
        tilPagina = findViewById(R.id.til_pagina_venda);
        tilProduto = findViewById(R.id.til_produto_venda);
        tilQtde = findViewById(R.id.til_qtde_venda);
        tilValor = findViewById(R.id.til_valor_venda);
        tilTotal = findViewById(R.id.til_total_venda);

        etCodigo = findViewById(R.id.et_codigo_venda);
        etPagina = findViewById(R.id.et_pagina_venda);
        etProduto = findViewById(R.id.et_produto_venda);
        etQtde = findViewById(R.id.et_qtde_venda);
        etValor = findViewById(R.id.et_valor_venda);
        etTotal = findViewById(R.id.et_total_venda);

        etCodigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!etCodigo.getText().toString().isEmpty()) {
                    String codigo = etCodigo.getText().toString().trim().toUpperCase();
                    venda.setCodigo(codigo);
                    buscarProduto();
                }
            }
        });

        spCliente = findViewById(R.id.sp_cliente_venda);
        spCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                venda.setClienteId(clienteList.get(position).getChave());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPedido = findViewById(R.id.sp_pedido_venda);
        spPedido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                venda.setPedidoId(pedidoList.get(position).getChave());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btCadastrarVenda = findViewById(R.id.btCadastrarVenda);
        btCadastrarVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarVenda();
            }
        });

        Button btCalcularVenda = findViewById(R.id.btCalcularVenda);
        btCalcularVenda.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                if (!etQtde.getText().toString().isEmpty() && !etValor.getText().toString().isEmpty()) {
                    int qtde = Integer.parseInt(etQtde.getText().toString().trim());
                    double valor = Double.parseDouble(etValor.getText().toString().trim());
                    double total = qtde * valor;
                    etTotal.setText(String.format("%.2f", total));
                }
            }
        });

        GetClientes getClientes = new GetClientes();
        getClientes.execute();

        GetPedidos getPedidos = new GetPedidos();
        getPedidos.execute();

        venda = new Venda();
        etQtde.setText("1");
    }

    private void startSpinnerCliente(List<ChaveValor> clientes) {
        ArrayAdapter<ChaveValor> adapterClientes = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, clientes);
        spCliente.setAdapter(adapterClientes);
    }

    private void startSpinnerPedido(List<ChaveValor> pedidos) {
        ArrayAdapter<ChaveValor> adapterPedidos = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, pedidos);
        spPedido.setAdapter(adapterPedidos);
    }

    @SuppressLint("StaticFieldLeak")
    private void buscarProduto() {
        new AsyncTask<Void, Void, Produto>() {
            @Override
            protected Produto doInBackground(Void... voids) {
                return db.produtoDao().getProdutoByCodigo(venda.getCodigo());
            }

            @Override
            protected void onPostExecute(Produto produto) {
                if (produto != null) {
                    produtoId = produto.getId();
                    etProduto.setText(produto.getNome());
                    etValor.setText(String.valueOf(produto.getValor()));
                    etPagina.setText(String.valueOf(produto.getPagina()));
                } else {
                Toast.makeText(getApplicationContext(), "Produto não encontrado",
                        Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void cadastrarVenda() {
        String codigo = etCodigo.getText().toString().trim().toUpperCase();
        String pagina = etPagina.getText().toString().trim().toUpperCase();
        String produto = etProduto.getText().toString().toUpperCase();
        String qtde = etQtde.getText().toString().trim();
        String valor = etValor.getText().toString().trim();
        String total = etTotal.getText().toString().trim();

        if (validar(codigo, pagina, produto, qtde, valor, total)) {
            venda.setCodigo(codigo);
            venda.setPagina(Integer.parseInt(pagina));
            venda.setProduto(produto);
            venda.setQuantidade(Integer.parseInt(qtde));
            venda.setValor(Double.parseDouble(valor));
            String totalPonto = total.replace(",", ".");
            venda.setTotal(Double.parseDouble(totalPonto));

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    db.vendaDao().insert(venda);
                    if (produtoId == 0) {
                        Produto p = new Produto();
                        p.setCodigo(venda.getCodigo());
                        p.setNome(venda.getProduto());
                        p.setValor(venda.getValor());
                        p.setPagina(venda.getPagina());
                        db.produtoDao().insert(p);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Venda cadastrada com sucesso!",
                            Toast.LENGTH_LONG).show();
                    limparCampos();
                }
            }.execute();
        }
    }

    private void limparCampos() {
        etCodigo.setText("");
        etPagina.setText("");
        etProduto.setText("");
        etQtde.setText("1");
        etValor.setText("");
        etTotal.setText("");
    }

    private boolean validar(String codigo, String pagina, String produto, String qtde, String valor, String total) {
        if (codigo.isEmpty()) {
            tilCodigo.setErrorEnabled(true);
            tilCodigo.setError("Codigo é obrigatório");
            return false;
        } else if (pagina.isEmpty()) {
            tilPagina.setErrorEnabled(true);
            tilPagina.setError("Pagina é obrigatório");
            return false;
        } else if (produto.isEmpty()) {
            tilProduto.setErrorEnabled(true);
            tilProduto.setError("Nome do produto é obrigatório");
            return false;
        } else if (qtde.isEmpty()) {
            tilQtde.setErrorEnabled(true);
            tilQtde.setError("Quantidade é obrigatório");
            return false;
        } else if (valor.isEmpty()) {
            tilValor.setErrorEnabled(true);
            tilValor.setError("Valor é obrigatório");
            return false;
        } else if (total.isEmpty()) {
            tilTotal.setErrorEnabled(true);
            tilTotal.setError("Total é obrigatório");
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetClientes extends AsyncTask<Void, Void, List<ChaveValor>> {

        @Override
        protected List<ChaveValor> doInBackground(Void... voids) {
            return db.clienteDao().getClientesDropDown();
        }

        @Override
        protected void onPostExecute(List<ChaveValor> clientes) {
            clienteList = clientes;
            startSpinnerCliente(clientes);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetPedidos extends AsyncTask<Void, Void, List<ChaveValor>> {

        @Override
        protected List<ChaveValor> doInBackground(Void... voids) {
            return db.pedidoDao().getPedidosDropDown();
        }

        @Override
        protected void onPostExecute(List<ChaveValor> pedidos) {
            pedidoList = pedidos;
            startSpinnerPedido(pedidos);
        }
    }
}
