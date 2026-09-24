# 📞 Guía de uso y configuración — Videollamada POO II

> Chat + videollamada en tiempo real para **hasta 6 personas** (probado el diseño para 4 cómodas).
> Código: `Tareas/src/main/java/PC1_chat` · Java 11+ · video con **JavaCV (OpenCV)** · audio con **Java Sound**.

<br>

## 1 · Cómo funciona (en 10 segundos)

```
          ┌──────────── ANFITRIÓN (una sola PC) ────────────┐
          │  MainServidor  ◄── recibe video/audio/chat       │
          │       │            y lo reenvía a los demás      │
          │  MainCliente (el anfitrión también participa)    │
          └───────▲───────────────▲───────────────▲─────────┘
                  │ TCP :6767     │               │
            laptop Bruno    laptop Carla     laptop Dario
            (MainCliente)   (MainCliente)    (MainCliente)
```

Una persona es **anfitrión**: ejecuta el **servidor** y además su cliente. Todos los demás solo ejecutan el **cliente** y escriben la dirección del anfitrión.

<br>

## 2 · Requisitos

| Qué | Detalle |
|---|---|
| Java | **11 o superior** en cada laptop → descargar *Eclipse Temurin 17 (LTS)* de adoptium.net |
| Cámara | Opcional. Sin cámara se usa una "cámara simulada" (fondo con tu nombre) |
| Audio | **Audífonos recomendados**: el programa no cancela eco; con parlantes se oye repetido |
| Internet del anfitrión | ≈ **15 Mbps de subida** para 4 personas (ver §8). Los demás ≈ 1,5 Mbps subida / 4 Mbps bajada |

<br>

## 3 · Obtener el programa

### Opción A — Un `.jar` para repartir (recomendada para "cualquier laptop")

El anfitrión lo genera una vez en IntelliJ:

1. Abrir la carpeta `Tareas` → panel **Maven** (derecha) → 🔄 *Reload*. La primera vez descarga OpenCV (~100 MB).
2. **Maven → Tareas → Lifecycle → `package`** (doble clic).
3. Aparece `Tareas/target/videollamada-windows-x86_64.jar`. Ese archivo se comparte (Drive, WhatsApp, USB).

Para laptops **Mac o Linux** hay que generar otro jar cambiando en `pom.xml` la línea
`<plataforma>windows-x86_64</plataforma>` por `macosx-arm64` (Mac M1–M4), `macosx-x86_64` (Mac Intel) o `linux-x86_64`, recargar Maven y volver a hacer `package`.

### Opción B — Desde el código (para el equipo del curso)

Clonar el repositorio, abrir `Tareas` en IntelliJ, recargar Maven y ejecutar los `Main` con *Run*.

<br>

## 4 · Anfitrión: iniciar el servidor

**IntelliJ:** clic derecho en `PC1_chat/servidor/MainServidor.java` → *Run*.
**Con el jar (CMD):**

```bat
java -cp videollamada-windows-x86_64.jar servidor.MainServidor 6767
```

La consola muestra algo así; **anota la IP** que corresponda a tu forma de conexión (§5):

```
 Comparte una de estas direcciones:
   192.168.1.34 : 6767   (Wi-Fi)
   100.101.12.7 : 6767   (Tailscale)
```

**Firewall de Windows:** la primera vez aparece un aviso para *Java(TM) Platform* → marca **Redes privadas y públicas** → *Permitir*.
Si no apareció, en CMD **como administrador**:

```bat
netsh advfirewall firewall add rule name="Videollamada POO" dir=in action=allow protocol=TCP localport=6767
```

<br>

## 5 · ¿Cómo se conectan los demás? (sin pagar servidor)

| Situación | Qué hacer | Dirección que escriben los demás |
|---|---|---|
| Todos en la **misma PC** (prueba) | Nada | `localhost` |
| Todos en la **misma red WiFi** (casa, laboratorio UNI) | Nada más que el firewall (§4) | IP `192.168.x.x` / `10.x.x.x` que muestra el servidor |
| **Casas distintas, todos con Windows** | **Radmin VPN** (gratis): el anfitrión crea una red con nombre y clave; los demás se unen con esos datos | IP `26.x.x.x` que Radmin le da al anfitrión |
| **Casas distintas, sistemas mezclados** | **Tailscale** (gratis): todos instalan y entran; el anfitrión invita a los demás o usa *Share* sobre su PC | IP `100.x.x.x` de Tailscale del anfitrión |
| **Nadie más quiere instalar nada** | **playit.gg** (gratis) en la PC del anfitrión: crear túnel **TCP** hacia el puerto local `6767` | La dirección y el puerto que da playit (ej. `abc.gl.at.ply.gg` y `12345`) |
| IP pública + abrir puerto en el router | ⚠️ No recomendado: muchas conexiones en Perú usan CGNAT (no funciona) y expone tu PC | Tu IP pública |

> Los planes gratuitos cambian; revisa los límites de usuarios de cada servicio antes de la reunión.
> ⚠️ El programa **no tiene contraseña ni cifrado** (Radmin/Tailscale sí cifran el túnel). Apaga el servidor al terminar.

<br>

## 6 · Unirse a la llamada (todos)

1. Doble clic en el `.jar` o, en CMD: `java -jar videollamada-windows-x86_64.jar`
   (IntelliJ: *Run* en `PC1_chat/cliente/MainCliente.java`).
2. Completa la ventana:

| Campo | Ejemplo | Nota |
|---|---|---|
| Tu nombre | `bruno` | 3–15 letras, números o `_`; no se puede repetir |
| Servidor | `192.168.1.34` | La de la tabla del §5 |
| Puerto | `6767` | Con playit.gg, el puerto que te dio |
| Cámara | *Cámara 0 (integrada)* | Si falla, entra con cámara simulada automáticamente |

3. **Mac:** la primera vez acepta el permiso de **Cámara** y **Micrófono** (Configuración → Privacidad y seguridad).

<br>

## 7 · Durante la llamada

```
┌───────────────────────────────┬──────────────┐
│  [ tú ]        [ bruno ]      │  Chat        │
│  [ carla ]     [ dario ]      │  ...         │
│   (el mosaico se acomoda solo)│ [escribir][▶]│
├───────────────────────────────┴──────────────┤
│ [Cámara: ON] [Micrófono: ON] [Salir]  4 en la llamada │
└──────────────────────────────────────────────┘
```

| Acción | Cómo |
|---|---|
| Apagar/encender cámara o micrófono | Botones de la barra inferior |
| Mensaje a todos | Escribir y *Enter* |
| Mensaje privado | `@bruno hola` |
| Ver conectados / tu historial | `/usuarios` · `/historial` · `/ayuda` |
| Salir | Botón **Salir** o cerrar la ventana |

Si alguien apaga su cámara o se congela, su recuadro muestra su inicial tras 2 s.

<br>

## 8 · Capacidad y calidad

| Personas | Subida del anfitrión | Bajada de cada uno |
|---|---|---|
| 2 | ≈ 3 Mbps | ≈ 1,3 Mbps |
| 4 | ≈ 15 Mbps | ≈ 4 Mbps |
| 6 | ≈ 36 Mbps | ≈ 6,5 Mbps |

Si se congela o hay retraso, baja la calidad en `media/ConfiguracionMedia.java` (y regenera el jar):
`VIDEO_FPS = 8`, `VIDEO_CALIDAD_JPEG = 0.45f` → casi la mitad de consumo.
Máximo de personas: `protocolo/ConfiguracionRed.MAX_PARTICIPANTES` (6).

<br>

## 9 · Problemas comunes

| Síntoma | Causa probable | Solución |
|---|---|---|
| `No se pudo conectar ... Connection refused` | Servidor apagado o puerto distinto | Iniciar `MainServidor`; revisar puerto |
| `... connect timed out` | Firewall o IP equivocada | Regla de firewall (§4); usar la IP de la tabla §5 |
| `El usuario X ya esta conectado` | Nombre repetido | Otro nombre |
| `La sala esta llena` | Ya hay 6 | Esperar o subir `MAX_PARTICIPANTES` |
| "usando camara simulada" | Cámara ocupada (Zoom/Teams/otro cliente) o jar de otro sistema | Cerrar la otra app; probar *Cámara 1*; usar el jar de tu SO |
| No se escucha a nadie | Parlantes/audífonos no predeterminados | Elegir el dispositivo por defecto en Windows y reingresar |
| Eco o pitido | Micrófono capta los parlantes | Audífonos o silenciar micrófono |
| Maven no descarga OpenCV | Sin internet o proxy | Reintentar *Reload*; revisar conexión |

<br>

## 10 · Probar todo en tu propia PC

1. *Run* `MainServidor`.
2. *Run* `MainCliente` → nombre `ana`, servidor `localhost`, **Cámara 0**.
3. *Run* `MainCliente` otra vez (activar *Allow multiple instances* en *Edit Configurations*) → `bruno`, **Sin cámara (simulada)**. Solo un programa puede usar la webcam a la vez.
4. Silencia el micrófono de uno de los dos para evitar el eco.
