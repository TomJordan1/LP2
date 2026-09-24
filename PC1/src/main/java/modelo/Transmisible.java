package modelo;

// Contrato: el objeto sabe convertirse en una línea de texto para viajar por TCP.
// Cada tipo de mensaje decide sus propios campos (polimorfismo, sin if/else por tipo).
public interface Transmisible {

    // Separador de campos del protocolo de texto
    String SEPARADOR = "|";

    String serializar();
}
