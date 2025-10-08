package com.mycompany.nequi;

//IMPORTO LISTAS Y DEMAS LIBRERIAS QUE ME AYUDEN A HACER EL PROGRAMA
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//CREO CLASE CAJERO
public class Cajero {
    private String ubicacion;
    private List<ScheduledTransaction> programadas;
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //CONSTRUCTOR
    public Cajero(String ubicacion) {
        this.ubicacion = ubicacion;
        this.programadas = new ArrayList<>();
    }

    /* Operaciones basicas */
    public boolean retirar(Cliente cliente, int cuentaIndex, double monto) {
        if (cliente == null || monto <= 0) return false;
        Cuenta c = cliente.getCuenta(cuentaIndex);
        if (c == null) return false;
        boolean ok = c.retirar(monto);
        if (!ok) System.out.println("No se pudo retirar: fondos insuficientes o monto inválido.");
        return ok;
    }

    public void consignar(Cliente cliente, int cuentaIndex, double monto) {
        if (cliente == null) {
            System.out.println("Cliente invalido.");
            return;
        }
        if (monto <= 0) {
            System.out.println("Monto inválido para consignación.");
            return;
        }
        Cuenta c = cliente.getCuenta(cuentaIndex);
        if (c == null) {
            System.out.println("Cuenta no encontrada.");
            return;
        }
        c.consignar(monto);
    }

    public void mostrarSaldo(Cliente cliente, int cuentaIndex) {
        Cuenta c = cliente == null ? null : cliente.getCuenta(cuentaIndex);
        if (c == null) {
            System.out.println("Cuenta no encontrada.");
            return;
        }
        System.out.println("Saldo: " + c.getSaldo());
    }

    public void mostrarHistorial(Cliente cliente, int cuentaIndex) {
        Cuenta c = cliente == null ? null : cliente.getCuenta(cuentaIndex);
        if (c == null) {
            System.out.println("Cuenta no encontrada.");
            return;
        }
        c.mostrarHistorial();
    }

    /* Donación inmediata: intentar retirar de origen y consignar en destino */
    public void donar(Cliente donante, int cuentaIndexDonante, Cuenta destinatario, double monto) {
        if (donante == null || destinatario == null) {
            System.out.println("Donante o destinatario inválido.");
            return;
        }
        if (monto <= 0) {
            System.out.println("Monto inválido para donación.");
            return;
        }
        Cuenta origen = donante.getCuenta(cuentaIndexDonante);
        if (origen == null) {
            System.out.println("Cuenta donante no encontrada.");
            return;
        }
        boolean retirado = origen.retirar(monto);
        if (retirado) {
            destinatario.consignar(monto);
            origen.agregarTransaccion(new Transaccion("Donación realizada", monto));
            destinatario.agregarTransaccion(new Transaccion("Donación recibida", monto));
            System.out.println("Donación de " + monto + " realizada correctamente.");
        } else {
            System.out.println("No fue posible realizar la donación (fondos insuficientes).");
        }
    }

    /* Programacion de consignacion (deposito) desde "externo" */
    public void programarConsignacion(Cliente beneficiario, int cuentaIndex, String fechaStr, double monto) {
        if (beneficiario == null) {
            System.out.println("Beneficiario invalido.");
            return;
        }
        if (monto <= 0) {
            System.out.println("Monto inválido para consignación programada.");
            return;
        }
        Cuenta destino = beneficiario.getCuenta(cuentaIndex);
        if (destino == null) {
            System.out.println("Cuenta destino no encontrada.");
            return;
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr, FORMAT);
        } catch (Exception e) {
            System.out.println("Fecha invalida (use yyyy-MM-dd).");
            return;
        }
        programadas.add(new ScheduledTransaction(fecha, null, destino, monto, ScheduledTransaction.Type.CONSIGNACION));
        System.out.println("Consignación programada para " + fecha + " por " + monto);
    }

    /* Programacion de donacion desde una cuenta origen hacia un destino en una fecha */
    public void programarDonacion(Cliente donante, int cuentaIndex, Cuenta destinatario, String fechaStr, double monto) {
        if (donante == null || destinatario == null) {
            System.out.println("Donante o destinatario inválido.");
            return;
        }
        if (monto <= 0) {
            System.out.println("Monto inválido para donación programada.");
            return;
        }
        Cuenta origen = donante.getCuenta(cuentaIndex);
        if (origen == null) {
            System.out.println("Cuenta origen no encontrada.");
            return;
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr, FORMAT);
        } catch (Exception e) {
            System.out.println("Fecha inválida (use yyyy-MM-dd).");
            return;
        }
        programadas.add(new ScheduledTransaction(fecha, origen, destinatario, monto, ScheduledTransaction.Type.DONACION));
        System.out.println("Donación programada para " + fecha + " por " + monto);
    }

    /* Procesa todas las transacciones programadas con fecha <= fecha indicada (inclusive) */
    public void procesarProgramadasHasta(String fechaStr) {
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr, FORMAT);
        } catch (Exception e) {
            System.out.println("Fecha inválida (use yyyy-MM-dd).");
            return;
        }
        procesarProgramadasHasta(fecha);
    }

    public void procesarProgramadasHasta(LocalDate fecha) {
        if (programadas.isEmpty()) {
            System.out.println("No hay transacciones programadas.");
            return;
        }
        Iterator<ScheduledTransaction> it = programadas.iterator();
        int procesadas = 0;
        while (it.hasNext()) {
            ScheduledTransaction st = it.next();
            if (!st.fecha.isAfter(fecha)) { // st.fecha <= fecha
                if (st.tipo == ScheduledTransaction.Type.CONSIGNACION) {
                    st.destino.consignar(st.monto);
                    st.destino.agregarTransaccion(new Transaccion("Consignación programada", st.monto, st.fecha));
                    System.out.println("Procesada consignación programada a " + st.destino.getNumeroCuenta() + " por " + st.monto + " (fecha: " + st.fecha + ")");
                    procesadas++;
                    it.remove();
                } else if (st.tipo == ScheduledTransaction.Type.DONACION) {
                    boolean ok = st.origen.retirar(st.monto);
                    if (ok) {
                        st.destino.consignar(st.monto);
                        st.origen.agregarTransaccion(new Transaccion("Donación programada realizada", st.monto, st.fecha));
                        st.destino.agregarTransaccion(new Transaccion("Donación programada recibida", st.monto, st.fecha));
                        System.out.println("Procesada donación programada: " + st.monto + " de " + st.origen.getNumeroCuenta() + " a " + st.destino.getNumeroCuenta() + " (fecha: " + st.fecha + ")");
                        procesadas++;
                        it.remove();
                    } else {
                        System.out.println("No se procesó donación programada (fondos insuficientes) de " + st.origen.getNumeroCuenta() + " (fecha programada: " + st.fecha + ")");
                        // la dejamos en la lista para intentar más tarde
                    }
                }
            }
        }
        if (procesadas == 0) {
            System.out.println("No se procesaron transacciones programadas en la fecha indicada.");
        }
    }

    public void listarProgramadas() {
        if (programadas.isEmpty()) {
            System.out.println("No hay transacciones programadas.");
            return;
        }
        System.out.println("Transacciones programadas:");
        for (int i = 0; i < programadas.size(); i++) {
            System.out.println(i + " - " + programadas.get(i));
        }
    }

    /* Clase interna para representar programadas */
    private static class ScheduledTransaction {
        enum Type { CONSIGNACION, DONACION }
        LocalDate fecha;
        Cuenta origen; // null si la consignación viene "desde fuera"
        Cuenta destino;
        double monto;
        Type tipo;

        ScheduledTransaction(LocalDate fecha, Cuenta origen, Cuenta destino, double monto, Type tipo) {
            this.fecha = fecha;
            this.origen = origen;
            this.destino = destino;
            this.monto = monto;
            this.tipo = tipo;
        }

        @Override
        public String toString() {
            String sTipo = tipo == Type.CONSIGNACION ? "Consignación programada" : "Donación programada";
            String origenStr = (origen == null) ? "externa" : origen.getNumeroCuenta();
            return sTipo + " | Fecha: " + fecha + " | Monto: " + monto + " | Origen: " + origenStr + " | Destino: " + destino.getNumeroCuenta();
        }
    }
}
