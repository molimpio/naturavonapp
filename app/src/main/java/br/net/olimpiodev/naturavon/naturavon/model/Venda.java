package br.net.olimpiodev.naturavon.naturavon.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "venda",
        foreignKeys = {
                @ForeignKey(
                        entity = Cliente.class,
                        parentColumns = "id",
                        childColumns = "cliente_id"
                ),
                @ForeignKey(
                    entity = Pedido.class,
                    parentColumns = "id",
                    childColumns = "pedido_id"
                )
        }
)
public class Venda implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String codigo;
    private int pagina;
    private String produto;
    private int quantidade;
    private Double valor;
    private Double total;
    private Boolean pago = false;
    private Boolean sincronizado = false;

    @ColumnInfo(name = "cliente_id")
    private int clienteId;

    @ColumnInfo(name = "pedido_id")
    private int pedidoId;

    public Venda() { }

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

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
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
                ", pagina=" + pagina +
                ", produto='" + produto + '\'' +
                ", quantidade=" + quantidade +
                ", valor=" + valor +
                ", total=" + total +
                ", pago=" + pago +
                ", sincronizado=" + sincronizado +
                ", clienteId=" + clienteId +
                ", pedidoId=" + pedidoId +
                '}';
    }
}
