package TareaAbstractFactory;

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
