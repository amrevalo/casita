/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.beans;

import com.google.gson.annotations.Expose;

/**
 *
 * @author a022593391p
 */
public class LineadepedidoBean {
    @Expose
    private Integer id;
    @Expose
    private Integer cantidad;
//    @Expose(serialize = false)
//    private Integer id_producto = 0;    
//    @Expose(deserialize = false)
//    private Integer id_pedido = 0;
    private Integer id_producto;
    private Integer id_pedido; 
    @Expose(deserialize = false)
    private ProductoBean obj_producto = null;
    @Expose(deserialize = false)
    private PedidoBean obj_pedido = null;

    public LineadepedidoBean() {
    }

    
    
    public LineadepedidoBean(Integer id) {
        this.id = id;
    }

    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public ProductoBean getObj_producto() {
        return obj_producto;
    }

    public void setObj_producto(ProductoBean obj_producto) {
        this.obj_producto = obj_producto;
    }

    public PedidoBean getObj_pedido() {
        return obj_pedido;
    }

    public void setObj_pedido(PedidoBean obj_pedido) {
        this.obj_pedido = obj_pedido;
    }

    public Integer getId_producto() {
        return id_producto;
    }

    public void setId_producto(Integer id_producto) {
        this.id_producto = id_producto;
    }

    public Integer getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(Integer id_pedido) {
        this.id_pedido = id_pedido;
    }
    
    
}
