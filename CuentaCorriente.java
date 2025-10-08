package com.mycompany.nequi;

//Se presencia la herencia de la clase cuenta a la clase cuenta corriente
public class CuentaCorriente extends Cuenta {
    private double limiteDescubierto; // permitido (p. ej. -100000)

    public CuentaCorriente(String numeroCuenta, double saldoInicial, double limiteDescubierto) {
        super(numeroCuenta, saldoInicial);
        this.limiteDescubierto = limiteDescubierto; // atributo caracteristico de la clase cuenta corriente
    }

    @Override
    public boolean retirar(double monto) {
        if (monto <= 0) return false;
        if (saldo - monto >= -limiteDescubierto) {
            saldo -= monto;
            transacciones.add(new Transaccion("Retiro", monto));
            return true;
        } else {
            return false;
        }
    }

    public double getLimiteDescubierto() {
        return limiteDescubierto;
    }
}
