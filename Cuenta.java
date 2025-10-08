package com.mycompany.nequi;

import java.util.ArrayList;
import java.util.List;

// Clase base abstracta
public abstract class Cuenta {
    protected String numeroCuenta;
    protected double saldo;
    protected List<Transaccion> transacciones;

    public Cuenta(String numeroCuenta, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
        this.transacciones = new ArrayList<>();
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    // Retira y retorna true si tuvo éxito
    public boolean retirar(double monto) {
        if (monto <= 0) return false;
        if (saldo >= monto) {
            saldo -= monto;
            transacciones.add(new Transaccion("Retiro", monto));
            return true;
        } else {
            return false;
        }
    }

    // Consigna (deposito)
    public void consignar(double monto) {
        if (monto <= 0) return;
        saldo += monto;
        transacciones.add(new Transaccion("Consignacion", monto));
    }

    public void mostrarHistorial() {
        System.out.println("Historial cuenta " + numeroCuenta + ":");
        for (Transaccion t : transacciones) {
            System.out.println(t.getInfo());
        }
    }

    // Permite agregar manualmente una transaccion (uso por ej. en Cajero)
    public void agregarTransaccion(Transaccion t) {
        transacciones.add(t);
    }
}
