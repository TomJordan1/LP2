package pe.edu.uni.menu.modelo;

public interface ItemMenu {

    String nombre();

    double precio();

    // cada item sabe describirse segun el nivel en que se encuentra dentro del arbol
    String describir(int nivel);
}
