package br.net.olimpiodev.naturavon.naturavon.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import br.net.olimpiodev.naturavon.naturavon.model.Produto;

@Dao
public interface ProdutoDao {

    @Insert
    void insert(Produto... produto);

    @Query("SELECT * FROM produto WHERE codigo = :codigo")
    Produto getProdutoByCodigo(String codigo);

    @Query("SELECT count(id) FROM produto")
    Integer getQtdeProdutos();
}
