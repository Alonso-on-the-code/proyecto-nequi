package com.mycompany.nequi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class Transaccion {
    private String tipo;
    private double monto;
    private String fecha;

    public Transaccion(String tipo, double monto) {
        this.tipo = tipo;
        this.monto = monto;
        // Guardamos la fecha y hora actual formateada
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.fecha = LocalDateTime.now().format(formatter);
    }

    public Transaccion(String tipo, double monto, LocalDate fecha) {
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha.toString();
    }
    
    
    public String getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }

    public String getFecha() {
        return fecha;
    }

    // Metodo añadido para que Cuenta.mostrarHistorial() funcione
    public String getInfo() {
        return tipo + " | $" + monto + " | " + fecha;
    }

    @Override
    public String toString() {
        return getInfo();
    }
}
