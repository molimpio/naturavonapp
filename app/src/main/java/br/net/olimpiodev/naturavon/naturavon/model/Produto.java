package br.net.olimpiodev.naturavon.naturavon.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "produto")
public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String codigo;
    private int pagina;
    private String nome;
    private Double valor;
    private Boolean sincronizado = false;

    public Produto() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
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
                ", codigo='" + codigo + '\'' +
                ", pagina='" + pagina + '\'' +
                ", nome='" + nome + '\'' +
                ", valor=" + valor +
                ", sincronizado=" + sincronizado +
                '}';
    }
}
