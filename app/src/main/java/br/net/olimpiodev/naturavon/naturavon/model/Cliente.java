package br.net.olimpiodev.naturavon.naturavon.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cliente")
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nome;
    private String referencia;
    private String telefone;
    private Boolean sincronizado = false;

    public Cliente() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Boolean sincronizado) {
        this.sincronizado = sincronizado;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", referencia='" + referencia + '\'' +
                ", telefone='" + telefone + '\'' +
                ", sincronizado=" + sincronizado +
                '}';
    }
}
