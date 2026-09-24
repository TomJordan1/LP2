# 🧭 Guía de flujos · Chat + Videollamada

> Cómo viajan el chat, el video y el audio entre los módulos. Uso e instalación: [GUIA_USO.md](GUIA_USO.md).

<br>

## 1 · Mapa general

```
 CLIENTE (cada laptop)                                         SERVIDOR (anfitrión)
 ┌──────────────────────────────────────┐                 ┌──────────────────────────────────┐
 │ CamaraOpenCV ─► TransmisorVideo ─┐    │                 │ ServidorLlamada                  │
 │   (hilo Camara)   JPEG           │    │                 │  accept ─► Semaphore ─► pool     │
 │ CapturadorAudio ─────────────────┤    │   TCP :6767     │           │                      │
 │   (hilo Microfono)  PCM          ▼    │   Paquetes      │  ManejadorParticipante (1/persona)│
 │ PanelChat ─► ClienteLlamada ─► ColaSalida ════════════► │   ├─ TEXTO ─► ProtocoloChat       │
 │                                (hilo Salida)            │   │           └► ServicioChat     │
 │                                      │                  │   └─ VIDEO/AUDIO ─► SalaLlamada   │
 │ ReceptorServidor ◄═════════════════════════════════════ │        retransmitir a los demás   │
 │  (hilo Receptor)                     │                  │        └► CanalParticipante       │
 │   ├─ TEXTO ─► chat / mosaico         │                  │           (cola + hilo Salida)    │
 │   ├─ VIDEO ─► CodificadorJpeg ─► PanelVideo             └──────────────────────────────────┘
 │   └─ AUDIO ─► MezcladorAudio ─► parlante (hilo Parlante)
 └──────────────────────────────────────┘
```

<br>

## 2 · El paquete: una sola "caja" para todo

```
 ┌──────────┬──────────────┬───────────┬──────────────────────────────┐
 │ tipo 1B  │ origen (UTF) │ largo 4B  │ datos                        │
 ├──────────┼──────────────┼───────────┼──────────────────────────────┤
 │ TEXTO    │ ""           │ 14        │ "TXT|TODOS|hola"   (sem. 4)  │
 │ VIDEO    │ "ana"        │ ~9 000    │ JPEG 320x240                 │
 │ AUDIO    │ "ana"        │ 1 280     │ 40 ms de PCM 16 kHz          │
 └──────────┴──────────────┴───────────┴──────────────────────────────┘
```
El **origen lo pone el servidor**: el cliente no puede hacerse pasar por otro.

<br>

## 3 · Entrar a la llamada

```
 bruno                          Manejador(bruno)           ServicioChat    SalaLlamada
   │ connect ─────────────────► │ (cupo del Semaphore)          │              │
   │ ◄── INFO|Bienvenido ────── │                               │              │
   │ LOGIN|bruno ─────────────► │ conectar(bruno) ─────────────►│ valida nombre│
   │ ◄── OK|Sesion iniciada ─── │                               │              │
   │                            │ unir(bruno) ─────────────────────────────────►│
   │ ◄── PARTICIPANTES|ana ──── │                               │   a ana: UNIDO|bruno
   │ (crea recuadro de ana)                                       (ana crea recuadro de bruno)
```

<br>

## 4 · Un cuadro de video (cada 83 ms)

```
 ① TransmisorVideo: fuente.capturar() ─► vista previa local ─► CodificadorJpeg (≈9 KB)
 ② ColaSalida.enviar(VIDEO)   ← si la cola está a medias, se DESCARTA (tiempo real)
 ③ Servidor: Manejador(ana) lee ─► SalaLlamada.retransmitir("ana", paquete)
 ④ CanalParticipante de bruno, carla, dario encolan ─► su hilo Salida escribe
 ⑤ ReceptorServidor de bruno ─► decodificar ─► PanelVideo("ana").mostrar()
```

## 5 · El audio (cada 40 ms)

```
 CapturadorAudio: micrófono.read(1280 B) ─► AUDIO ─► servidor ─► los demás
 MezcladorAudio (en cada receptor):
     [cola ana]   ─┐
     [cola carla] ─┼─ suma muestra a muestra ─► parlante.write()  (marca el ritmo)
     [cola dario] ─┘   cola > 6 bloques → se bota lo viejo · cola vacía → precarga 2
```

## 6 · Si algo falla

| Falla | Dónde se detecta | Qué pasa |
|---|---|---|
| Regla del chat (nombre repetido, texto vacío...) | `ServicioChat`, `MensajeTexto`, `ProtocoloChat` | `ERROR\|CODIGO\|detalle` en el chat; sigue conectado |
| Sala llena | `ServidorLlamada` (Semaphore) | El diálogo de ingreso muestra el error |
| Cámara ocupada / sin librerías nativas | `CamaraOpenCV.abrir()` → `MediaException` | Entra con `CamaraSimulada` |
| Sin micrófono o parlantes | `CapturadorAudio` / `MezcladorAudio` | Aviso en el chat; la llamada sigue |
| Paquete corrupto o gigante | `CodecPaquete.leer()` | Se cierra solo esa conexión |
| Se cae la red | `ReceptorServidor` (IOException) | Aviso y cierre ordenado |

## 7 · Hilos y recursos compartidos

| Recurso | Quién lo comparte | Protección |
|---|---|---|
| Socket de salida | Cámara, micrófono, chat (cliente) / todos los demás (servidor) | `ColaSalida`: BlockingQueue + **un** escritor |
| Participantes de la sala | Hilos del pool | `ConcurrentHashMap` |
| Audio por persona | Receptor (produce) y Parlante (consume) | `ArrayBlockingQueue` por origen |
| IDs de mensajes | Hilos del pool | `ReentrantLock` |
| Recuadros de video | Receptor escribe, Swing dibuja | campos `volatile` + `invokeLater` |
| Cupos | accept vs. salidas | `Semaphore` + pool fijo |
