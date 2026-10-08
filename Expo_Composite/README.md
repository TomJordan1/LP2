# Composite: menu y combos de un restaurante

Proyecto Java sin dependencias (JDK 17 o superior).

## Estructura

```
src/pe/edu/uni/menu
├── modelo
│   ├── ItemMenu.java      componente
│   ├── Producto.java      base de las hojas
│   ├── Plato.java         hoja
│   ├── Bebida.java        hoja
│   └── Combo.java         compuesto
├── servicio
│   ├── Pedido.java        cliente del patron
│   └── Boleta.java
├── app
│   └── Main.java
└── verificacion
    └── Verificacion.java
```

## Compilar y ejecutar

En Windows (PowerShell), desde la carpeta del proyecto:

```
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out pe.edu.uni.menu.app.Main
java -cp out pe.edu.uni.menu.verificacion.Verificacion
```

En Linux o macOS:

```
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out pe.edu.uni.menu.app.Main
java -cp out pe.edu.uni.menu.verificacion.Verificacion
```

Tambien se puede abrir la carpeta en IntelliJ o NetBeans, marcar `src` como carpeta de fuentes y ejecutar `Main` o `Verificacion`.
