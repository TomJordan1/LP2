package TareaAbstractFactory;

public class Main {
    public static void main(String[] args) {
        Restaurante restaurante = new Restaurante();
        System.out.println("Cliente 1 prefiere Menu Criollo");
        restaurante.servir(new FactoryCriollo());

        System.out.println("Cliente 2 prefiere Menu Andino");
        restaurante.servir(new FactoryAndino());

        System.out.println("Cliente 3 prefiere Menu Amazonico");
        restaurante.servir(new FactoryAmazonico());
    }
}
