package com.techlab.main;

import com.techlab.excepciones.StockInsuficienteException;
import com.techlab.pedidos.LineaPedido;
import com.techlab.pedidos.Pedido;
import com.techlab.productos.Bebida;
import com.techlab.productos.Comida;
import com.techlab.productos.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Producto> inventario = new ArrayList<>();
    private static List<Pedido> historialPedidos = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Agregamos un par de productos de prueba
        inventario.add(new Bebida("Café Premium", 2500.50, 50, 1.0));
        inventario.add(new Comida("Alfajor de Maicena", 800.00, 120, true));

        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== SISTEMA DE GESTIÓN ===");
            System.out.println("1. Agregar Producto");
            System.out.println("2. Listar Productos");
            System.out.println("3. Buscar/Actualizar Producto");
            System.out.println("4. Eliminar Producto");
            System.out.println("5. Crear un Pedido");
            System.out.println("6. Listar Pedidos");
            System.out.println("7. Salir");
            System.out.print("Elegí una opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        agregarProducto();
                        break;
                    case 2:
                        listarProductos();
                        break;
                    case 3:
                        actualizarProducto();
                        break;
                    case 4:
                        eliminarProducto();
                        break;
                    case 5:
                        crearPedido();
                        break;
                    case 6:
                        listarPedidos();
                        break;
                    case 7:
                        salir = true;
                        System.out.println("¡Nos vemos! Cerrando sistema...");
                        break;
                    default:
                        System.out.println("Opción inválida. Intentá de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingresá un número válido.");
            }
        }
        scanner.close();
    }

    private static void agregarProducto() {
        try {
            System.out.println("Tipo de producto? (1: Bebida, 2: Comida, 3: Generico): ");
            int tipo = Integer.parseInt(scanner.nextLine());

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Precio: ");
            double precio = Double.parseDouble(scanner.nextLine());
            System.out.print("Stock Inicial: ");
            int stock = Integer.parseInt(scanner.nextLine());

            if (tipo == 1) {
                System.out.print("Litros: ");
                double litros = Double.parseDouble(scanner.nextLine());
                inventario.add(new Bebida(nombre, precio, stock, litros));
            } else if (tipo == 2) {
                System.out.print("Es perecedero? (true/false): ");
                boolean perecedero = Boolean.parseBoolean(scanner.nextLine());
                inventario.add(new Comida(nombre, precio, stock, perecedero));
            } else {
                inventario.add(new Producto(nombre, precio, stock));
            }
            System.out.println("¡Producto agregado con éxito!");
        } catch (NumberFormatException e) {
            System.out.println("Error en el formato de los datos ingresados. Operación cancelada.");
        }
    }

    private static void listarProductos() {
        if (inventario.isEmpty()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        System.out.println("\n--- Lista de Productos ---");
        // Acá se usa polimorfismo: cada objeto llama a su propio mostrarDetalles()
        for (Producto p : inventario) {
            System.out.println(p.mostrarDetalles());
        }
    }

    private static Producto buscarPorId(int id) {
        for (Producto p : inventario) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private static void actualizarProducto() {
        try {
            System.out.print("Ingresá el ID del producto a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Producto p = buscarPorId(id);

            if (p != null) {
                System.out.println("Producto encontrado: " + p.getNombre());
                System.out.print("Nuevo precio (dejá en 0 para no cambiar): ");
                double precio = Double.parseDouble(scanner.nextLine());
                if (precio > 0) p.setPrecio(precio);

                System.out.print("Stock a sumar/restar (ej: 10 o -5. Dejá en 0 para no cambiar): ");
                int ajusteStock = Integer.parseInt(scanner.nextLine());
                int nuevoStock = p.getStock() + ajusteStock;

                if (nuevoStock >= 0) {
                    p.setStock(nuevoStock);
                    System.out.println("¡Producto actualizado!");
                } else {
                    System.out.println("Error: El stock no puede quedar en negativo.");
                }
            } else {
                System.out.println("Producto no encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Formato numérico inválido.");
        }
    }

    private static void eliminarProducto() {
        try {
            System.out.print("Ingresá el ID del producto a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Producto p = buscarPorId(id);

            if (p != null) {
                inventario.remove(p);
                System.out.println("Producto '" + p.getNombre() + "' eliminado.");
            } else {
                System.out.println("Producto no encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingresá un ID válido.");
        }
    }

    private static void crearPedido() {
        Pedido nuevoPedido = new Pedido();
        boolean agregando = true;

        System.out.println("\n--- Nuevo Pedido ---");
        listarProductos();

        while (agregando) {
            try {
                System.out.print("\nID del producto a agregar (o 0 para terminar): ");
                int id = Integer.parseInt(scanner.nextLine());

                if (id == 0) {
                    agregando = false;
                    continue;
                }

                Producto p = buscarPorId(id);
                if (p == null) {
                    System.out.println("ID no encontrado.");
                    continue;
                }

                System.out.print("Cantidad deseada: ");
                int cantidad = Integer.parseInt(scanner.nextLine());

                // Validación de stock lanzando nuestra excepción personalizada
                if (cantidad > p.getStock()) {
                    throw new StockInsuficienteException("No hay stock suficiente. Stock actual: " + p.getStock());
                }

                // Descontamos stock y agregamos al pedido
                p.setStock(p.getStock() - cantidad);
                nuevoPedido.agregarLinea(new LineaPedido(p, cantidad));
                System.out.println("Agregado al pedido.");

            } catch (NumberFormatException e) {
                System.out.println("Error: Ingresá un número válido.");
            } catch (StockInsuficienteException e) {
                System.out.println("Excepción: " + e.getMessage());
            }
        }

        if (nuevoPedido.calcularTotal() > 0) {
            historialPedidos.add(nuevoPedido);
            System.out.println("\n¡Pedido confirmado!");
            nuevoPedido.mostrarResumen();
        } else {
            System.out.println("Pedido cancelado o vacío.");
        }
    }

    private static void listarPedidos() {
        if (historialPedidos.isEmpty()) {
            System.out.println("Aún no hay pedidos registrados.");
            return;
        }
        for (Pedido p : historialPedidos) {
            p.mostrarResumen();
        }
    }
}