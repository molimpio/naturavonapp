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
}
