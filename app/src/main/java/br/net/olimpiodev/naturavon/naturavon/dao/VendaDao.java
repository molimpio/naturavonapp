package br.net.olimpiodev.naturavon.naturavon.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.net.olimpiodev.naturavon.naturavon.model.Venda;
import br.net.olimpiodev.naturavon.naturavon.model.VendaClientePedido;

@Dao
public interface VendaDao {

    @Insert
    void insert(Venda... venda);

    @Update
    void update(Venda... venda);

    @Query("SELECT v.id AS idVenda, v.codigo, v.pagina, v.produto, v.quantidade, v.valor," +
            "v.total, c.nome AS cliente, p.campanha AS pedido, v.produto, " +
            "v.cliente_id AS clienteId, v.pedido_id AS pedidoId FROM venda AS v " +
            "INNER JOIN cliente AS c ON c.id == v.cliente_id " +
            "INNER JOIN pedido AS p ON p.id == v.pedido_id " +
            "WHERE p.id = :pedidoId AND c.id = :clienteId")
    List<VendaClientePedido> getVendas(int pedidoId, int clienteId);

    @Query("SELECT v.id AS idVenda, v.codigo, v.pagina, v.produto, v.quantidade, v.valor," +
            "v.total, c.nome AS cliente, p.campanha AS pedido, v.produto, " +
            "v.cliente_id AS clienteId, v.pedido_id AS pedidoId FROM venda AS v " +
            "INNER JOIN cliente AS c ON c.id == v.cliente_id " +
            "INNER JOIN pedido AS p ON p.id == v.pedido_id " +
            "WHERE p.id = :pedidoId ORDER BY c.nome ASC")
    List<VendaClientePedido> getVendasByPedidoId(int pedidoId);

    @Query("SELECT v.id AS idVenda, v.codigo, v.pagina, v.produto, v.quantidade, v.valor," +
            "v.total, c.nome AS cliente, p.campanha AS pedido, v.produto, " +
            "v.cliente_id AS clienteId, v.pedido_id AS pedidoId FROM venda AS v " +
            "INNER JOIN cliente AS c ON c.id == v.cliente_id " +
            "INNER JOIN pedido AS p ON p.id == v.pedido_id " +
            "WHERE c.id = :clienteId ORDER BY p.id ASC")
    List<VendaClientePedido> getVendasByClienteId(int clienteId);
}