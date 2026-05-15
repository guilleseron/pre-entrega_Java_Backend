package com.techlab.productos;

public class Bebida extends Producto {
    private double litros;

    public Bebida(String nombre, double precio, int stock, double litros) {
        super(nombre, precio, stock);
        this.litros = litros;
    }

    @Override
    public String mostrarDetalles() {
        return super.mostrarDetalles() + " | Volumen: " + litros + "L";
    }
}
