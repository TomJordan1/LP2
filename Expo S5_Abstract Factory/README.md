# Menú Regional — Abstract Factory

Ejemplo en Java del patrón Abstract Factory aplicado a un restaurante que prepara un menú del día compuesto por entrada, fondo y bebida. El proyecto incluye tres variantes regionales: criolla, andina y amazónica.

La idea principal es que `Restaurante` trabaja con la interfaz `FabricaDeMenu`. Según la región seleccionada, se utiliza una fábrica concreta que crea los productos correspondientes a esa variante.

## Estructura

```text
src/main/java/pe/edu/uni/menu/
├── producto/        Interfaces Entrada, Fondo y Bebida
├── fabrica/         Interfaz FabricaDeMenu
├── variantes/       Fábricas concretas y sus productos
│   ├── criolla/
│   ├── andina/
│   └── amazonica/
├── cliente/         Restaurante y MenuDelDia
├── config/          Region y SelectorDeFabrica
├── problema/        Ejemplo sencillo sin Abstract Factory
└── app/             Main
```

## Familias de productos

| Producto | Criolla | Andina | Amazónica |
|---|---|---|---|
| Entrada | PapaALaHuancaina | ChocloConQueso | Patacones |
| Fondo | LomoSaltado | Pachamanca | Juane |
| Bebida | ChichaMorada | ApiMorado | Aguajina |

## Relación con Abstract Factory

- `FabricaDeMenu` representa la fábrica abstracta.
- `FabricaCriolla`, `FabricaAndina` y `FabricaAmazonica` son las fábricas concretas.
- `Entrada`, `Fondo` y `Bebida` representan los productos abstractos.
- Las clases de cada paquete regional son los productos concretos.
- `Restaurante` actúa como cliente y utiliza la fábrica recibida para preparar el menú.

`SelectorDeFabrica` se encarga de escoger la fábrica concreta según la región elegida en `Main`. Esta clase forma parte de la configuración del ejemplo y no del patrón en sí.

## Ejecutar en IntelliJ IDEA

1. Descomprimir el proyecto.
2. Abrir en IntelliJ IDEA la carpeta que contiene `pom.xml` y `src`.
3. Si IntelliJ muestra la opción de cargar el proyecto Maven, aceptarla.
4. Ejecutar `pe.edu.uni.menu.app.Main`.

El programa muestra un menú con las tres regiones y una cuarta opción que permite comparar el ejemplo con una versión sencilla sin Abstract Factory.

## Compilar con Maven

Desde la carpeta que contiene `pom.xml`:

```bash
mvn clean compile
```

El proyecto utiliza Java 11 y no necesita dependencias externas.

## Extender el ejemplo

Para agregar otra variante regional se puede crear un nuevo paquete dentro de `variantes`, implementar sus productos y añadir una fábrica que implemente `FabricaDeMenu`. Después, la nueva región debe incorporarse a la selección utilizada por la aplicación.

Si se quisiera agregar un nuevo tipo de producto, por ejemplo `Postre`, habría que ampliar `FabricaDeMenu` y las fábricas concretas para contemplarlo. Esto permite mostrar una de las características habituales del patrón: resulta más directo añadir nuevas familias que cambiar la estructura de productos existente.
