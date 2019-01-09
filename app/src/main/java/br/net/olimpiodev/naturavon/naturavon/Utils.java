package br.net.olimpiodev.naturavon.naturavon;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.net.olimpiodev.naturavon.naturavon.api.SincronizacaoApi;
import br.net.olimpiodev.naturavon.naturavon.model.Cliente;
import br.net.olimpiodev.naturavon.naturavon.model.Pedido;
import br.net.olimpiodev.naturavon.naturavon.model.Produto;
import br.net.olimpiodev.naturavon.naturavon.model.VendaClientePedido;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Utils {

    private static final String URL_BASE = "http://www.olimpiodev.kinghost.net/naturavon/api/";
    @SuppressLint("StaticFieldLeak")
    private static Context contextU;
    private static ProgressDialog dialog;

    public static String getDataNow() {
        Calendar c = Calendar.getInstance();
        Date data = c.getTime();
        Locale brasilLocale = new Locale("pt", "BR");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", brasilLocale);
        return sdf.format(data);
    }

    public static void sincronizar(Context context) {
        contextU = context;
        dialog = new ProgressDialog(context);
        dialog.setTitle("Sincronização");
        dialog.setMessage("Preparando dados para sincronizar");
        dialog.show();

        final AppDatabase db = Room.databaseBuilder(contextU, AppDatabase.class, AppDatabase.DB_NAME).build();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                JSONObject dados = new JSONObject();

                try {
                    List<Cliente> clientes = db.clienteDao().getClientesNaoSincronizados(false);
                    JSONArray arrayClientes = new JSONArray();

                    for (Cliente c: clientes) {
                        JSONObject objCliente = new JSONObject();
                        objCliente.put("id", c.getId());
                        objCliente.put("nome", c.getNome());
                        objCliente.put("referencia", c.getReferencia());
                        objCliente.put("telefone", c.getTelefone());
                        arrayClientes.put(objCliente);
                    }

                    List<Pedido> pedidos = db.pedidoDao().getPedidosNaoSincronizados(false);
                    JSONArray arrayPedidos = new JSONArray();

                    for (Pedido p: pedidos) {
                        JSONObject objPedido = new JSONObject();
                        objPedido.put("id", p.getId());
                        objPedido.put("data", p.getData());
                        objPedido.put("campanha", p.getCampanha());
                        arrayPedidos.put(objPedido);
                    }

                    List<Produto> produtos = db.produtoDao().getProdutosNaoSincronizados(false);
                    JSONArray arrayProdutos = new JSONArray();

                    for (Produto p: produtos) {
                        JSONObject objProduto = new JSONObject();
                        objProduto.put("id", p.getId());
                        objProduto.put("codigo", p.getCodigo());
                        objProduto.put("pagina", p.getPagina());
                        objProduto.put("nome", p.getNome());
                        objProduto.put("valor", p.getValor());
                        arrayProdutos.put(objProduto);
                    }

                    List<VendaClientePedido> vendas = db.vendaDao().getVendasNaoSincronizados(false);
                    JSONArray arrayVendas = new JSONArray();

                    for (VendaClientePedido v: vendas) {
                        JSONObject objVenda = new JSONObject();
                        objVenda.put("id", v.getIdVenda());
                        objVenda.put("codigo", v.getCodigo());
                        objVenda.put("pagina", v.getPagina());
                        objVenda.put("produto", v.getProduto());
                        objVenda.put("qtde", v.getQuantidade());
                        objVenda.put("valor", v.getValor());
                        objVenda.put("total", v.getTotal());
                        objVenda.put("cliente_id", v.getClienteId());
                        objVenda.put("pedido_id", v.getPedidoId());
                        arrayVendas.put(objVenda);
                    }
                    dados.put("clientes", arrayClientes);
                    dados.put("pedidos", arrayPedidos);
                    dados.put("produtos", arrayProdutos);
                    dados.put("vendas", arrayVendas);

                    if (clientes.size() > 0 || pedidos.size() > 0 || produtos.size() > 0 || vendas.size() > 0) {
                        return dados.toString();
                    } else {
                        return "";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String dados) {
                if (dados.isEmpty()) {
                    Toast.makeText(contextU, "Não existem dados a serem sincronizados", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    enviarDados(dados);
                }
            }
        }.execute();
    }

    private static void enviarDados(String dados) {
        dialog.setMessage("Sincronizando dados");
        Retrofit retrofit;
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(Utils.URL_BASE);
        retrofit = builder.build();

        SincronizacaoApi sincronizacaoApi = retrofit.create(SincronizacaoApi.class);

        RequestBody sincronizacaoDados = RequestBody.create(MediaType.parse("application/json"), dados);
        sincronizacaoApi.sincronizar(sincronizacaoDados).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(response.body().string());

                    JSONArray clientesIds = obj.getJSONObject("data").optJSONArray("clientesIds");
                    JSONArray pedidosIds = obj.getJSONObject("data").optJSONArray("pedidosIds");
                    JSONArray produtosIds = obj.getJSONObject("data").optJSONArray("produtosIds");
                    JSONArray vendasIds = obj.getJSONObject("data").optJSONArray("vendasIds");
                    atualizarRegistros(clientesIds, pedidosIds, produtosIds, vendasIds);

                } catch (IOException | JSONException e) {
                    Toast.makeText(contextU, "Erro ao sincronizar dados", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(contextU, "Erro ao conectar servidor", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private static void atualizarRegistros(final JSONArray clientesIds, final JSONArray pedidosIds,
                                           final JSONArray produtosIds, final JSONArray vendasIds) {

        try {
            final AppDatabase db = Room.databaseBuilder(contextU, AppDatabase.class, AppDatabase.DB_NAME).build();
            dialog.setMessage("Atualizando registros");

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        for (int i=0; i < clientesIds.length(); i++) {
                            db.clienteDao().atualizarClientesSincronizados(true, clientesIds.getInt(i));
                        }

                        for (int x=0; x < pedidosIds.length(); x++) {
                            db.pedidoDao().atualizarPedidosSincronizados(true, pedidosIds.getInt(x));
                        }

                        for (int z=0; z < produtosIds.length(); z++) {
                            db.produtoDao().atualizarProdutosSincronizados(true, produtosIds.getInt(z));
                        }

                        for (int w=0; w < vendasIds.length(); w++) {
                            db.vendaDao().atualizarVendasSincronizadas(true, vendasIds.getInt(w));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    dialog.dismiss();
                }
            }.execute();
        } catch (Exception e) {
            Toast.makeText(contextU, "Erro ao atualizar registros", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }
}
