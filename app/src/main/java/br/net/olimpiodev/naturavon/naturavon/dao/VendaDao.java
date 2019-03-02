package br.net.olimpiodev.naturavon.naturavon.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.widget.ListAdapter;

import java.util.List;

import br.net.olimpiodev.naturavon.naturavon.model.Venda;
import br.net.olimpiodev.naturavon.naturavon.model.VendaClientePedido;

@Dao
public interface VendaDao {

    @Insert
    void insert(Venda... venda);

    @Delete
    void delete(Venda... venda);

    @Query("SELECT * FROM venda WHERE id = :vendaId")
    Venda getVendaById(int vendaId);

    @Query("UPDATE venda SET sincronizado = :sincronizado WHERE id = :vendaId")
    void atualizarVendasSincronizadas(boolean sincronizado, int vendaId);

    @Query("SELECT v.id AS idVenda, v.codigo, v.pagina, v.produto, v.quantidade, v.valor," +
            "v.total, v.cliente_id AS clienteId, v.pedido_id AS pedidoId FROM venda AS v " +
            "INNER JOIN cliente AS c ON c.id == v.cliente_id " +
            "INNER JOIN pedido AS p ON p.id == v.pedido_id " +
            "WHERE v.sincronizado = :sincronizado ORDER BY v.id ASC")
    List<VendaClientePedido> getVendasNaoSincronizados(boolean sincronizado);

    @Query("SELECT v.id AS idVenda, v.codigo, v.pagina, v.produto, v.quantidade, v.valor," +
            "v.total, c.nome AS cliente, p.campanha AS pedido, v.produto, " +
            "v.cliente_id AS clienteId, v.pedido_id AS pedidoId, v.pago FROM venda AS v " +
            "INNER JOIN cliente AS c ON c.id == v.cliente_id " +
            "INNER JOIN pedido AS p ON p.id == v.pedido_id " +
            "WHERE p.id = :pedidoId AND c.id = :clienteId")
    List<VendaClientePedido> getVendas(int pedidoId, int clienteId);

    @Query("SELECT v.id AS idVenda, v.codigo, v.pagina, v.produto, v.quantidade, v.valor," +
            "v.total, c.nome AS cliente, p.campanha AS pedido, v.produto, " +
            "v.cliente_id AS clienteId, v.pedido_id AS pedidoId, v.pago FROM venda AS v " +
            "INNER JOIN cliente AS c ON c.id == v.cliente_id " +
            "INNER JOIN pedido AS p ON p.id == v.pedido_id " +
            "WHERE p.id = :pedidoId ORDER BY c.nome ASC")
    List<VendaClientePedido> getVendasByPedidoId(int pedidoId);

    @Query("SELECT v.id AS idVenda, v.codigo, v.pagina, v.produto, v.quantidade, v.valor," +
            "v.total, c.nome AS cliente, p.campanha AS pedido, v.produto, " +
            "v.cliente_id AS clienteId, v.pedido_id AS pedidoId, v.pago FROM venda AS v " +
            "INNER JOIN cliente AS c ON c.id == v.cliente_id " +
            "INNER JOIN pedido AS p ON p.id == v.pedido_id " +
            "WHERE c.id = :clienteId ORDER BY p.id ASC")
    List<VendaClientePedido> getVendasByClienteId(int clienteId);

    @Query("UPDATE venda SET pago = 1 where id = :vendaId")
    void baixarVendaById(int vendaId);

    @Query("SELECT (c.nome || ' RS ' || sum(v.total)) AS total FROM pedido AS p INNER JOIN venda AS v ON v.pedido_id = p.id INNER JOIN cliente AS c ON c.id = v.cliente_id  WHERE p.id = :pedidoId AND v.pago = 0 GROUP BY c.id ORDER BY nome ASC")
    List<String> getClienteNaoPagaram(int pedidoId);


}
