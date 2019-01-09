package br.net.olimpiodev.naturavon.naturavon.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "pedido")
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String data;
    private String campanha;
    private Boolean sincronizado = false;

    public Pedido() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCampanha() {
        return campanha;
    }

    public void setCampanha(String campanha) {
        this.campanha = campanha;
    }

    public Boolean getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Boolean sincronizado) {
        this.sincronizado = sincronizado;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", campanha='" + campanha + '\'' +
                ", sincronizado=" + sincronizado +
                '}';
    }
}
