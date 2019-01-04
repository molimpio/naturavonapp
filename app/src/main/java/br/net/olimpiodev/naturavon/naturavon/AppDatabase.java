package br.net.olimpiodev.naturavon.naturavon;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import br.net.olimpiodev.naturavon.naturavon.dao.ClienteDao;
import br.net.olimpiodev.naturavon.naturavon.dao.PedidoDao;
import br.net.olimpiodev.naturavon.naturavon.dao.ProdutoDao;
import br.net.olimpiodev.naturavon.naturavon.dao.VendaDao;
import br.net.olimpiodev.naturavon.naturavon.model.Cliente;
import br.net.olimpiodev.naturavon.naturavon.model.Pedido;
import br.net.olimpiodev.naturavon.naturavon.model.Produto;
import br.net.olimpiodev.naturavon.naturavon.model.Venda;

@Database(entities = {
        Cliente.class,
        Pedido.class,
        Venda.class,
        Produto.class
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "naturavon";
    public abstract ClienteDao clienteDao();
    public abstract PedidoDao pedidoDao();
    public abstract VendaDao vendaDao();
    public abstract ProdutoDao produtoDao();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

}
