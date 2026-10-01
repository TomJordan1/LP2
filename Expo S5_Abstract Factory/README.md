# Menú Regional — Patrón Abstract Factory (Java 11+, Maven)

Proyecto de exposición. Un restaurante arma un **menú del día** (entrada + fondo + bebida)
en tres variantes regionales. Los tres platos SIEMPRE son de la misma región, y se puede
agregar una región nueva sin tocar el código del restaurante.

## Estructura (un paquete = una responsabilidad)

```
src/main/java/pe/edu/uni/menu/
├── producto/        Productos abstractos: Entrada, Fondo, Bebida (interfaces)
├── fabrica/         Fábrica abstracta: FabricaDeMenu (interface)
├── variantes/       Una subcarpeta por variante = fábrica concreta + sus productos
│   ├── criolla/     FabricaCriolla + PapaALaHuancaina, LomoSaltado, ChichaMorada
│   ├── andina/      FabricaAndina + ChocloConQueso, Pachamanca, ApiMorado
│   └── amazonica/   FabricaAmazonica + Patacones, Juane, Aguajina
├── cliente/         Restaurante (cliente) + MenuDelDia (agrupa la familia)
├── config/          Region (enum) + SelectorDeFabrica (elige la fábrica al iniciar)
├── problema/        PedidoSinPatron: el "antes", solo para contrastar
└── app/             Main: punto de entrada
```

Matriz del patrón (filas = tipos de producto, columnas = variantes):

|            | Criolla           | Andina           | Amazónica  |
|------------|-------------------|------------------|------------|
| Entrada    | PapaALaHuancaina  | ChocloConQueso   | Patacones  |
| Fondo      | LomoSaltado       | Pachamanca       | Juane      |
| Bebida     | ChichaMorada      | ApiMorado        | Aguajina   |

## Ejecutar

**IntelliJ:** abrir la carpeta (detecta el `pom.xml`) → ejecutar `app/Main`.
Para pasar una región: *Run → Edit Configurations → Program arguments* = `ANDINA`.

**Terminal (Windows):**
```
chcp 65001
run.bat            (menú interactivo)
run.bat CRIOLLA    (región directa)
```

**Terminal (Linux/Mac):** `./run.sh` o `./run.sh AMAZONICA`

## Demo en vivo: agregar la región Norteña (Principio Abierto/Cerrado)
1. Crear paquete `variantes/nortena/` con `CevicheDeConchas`, `ArrozConPato`, `ChichaDeJora`
   (package-private, implementan Entrada/Fondo/Bebida) y `FabricaNortena` (public).
2. Agregar `NORTENA("Norte")` al enum `Region`.
3. Agregar `case NORTENA: return new FabricaNortena();` en `SelectorDeFabrica`.

`Restaurante`, `MenuDelDia` y las interfaces **no se tocan**.

## Contra-demo: agregar un nuevo TIPO de producto (Postre)
Obliga a modificar `FabricaDeMenu` y **todas** las fábricas concretas. Es la desventaja
clásica del patrón: es fácil agregar variantes, costoso agregar tipos de producto.
