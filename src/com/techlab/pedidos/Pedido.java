package com.techlab.pedidos;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private static int contadorPedido = 1;
    private int id;
    private List<LineaPedido> lineas;

    public Pedido() {
        this.id = contadorPedido++;
        this.lineas = new ArrayList<>();
    }

    public void agregarLinea(LineaPedido linea) {
        lineas.add(linea);
    }

    public double calcularTotal() {
        double total = 0;
        for (LineaPedido linea : lineas) {
            total += linea.getSubtotal();
        }
        return total;
    }

    public void mostrarResumen() {
        System.out.println("--- Pedido #" + id + " ---");
        for (LineaPedido linea : lineas) {
            System.out.println("- " + linea.getProducto().getNombre() + 
                               " x" + linea.getCantidad() + 
                               " (Subtotal: $" + linea.getSubtotal() + ")");
        }
        System.out.println("Total a pagar: $" + calcularTotal());
        System.out.println("--------------------");
    }
}