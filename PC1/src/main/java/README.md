# Semana 4 · PC1 — Chat + Videollamada en tiempo real (Java)

> **Objetivo:** chat de texto y **videollamada con audio para 4–6 personas** en distintas laptops,
> construido sobre lo visto en las semanas 1–4 (POO, contratos, excepciones, genéricos, hilos,
> colas concurrentes, SOLID y TCP).
> **Librerías:** JavaCV 1.5.10 (OpenCV) para la cámara · Java Sound y Swing (incluidos en Java).

📘 **[GUIA_USO.md](GUIA_USO.md)** — instalación, conexión desde cualquier laptop y problemas comunes
🧭 **[GUIA_FLUJOS.md](GUIA_FLUJOS.md)** — cómo se conectan los módulos

<br>

## ▶️ Ejecutar rápido (IntelliJ)

1. Panel **Maven** → 🔄 *Reload* (descarga OpenCV la primera vez).
2. *Run* `servidor/MainServidor`.
3. *Run* `cliente/MainCliente` (una vez por participante) → nombre, `localhost`, puerto `5000`.

<br>

## 📦 Módulos

| Subpaquete | Responsabilidad | Clases |
|---|---|---|
| `modelo` | Mensaje de chat | `Identificable`, `Transmisible`, `Mensaje` (abstracta), `MensajeTexto` |
| `excepciones` | Errores con código para el protocolo | `ChatException` + `Duplicado`, `NoEncontrado`, `MensajeInvalido`, `ComandoInvalido`, `ServidorLleno`, `Media` |
| `repositorio` | Historial genérico | `Repositorio<T extends Identificable>`, `RepositorioMemoria` |
| `servicio` | Reglas del chat | `ServicioChat`, `FabricaMensajes`, `GeneradorIds`, `Buzon` |
| `protocolo` | Cómo viaja todo por TCP | `Paquete`, `TipoPaquete`, `CodecPaquete`, `ColaSalida`, `SalidaPaquetes`, `ProtocoloChat`, `Comando`, `Peticion`, `ConfiguracionRed` |
| `media` | Cámara, JPEG y audio | `FuenteVideo`, `CamaraOpenCV`, `CamaraSimulada`, `CodificadorJpeg`, `TransmisorVideo`, `VisorVideo`, `CapturadorAudio`, `MezcladorAudio`, `ConfiguracionMedia` |
| `servidor` | Sala y retransmisión | `ServidorLlamada`, `ManejadorParticipante`, `CanalParticipante`, `SalaLlamada`, `MainServidor` |
| `cliente` | Conexión y control | `ClienteLlamada`, `ConexionServidor`, `ReceptorServidor`, `EventosLlamada`, `AccionesUsuario`, `TraductorEntrada`, `PresentadorMensajes`, `DatosConexion`, `MainCliente` |
| `cliente.ui` | Ventanas Swing | `VentanaLlamada`, `PanelVideo`, `PanelChat`, `DialogoConexion` |

<br>

## 🗺️ Sílabo → código

| Sem | Material A (POO) | Dónde | Material B | Dónde |
|---|---|---|---|---|
| 1 | Herencia, polimorfismo, encapsulamiento | `Mensaje`→`MensajeTexto`; `FuenteVideo`→`CamaraOpenCV` / `CamaraSimulada` | Thread, Runnable, join | 6 hilos en el cliente, 2 por participante en el servidor |
| 2 | Interfaces y contratos; composición | `SalidaPaquetes`, `VisorVideo`, `Buzon`, `EventosLlamada`, `AccionesUsuario`; `ClienteLlamada` **tiene** cámara, micrófono y mezclador | Sección crítica, `ReentrantLock` | `GeneradorIds`; un solo escritor por socket |
| 3 | Excepciones, genéricos, colecciones | `ChatException` y `MediaException` (la llamada sigue sin el dispositivo); `Repositorio<T>` | `BlockingQueue`, `ConcurrentHashMap`, `Semaphore`, `ExecutorService` | `ColaSalida`, `MezcladorAudio`, `SalaLlamada`, `ServidorLlamada` |
| 4 | SOLID | S: cada clase un motivo · O: nueva fuente de video sin tocar el transmisor · L: cualquier `FuenteVideo` sirve · I: interfaces pequeñas · D: media depende de `SalidaPaquetes`, no del socket | TCP, serialización, protocolo, pool limitado | `CodecPaquete` (binario) + `ProtocoloChat` (texto), `newFixedThreadPool` + `Semaphore` |

<br>

## ⚖️ Decisiones de diseño

- **Servidor retransmisor (relay):** cada persona envía 1 video y recibe N−1. Es simple y funciona detrás de NAT; el costo es la subida del anfitrión (ver guía §8).
- **TCP en vez de UDP:** atraviesa firewalls, VPN y túneles gratuitos sin configuración extra. Para no acumular retraso, `ColaSalida` **descarta** video viejo cuando la red va lenta y `MezcladorAudio` bota audio atrasado.
- **Video = JPEG por cuadro (MJPEG):** fácil de explicar y depurar; FFmpeg/H.264 comprimiría más, pero complica el proyecto.
- **Audio mezclado en el cliente:** una cola por persona y una sola línea de salida; sin cancelación de eco (usar audífonos).
- **Protocolo de dos capas:** el chat de la semana 4 (`LOGIN|ana`, `TXT|TODOS|hola`) viaja **dentro** de paquetes binarios `TEXTO`; video y audio van en paquetes `VIDEO` / `AUDIO`.
