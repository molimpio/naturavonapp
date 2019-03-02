package br.net.olimpiodev.naturavon.naturavon.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.json.JSONArray;

import java.util.List;

import br.net.olimpiodev.naturavon.naturavon.model.ChaveValor;
import br.net.olimpiodev.naturavon.naturavon.model.Cliente;

@Dao
public interface ClienteDao {

    @Insert
    void insert(Cliente... cliente);

    @Update
    void update(Cliente... cliente);

    @Query("SELECT * FROM cliente ORDER BY nome ASC")
    List<Cliente> getClientes();

    @Query("SELECT c.id AS chave, (c.nome || ' - ' || c.referencia) AS valor FROM cliente AS c ORDER BY nome ASC")
    List<ChaveValor> getClientesDropDown();

    @Query("SELECT c.id AS chave, (c.nome || ' - ' || c.referencia) AS valor FROM pedido AS p INNER JOIN venda AS v ON v.pedido_id = p.id" +
            " INNER JOIN cliente AS c ON c.id = v.cliente_id WHERE p.id = :pedidoId GROUP BY c.id ORDER BY nome ASC")
    List<ChaveValor> getClientesDropDownByPedidoId(int pedidoId);

    @Query("SELECT * FROM cliente WHERE sincronizado = :sincronizado ORDER BY id ASC")
    List<Cliente> getClientesNaoSincronizados(boolean sincronizado);

    @Query("UPDATE cliente SET sincronizado = :sincronizado WHERE id = :clienteId")
    void atualizarClientesSincronizados(boolean sincronizado, int clienteId);
}
