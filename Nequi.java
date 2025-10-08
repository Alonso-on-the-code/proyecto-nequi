package com.mycompany.nequi;


//SECCION PARA IMPORTACIONES (scanner y listas)
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;



public class Nequi {
    
    //  Aqui va la lista, FUERA del main
    private static List<Transaccion> transacciones = new ArrayList<>();
    
    
    
    public static void main(String[] args) {
    
        Scanner sc = new Scanner(System.in);
        
        
        // Crear cajero y algunos clientes/cuentas de ejemplo
        Cajero cajero = new Cajero("Cajero central");
        Cliente c1 = new Cliente("Alonso");
        Cliente c2 = new Cliente("Santiago");

        Cuenta ahorro1 = new CuentaAhorros("AH-1001", 50000, 0.02);
        Cuenta corriente1 = new CuentaCorriente("CC-2001", 20000, 50000);
        Cuenta ahorro2 = new CuentaAhorros("AH-1002", 30000, 0.02);

        c1.agregarCuenta(ahorro1);
        c1.agregarCuenta(corriente1);
        c2.agregarCuenta(ahorro2);

        System.out.println("Probar operaciones basicas y programadas");
        boolean salir = false;
        while (!salir) {
            
            // MENU QUE VA A APARECER EN CONSOLA
            System.out.println("\nNEQUI, UN BANCO A TU MANO:");
            System.out.println("1. Mostrar cuentas cliente 1");
            System.out.println("2. Consignar");
            System.out.println("3. Retirar");
            System.out.println("4. Donar inmediatamente (cliente1 -> cliente2)");
            System.out.println("5. Programar consignacion (cliente2)");
            System.out.println("6. Programar donacion (cliente1 -> cliente2)");
            System.out.println("7. Listar programadas");
            System.out.println("8. Procesar programadas hasta fecha (yyyy-MM-dd)");
            System.out.println("9. Mostrar historial cuenta destino (cliente2 índice 0)");
            System.out.println("10. Generar factura");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");
String op = sc.nextLine();

        switch (op) {
        case "1":
          c1.listarCuentas();
          break;
        
        case "2":
          System.out.print("Monto consignar a cliente1 cuenta 0: ");
          double m2 = Double.parseDouble(sc.nextLine());
          cajero.consignar(c1, 0, m2);
          transacciones.add(new Transaccion("Consignacion", m2));
        break;
        
        case "3":
          System.out.print("Monto retiro cliente1 cuenta 1: ");
          double m3 = Double.parseDouble(sc.nextLine());
          cajero.retirar(c1, 1, m3);
          transacciones.add(new Transaccion("Retiro", m3));
        break;
        
        case "4":
          System.out.print("Monto donacion (cliente1 cuenta 0 -> cliente2 cuenta 0): ");
          double md = Double.parseDouble(sc.nextLine());
          cajero.donar(c1, 0, c2.getCuenta(0), md);
          transacciones.add(new Transaccion("Donacion", md));
        break;
    
        case "5":
          System.out.print("Fecha consignacion (yyyy-MM-dd): ");
          String f5 = sc.nextLine();
          System.out.print("Monto consignacion: ");
          double m5 = Double.parseDouble(sc.nextLine());
          cajero.programarConsignacion(c2, 0, f5, m5);
          transacciones.add(new Transaccion("Consignacion programada", m5));
        break;
    
        case "6":
          System.out.print("Fecha donacion (yyyy-MM-dd): ");
          String f6 = sc.nextLine();
          System.out.print("Monto donacion: ");
          double m6 = Double.parseDouble(sc.nextLine());
          cajero.programarDonacion(c1, 0, c2.getCuenta(0), f6, m6);
          transacciones.add(new Transaccion("Donacion programada", m6));
        break;
    
        case "7":
          cajero.listarProgramadas();
        break;
    
        case "8":
          System.out.print("Procesar hasta fecha (yyyy-MM-dd): ");
          String f8 = sc.nextLine();
          cajero.procesarProgramadasHasta(f8);
        // Aqui tambien se puede registrar si quiere todas las transacciones procesadas
        break;
    
        case "9":
          cajero.mostrarHistorial(c2, 0);
        break;
    
        case "10":
          FacturaPDF.generarFactura(c1, transacciones);
        break;
    
        case "0":
          salir = true;
        break;
    
        default:
          System.out.println("Opcion invalida.");
          }

        }

        sc.close();
        System.out.println("Programa terminado.");
    }
}
