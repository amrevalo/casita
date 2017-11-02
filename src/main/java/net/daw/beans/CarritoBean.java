/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.beans;

import com.google.gson.annotations.Expose;

/**
 *
 * @author alumno
 */
public class CarritoBean {
    
    private int cantidad;
    private ProductoBean oProducto = null;

    public CarritoBean() {
    }

    
    
    
    
    
    public int getCantidad() {
        return cantidad;
    }

    public CarritoBean(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public ProductoBean getoProducto() {
        return oProducto;
    }

    public void setoProducto(ProductoBean oProducto) {
        this.oProducto = oProducto;
    }

    
    
    
    
    
}
