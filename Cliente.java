package com.mycompany.nequi;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    
    private String nombre;
    
    // Lista de cuentas que pertenecen a este cliente (puede tener varias cuentas)
    private List<Cuenta> cuentas;

    // CONSTRUCTOR
    public Cliente(String nombre) {
        this.nombre = nombre;
        this.cuentas = new ArrayList<>();
    }

    
    //METODOS
    
    public String getNombre() {
        return nombre;
    }

    // Agrega una cuenta a la lista de cuentas del cliente
    public void agregarCuenta(Cuenta c) {
        // Se valida que la cuenta no sea null antes de agregarla
        if (c != null) cuentas.add(c);
    }

    // Retorna una cuenta según su índice en la lista
    public Cuenta getCuenta(int indice) {
        // Si el indice es invalido (menor a 0 o mayor al tamaño), retorna null
        if (indice < 0 || indice >= cuentas.size()) return null;
        return cuentas.get(indice);
    }

    // Devuelve la lista completa de cuentas del cliente
    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    // Muestra en consola todas las cuentas del cliente
    public void listarCuentas() {
        System.out.println("Cuentas de " + nombre + ":");
        for (int i = 0; i < cuentas.size(); i++) {
            Cuenta c = cuentas.get(i);
            
            System.out.println(i + " - " + c.getNumeroCuenta() + " | Saldo: " + c.getSaldo());
        }
    }
}
