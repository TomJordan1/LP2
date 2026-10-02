package pe.edu.uni.menu.cliente;

import pe.edu.uni.menu.fabrica.FactoryMenu;
import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

public class Restaurante {
    public void servir(FactoryMenu Factory){
        Entrada entrada=Factory.crearEntrada();
        Bebida bebida =Factory.crearBebida();
        Fondo fondo=Factory.crearFondo();
        System.out.println(entrada.servir());
        System.out.println(fondo.servir());
        System.out.println(bebida.servir());
    }
}
