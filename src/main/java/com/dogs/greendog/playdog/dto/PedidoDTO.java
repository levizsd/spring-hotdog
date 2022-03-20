package com.dogs.greendog.playdog.dto;

import com.dogs.greendog.playdog.domain.Cliente;
import com.dogs.greendog.playdog.domain.Item;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

public class PedidoDTO {

    private Long id;

    @ManyToOne(optional = true)
    private Cliente cliente;

    @ManyToMany
    @Cascade(CascadeType.MERGE)
    private List<Item> itens;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date data;

    @Min(1)
    private Double valorTotal;

    private String status;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, Cliente cliente, List<Item> itens, Double valorTotal, String status) {
        super();
        this.id = id;
        this.cliente = cliente;
        this.itens = itens;
        this.data = new Date();
        this.valorTotal = valorTotal;
        this.status = status;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PedidoDTO other = (PedidoDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Pedido [id=" + id + ", cliente=" + cliente + ", itens=" + itens + ", data=" + data + ", valorTotal="
                + valorTotal + ", status=" + status + "]";
    }

}
