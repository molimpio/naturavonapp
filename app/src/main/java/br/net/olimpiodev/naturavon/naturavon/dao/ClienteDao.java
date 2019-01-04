package br.net.olimpiodev.naturavon.naturavon.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Query("SELECT c.id AS chave, c.nome AS valor FROM cliente AS c ORDER BY nome ASC")
    List<ChaveValor> getClientesDropDown();
}
