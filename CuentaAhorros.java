package com.mycompany.nequi;

public class CuentaAhorros extends Cuenta {
    private double tasaInteres; // por ejemplo 0.02 = 2%

    public CuentaAhorros(String numeroCuenta, double saldoInicial, double tasaInteres) {
        super(numeroCuenta, saldoInicial);
        this.tasaInteres = tasaInteres;
    }

    public double getTasaInteres() {
        return tasaInteres;
    }

    // ejemplo simple: aplicar interes (no requerido por la tarea pero util)
    public void aplicarInteres() {
        double interes = saldo * tasaInteres;
        consignar(interes);
        agregarTransaccion(new Transaccion("Interes aplicado", interes));
    }
}
