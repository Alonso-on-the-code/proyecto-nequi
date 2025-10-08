package com.mycompany.nequi;

// Librerias de iText7 necesarias para crear PDFs
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileNotFoundException;
import java.util.List;

public class FacturaPDF {

    
    public static void generarFactura(Cliente cliente, List<Transaccion> transacciones) {
        try {
            // El archivo PDF se llamara "Factura_NombreCliente.pdf"
            String nombreArchivo = "Factura_" + cliente.getNombre() + ".pdf";

            // PdfWriter se encarga de escribir el contenido en el archivo
            PdfWriter writer = new PdfWriter(nombreArchivo);

            // PdfDocument representa el archivo PDF como tal
            PdfDocument pdf = new PdfDocument(writer);

            try ( // Document es la interfaz de alto nivel para agregar contenido al PDF
                    Document document = new Document(pdf)) {
                document.add(new Paragraph("=== FACTURA DE OPERACIONES NEQUI ===\n\n"));
                document.add(new Paragraph("Cliente: " + cliente.getNombre()));
                document.add(new Paragraph("--------------------------------------\n"));
                // SI EL CLIENTE NO TIENE TRANSACCIONE MUESTRA ESTE AVISO
                if (transacciones.isEmpty()) {
                    document.add(new Paragraph("No se han registrado operaciones.\n"));
                } else {
                    // SI TIENE TRANSACCIONES SE CITAN TODAS EN EL PDF
                    for (Transaccion t : transacciones) {
                        document.add(new Paragraph(
                                "Operacion: " + t.getTipo() +
                                        " | Monto: $" + t.getMonto() +
                                        " | Fecha: " + t.getFecha()
                        ));
                    }
                }   document.add(new Paragraph("\n--------------------------------------"));
                document.add(new Paragraph("Gracias por usar Nequi\n"));
                // CERRAMOS EL DOCUMENTO PARA GUARDAR LOS DATOS EN EL ARCHIVO PDF
            }

            
            System.out.println("Factura generada exitosamente: " + nombreArchivo);

        } catch (FileNotFoundException e) {
        }
    }
}
