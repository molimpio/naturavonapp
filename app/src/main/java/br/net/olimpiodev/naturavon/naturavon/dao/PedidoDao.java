package br.net.olimpiodev.naturavon.naturavon.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.net.olimpiodev.naturavon.naturavon.model.ChaveValor;
import br.net.olimpiodev.naturavon.naturavon.model.Pedido;

@Dao
public interface PedidoDao {

    @Insert
    void insert(Pedido... pedido);

    @Update
    void update(Pedido... pedido);

    @Query("SELECT * FROM pedido")
    List<Pedido> getPedidos();

    @Query("SELECT p.id AS chave, p.campanha AS valor FROM pedido AS p ORDER BY id DESC")
    List<ChaveValor> getPedidosDropDown();

    @Query("SELECT * FROM pedido ORDER BY id DESC LIMIT 1")
    Pedido getUltimoPedido();

    @Query("SELECT * FROM pedido WHERE sincronizado = :sincronizado ORDER BY id ASC")
    List<Pedido> getPedidosNaoSincronizados(boolean sincronizado);

    @Query("UPDATE pedido SET sincronizado = :sincronizado WHERE id = :pedidoId")
    void atualizarPedidosSincronizados(boolean sincronizado, int pedidoId);
}
