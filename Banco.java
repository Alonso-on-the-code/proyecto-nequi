package com.mycompany.nequi;

import java.util.ArrayList;
import java.util.List;

//CREO LA CLASE BANCO
public class Banco {
    private String nombre;
    private List<Cliente> clientes;


//ATRIBUTOS
    public Banco(String nombre) {
        this.nombre = nombre;
        this.clientes = new ArrayList<>();
    }
 

//METODOS
    public String getNombre() {
        return nombre;
    }

    public void agregarCliente(Cliente cliente) {
        if (cliente != null) clientes.add(cliente);
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}
