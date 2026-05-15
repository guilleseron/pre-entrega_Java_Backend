package com.techlab.productos;

public class Comida extends Producto {
    private boolean esPerecedero;

    public Comida(String nombre, double precio, int stock, boolean esPerecedero) {
        super(nombre, precio, stock);
        this.esPerecedero = esPerecedero;
    }

    @Override
    public String mostrarDetalles() {
        String perecedero = esPerecedero ? "Sí" : "No";
        return super.mostrarDetalles() + " | Perecedero: " + perecedero;
    }
}
