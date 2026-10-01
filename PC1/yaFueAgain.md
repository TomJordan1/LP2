Sí. Si van con **informe + código**, entonces conviene que el informe funcione como mapa visual y evidencia de diseño/pruebas, mientras que el repo demuestra la implementación. No deberían saltar aleatoriamente entre archivos: el video debe seguir el recorrido real de la información por el sistema.

Hay además una inconsistencia que corregiría **antes de grabar**: el manual del informe usa el puerto `5000` en la pág. 39, pero el código actual define `PUERTO_DEFECTO = 6767` en `ConfiguracionRed.java`. Para el video usen un único valor y hagan coincidir informe, comandos y demo. El proyecto documenta, además, que integra texto, video y audio sobre sockets TCP y concurrencia, así que ese debe ser el hilo conductor de la sustentación. :chatgpt-content-reference{index="0"}

## Guion completo — 15:00

Las páginas que indico son **páginas físicas del archivo de 40 páginas**, no la numeración impresa inferior.

| Tiempo | Expositor | Parte |
|---|---|---|
| 0:00–4:15 | Tom | Problema, arquitectura, servidor y protocolo |
| 4:15–7:50 | Ricardo | Cliente, chat, POO y concurrencia |
| 7:50–11:45 | Raúl | Video, audio y manejo de tiempo real |
| 11:45–14:30 | Los tres | Demo |
| 14:30–15:00 | Raúl/Tom | Cierre |

---

# 0:00–4:15 — Tom
## Introducción, arquitectura, servidor y protocolo

### 0:00–0:35 — Qué construimos

**Tom:**

> Buenos días. Para esta práctica desarrollamos un sistema de videollamada en Java que permite que varios participantes se comuniquen mediante chat de texto, video y audio.
>
> Partimos del modelo cliente-servidor visto en clase, pero lo extendimos para que una misma conexión pueda transportar distintos tipos de información al mismo tiempo. Durante la explicación no vamos a recorrer todas las clases individualmente; vamos a seguir el recorrido de la información desde que un cliente se conecta hasta que otro recibe texto, video o audio.

[**INFORME: pág. 4, Resumen. Mostrar específicamente el párrafo donde se indica sockets TCP + texto/video/audio. Luego pág. 11–12, §3.2 “Estructura del proyecto”.**]

No permanezcan en el árbol del proyecto más de 10 segundos.

---

### 0:35–1:10 — Cómo se arma el servidor

> El punto de entrada del servidor es `MainServidor`. Esta clase funciona como raíz de composición: crea el repositorio de mensajes, el protocolo del chat, el servicio, la sala y la fábrica de mensajes, y finalmente los entrega a `ServidorLlamada`.
>
> Esto permite que las responsabilidades estén separadas. `MainServidor` configura las piezas, mientras que `ServidorLlamada` se encarga específicamente de aceptar conexiones.

[**CÓDIGO: `src/main/java/servidor/MainServidor.java`, líneas 27–39. Mantener visibles especialmente las líneas 31–39.**]

Aquí no expliquen todos los constructores. Señalen con el cursor:

`RepositorioMemoria → ServicioChat → SalaLlamada → ServidorLlamada`.

---

### 1:10–2:00 — Aceptación de conexiones y límite de participantes

> En `ServidorLlamada`, abrimos un `ServerSocket` y permanecemos esperando clientes mediante `accept()`.
>
> Cuando llega uno, usamos dos mecanismos relacionados pero distintos. Primero tenemos un `Semaphore`, que representa los cupos disponibles de la sala. Segundo, tenemos un `ExecutorService` con un número fijo de hilos, que ejecuta los manejadores de los participantes.
>
> Si `tryAcquire()` obtiene un permiso, la conexión se entrega a un `ManejadorParticipante`. Si no existe cupo, el servidor responde con un error de sala llena y cierra esa conexión.
>
> Por tanto, el semáforo controla la capacidad lógica de la sala y el pool controla los trabajadores que atienden las conexiones.

[**INFORME: pág. 12–13, §3.3 “Implementación del servidor”, Fragmento 1.**]

[**CÓDIGO: `src/main/java/servidor/ServidorLlamada.java`, líneas 32–43 para `pool` y `Semaphore`; después líneas 46–67 para `accept()`, `tryAcquire()` y `pool.execute()`.**]

Esta diferenciación es importante. No digan simplemente que “ambos limitan hilos”.

---

### 2:00–2:55 — ¿Cómo viajan texto, video y audio por el mismo socket?

> Una vez conectado el participante aparece un problema: TCP nos proporciona un flujo de bytes, pero nosotros necesitamos distinguir si esos bytes representan texto, un cuadro de video o audio.
>
> Para resolverlo definimos `Paquete`. Cada paquete contiene un tipo, un origen y los datos en bytes. Posteriormente `CodecPaquete` define exactamente cómo se escribe ese paquete en la conexión.
>
> Primero escribimos un byte que identifica el tipo; después el origen; luego un entero con la longitud de los datos; y finalmente los bytes del contenido.
>
> La longitud es fundamental porque nos permite saber cuántos bytes pertenecen al paquete actual antes de comenzar a leer el siguiente.

[**CÓDIGO: `src/main/java/protocolo/CodecPaquete.java`, líneas 9–13 para enseñar el formato; después líneas 17–34.**]

Mantengan visibles estas líneas:

```java
salida.writeByte(...)
salida.writeUTF(...)
salida.writeInt(...)
salida.write(...)
```

y luego:

```java
int largo = entrada.readInt();
entrada.readFully(datos);
```

> Además, antes de reservar memoria comprobamos que el tamaño no supere el máximo permitido por ese tipo de paquete.

[**CÓDIGO: `src/main/java/protocolo/TipoPaquete.java`, líneas 5–10; regresar a `CodecPaquete.java`, líneas 27–33.**]

---

### 2:55–3:40 — Qué hace el servidor con cada paquete

> Una vez reconstruido el paquete, `ManejadorParticipante` decide qué hacer según su tipo.
>
> Si es texto, lo interpreta mediante `ProtocoloChat` y lo envía a `ServicioChat`, donde se aplican las reglas del chat. Si es video o audio, el servidor no intenta decodificar la imagen ni reproducir el sonido. Después del inicio de sesión simplemente añade como origen el usuario autenticado y lo retransmite a la sala.
>
> Así evitamos confiar en un nombre de origen enviado arbitrariamente por el cliente.

[**INFORME: pág. 13–14, Fragmento 2 “despacho por tipo de paquete”.**]

[**CÓDIGO: `src/main/java/servidor/ManejadorParticipante.java`, líneas 75–93. Detenerse en el `switch`; señalar especialmente `p.conOrigen(usuario)` en la línea 87.**]

> La retransmisión se encuentra en `SalaLlamada`: recorre los participantes conectados y entrega el paquete a todos excepto al usuario que lo originó.

[**CÓDIGO: `src/main/java/servidor/SalaLlamada.java`, líneas 44–50.**]

---

### 3:40–4:15 — Transición hacia el cliente

> Hasta aquí hemos visto cómo entra una conexión y cómo el servidor distingue y retransmite la información. Ahora falta responder el otro lado de la comunicación: cómo un cliente puede tener simultáneamente una interfaz, recibir mensajes, capturar cámara, capturar micrófono y escribir por un único socket sin generar conflictos.

[**INFORME: pág. 18, Figura 1 “Hilos del cliente y del servidor y sus colas”. Mostrarla mientras Ricardo comienza.**]

---

# 4:15–7:50 — Ricardo
## Cliente, chat, POO y concurrencia

### 4:15–5:00 — Inicio del cliente

**Ricardo:**

> El cliente se coordina principalmente desde `ClienteLlamada`. Cuando iniciamos, primero se crea una `ConexionServidor`, luego se realiza el `LOGIN`, después se crea la ventana y finalmente se inician audio, video y un hilo receptor.
>
> Esta separación es importante porque la interfaz gráfica no debería quedarse esperando datos de red. Mientras Swing mantiene la ventana, otros hilos pueden capturar o recibir información.

[**INFORME: pág. 14–15, §3.4 “Implementación del cliente”, Fragmento 3.**]

[**CÓDIGO: `src/main/java/cliente/ClienteLlamada.java`, líneas 23–28 para mostrar los hilos; después líneas 41–55 para `iniciar()`.**]

No expliquen Swing todavía.

---

### 5:00–5:45 — Una conexión, dos direcciones

> `ConexionServidor` encapsula el socket utilizado por el cliente. Para recibir, el hilo `ReceptorServidor` permanece bloqueado esperando el siguiente paquete.
>
> Cuando lo recibe, vuelve a utilizar el tipo del paquete: texto se entrega a `alRecibirTexto`, video a `alRecibirVideo` y audio a `alRecibirAudio`.

[**CÓDIGO: `src/main/java/cliente/ConexionServidor.java`, líneas 16–33 y 45–47.**]

[**CÓDIGO: `src/main/java/cliente/ReceptorServidor.java`, líneas 19–40.**]

> Por eso no necesitamos tres conexiones diferentes para los tres medios.

---

### 5:45–6:30 — Chat: desde lo que escribe el usuario hasta el servidor

> En el chat también separamos lo que escribe una persona del protocolo interno.
>
> `TraductorEntrada` convierte, por ejemplo, un texto normal en `TXT|TODOS|mensaje`, un `@usuario` en un mensaje privado y comandos como `/usuarios` o `/historial` en las instrucciones correspondientes.
>
> En el servidor, `ProtocoloChat` interpreta esa línea y `ServicioChat` aplica las reglas de negocio.

[**INFORME: pág. 15, §3.5 “Chat de texto”.**]

[**CÓDIGO: `src/main/java/cliente/TraductorEntrada.java`, líneas 18–35.**]

[**CÓDIGO: `src/main/java/servicio/ServicioChat.java`, líneas 49–67.**]

> Por ejemplo, para un mensaje privado primero comprobamos que el destinatario realmente esté conectado. Si no existe, se lanza una excepción de dominio y el manejador devuelve un `ERROR` al cliente, pero la conexión continúa funcionando.

[**CÓDIGO: `src/main/java/servicio/ServicioChat.java`, líneas 59–66. Después `servidor/ManejadorParticipante.java`, líneas 131–134.**]

Esto prepara exactamente el error que demostrarán después.

---

### 6:30–7:20 — El problema crítico: muchos hilos escribiendo al mismo socket

> La concurrencia aparece también al enviar. El chat, la cámara y el micrófono pueden generar paquetes prácticamente al mismo tiempo.
>
> Si permitiéramos que todos esos hilos escribieran directamente sobre el mismo `DataOutputStream`, los bytes de dos paquetes podrían intercalarse y corromper el protocolo.
>
> Por eso utilizamos `ColaSalida`, siguiendo un esquema productor-consumidor. Los distintos componentes únicamente colocan paquetes en una `BlockingQueue`, y existe un único consumidor que los extrae y los escribe físicamente en el socket.

[**INFORME: pág. 17–19, §3.9 “Concurrencia”; especialmente pág. 18 Figura 1 y pág. 19 Tabla 4.**]

[**CÓDIGO: `src/main/java/protocolo/ColaSalida.java`, líneas 9–20 y 29–63.**]

Dejen visible especialmente:

```java
private final BlockingQueue<Paquete> cola
```

y:

```java
Paquete p = cola.take();
codec.escribir(salida, p);
```

> Además, la cola distingue prioridades prácticas. El texto intenta esperar espacio porque no queremos perder un mensaje de chat, mientras que un cuadro de video puede descartarse si ya existe demasiada información pendiente, porque enviarlo tarde solamente aumentaría el retraso.

[**CÓDIGO: `ColaSalida.java`, líneas 34–48.**]

---

### 7:20–7:50 — POO aplicada, no decorativa

> Finalmente, también usamos abstracción para evitar acoplar el sistema a una cámara específica. `FuenteVideo` define el contrato de una fuente de imágenes. Tanto `CamaraOpenCV` como `CamaraSimulada` cumplen ese mismo contrato.
>
> Por ello, `TransmisorVideo` recibe simplemente una `FuenteVideo` y no necesita conocer cuál implementación concreta estamos utilizando.

[**CÓDIGO: `src/main/java/media/FuenteVideo.java`, archivo completo, líneas 7–18. Después enseñar únicamente la declaración `implements FuenteVideo` en `CamaraOpenCV.java` y `CamaraSimulada.java`.**]

> Raúl explicará ahora qué ocurre después de obtener esas imágenes y cómo se maneja el audio.

---

# 7:50–11:45 — Raúl
## Video, audio y decisiones de tiempo real

### 7:50–8:45 — Captura de video

**Raúl:**

> Para video, primero intentamos trabajar con una cámara real mediante `CamaraOpenCV`. Al abrirla configuramos el ancho y alto deseados, y cada vez que capturamos obtenemos un frame que convertimos a `BufferedImage`.
>
> También escalamos la imagen porque el dispositivo físico no necesariamente respeta exactamente la resolución solicitada.
>
> Si la cámara no puede abrirse, el cliente puede continuar utilizando `CamaraSimulada`. Esto también nos permite ejecutar varios clientes en una misma computadora aunque solo tengamos una webcam disponible.

[**INFORME: pág. 15–16, §3.6 y §3.7.**]

[**CÓDIGO: `src/main/java/media/CamaraOpenCV.java`, líneas 29–50.**]

[**CÓDIGO: `src/main/java/cliente/ClienteLlamada.java`, líneas 103–120, donde se selecciona la fuente real o simulada.**]

---

### 8:45–9:35 — Transmisión del video

> Una vez seleccionada la fuente, `TransmisorVideo` corre en su propio hilo.
>
> Nuestra configuración establece una resolución de 320 por 240, doce cuadros por segundo y una calidad JPEG de 0.6.
>
> En cada ciclo capturamos un cuadro, lo mostramos localmente, lo comprimimos como JPEG y construimos un paquete de tipo `VIDEO`. Después dormimos solamente el tiempo restante del período para aproximarnos a los doce cuadros por segundo.

[**CÓDIGO: `src/main/java/media/ConfiguracionMedia.java`, líneas 5–17.**]

[**CÓDIGO: `src/main/java/media/TransmisorVideo.java`, líneas 23–29, 32–49 y 52–58.**]

> En el otro cliente ocurre la operación inversa: `ReceptorServidor` identifica `VIDEO`, obtiene el origen y los bytes JPEG, y `ClienteLlamada` los decodifica para actualizar el panel correspondiente.

[**CÓDIGO: `src/main/java/cliente/ReceptorServidor.java`, líneas 28–30; después `ClienteLlamada.java`, líneas 150–155.**]

---

### 9:35–10:25 — Formato de audio

> El audio utiliza Java Sound. Configuramos PCM a 16 mil muestras por segundo, 16 bits, un canal, con signo y little-endian.
>
> Cada muestra utiliza dos bytes, así que tenemos aproximadamente 32 mil bytes por segundo. Nuestro bloque es de 1280 bytes: al dividir 1280 entre 32000 obtenemos 0.04 segundos, es decir, cada paquete contiene aproximadamente 40 milisegundos de audio.
>
> `CapturadorAudio` abre el micrófono y realiza una lectura bloqueante de esos bloques. Si el micrófono está activo, los encapsula como paquetes `AUDIO` y los envía mediante la misma cola que vimos anteriormente.

[**INFORME: pág. 16–17, §3.8 “Audio”.**]

[**CÓDIGO: `src/main/java/media/ConfiguracionMedia.java`, líneas 15–17. Mantenerlas visibles durante el cálculo.**]

[**CÓDIGO: `src/main/java/media/CapturadorAudio.java`, líneas 26–43.**]

---

### 10:25–11:15 — Mezcla de varias voces

> Recibir audio de varios participantes plantea otro problema. No podemos simplemente reproducir cada bloque independientemente sobre el mismo parlante.
>
> `MezcladorAudio` mantiene una cola limitada por participante. El hilo receptor actúa como productor y el mezclador como consumidor.
>
> Cada ciclo toma un bloque disponible de cada usuario, reconstruye sus muestras de 16 bits y las suma muestra por muestra. Después limita el resultado al rango válido de un `short` antes de volver a convertirlo en bytes y reproducirlo.

[**INFORME: pág. 16–17, Fragmento 5. Si necesitan mostrar la suma concreta, Anexo A, pág. 38–39.**]

[**CÓDIGO: `src/main/java/media/MezcladorAudio.java`, líneas 43–53 para recepción; líneas 59–76 para ciclo de mezcla; líneas 92–105 para conversión PCM.**]

> También tratamos el retraso. Cada cola admite como máximo seis bloques, y si se llena eliminamos los más antiguos. Además precargamos dos bloques después de un vaciamiento para absorber pequeñas variaciones en la llegada por red.

[**CÓDIGO: `MezcladorAudio.java`, líneas 25–29 y 43–53, después 78–89.**]

---

### 11:15–11:45 — Limitaciones reconocidas

> La solución funciona como prototipo académico, pero no pretendemos presentarla como una arquitectura definitiva de videoconferencia.
>
> En particular, utilizamos TCP por simplicidad y entrega ordenada, pero una retransmisión puede aumentar la latencia del contenido multimedia. Tampoco implementamos cancelación de eco, por lo que recomendamos utilizar audífonos.
>
> Como mejoras planteamos separar el transporte multimedia mediante UDP o RTP, usar un códec como H.264 y automatizar pruebas de latencia.

[**INFORME: pág. 32, “Dificultades y soluciones”, especialmente audio y `ColaSalida`; pág. 34, §8 “Recomendaciones y trabajo futuro”.**]

Aquí cierren el informe. Empieza la aplicación.

---

# 11:45–14:30 — DEMO

La demo debe estar **ensayada segundo a segundo**. No improvisen nombres, IP ni ventanas.

## 11:45–12:25 — Tom: conexión de participantes

**Tom:**

> Ahora vamos a comprobar el recorrido que acabamos de explicar. Primero ejecutamos el servidor.

[**CÓDIGO/EJECUCIÓN: `servidor.MainServidor`. La consola debe mostrar IP, puerto y cupos. Si usan el valor actual del código: puerto `6767`.**]

> El servidor queda bloqueado en `accept()` esperando conexiones. Ahora conectamos los tres clientes: Tom, Ricardo y Raúl.

[**EJECUCIÓN: `cliente.MainCliente` ×3. Una cámara real como máximo si están en la misma PC; los otros pueden usar cámara simulada.**]

Cuando entren, enseñen brevemente que aparecen los tres participantes.

> En la consola del servidor podemos ver también las entradas en la sala, lo que confirma que cada conexión fue asignada a su manejador.

No pierdan tiempo reordenando ventanas.

---

## 12:25–13:15 — Ricardo: chat y manejo de error

**Ricardo:**

> Primero comprobaremos un mensaje grupal.

Escribir desde Ricardo:

`Hola grupo`

Mostrar Tom y Raúl recibiéndolo.

> El servidor lo tradujo como un mensaje dirigido a `TODOS`, lo almacenó y lo distribuyó a los demás participantes, sin devolvérselo al remitente.

[**INFORME: pág. 24–25, prueba PCH01.**]

Después:

`@Tom mensaje privado`

> Ahora el destino es solamente Tom.

Mostrar que Raúl no lo recibió.

[**INFORME: pág. 25, PCH02.**]

Finalmente:

`@NoExiste hola`

> Y aquí comprobamos el manejo de excepciones. El destinatario no está conectado, por lo que el servidor responde con un error, pero la conexión no se cae y podemos seguir utilizando el chat.

[**INFORME: pág. 26–27, PCH03.**]

**No hagan también el mensaje de 300 caracteres en la demo** salvo que vayan sobrados de tiempo. Está documentado en las págs. 27–28.

---

## 13:15–14:05 — Raúl: video y audio

**Raúl:**

> Ahora comprobamos video. Tenemos una fuente real y podemos mantener otro participante con cámara simulada usando exactamente la misma interfaz `FuenteVideo`.

Mostrar ambas.

> Si desactivamos la cámara de Raúl, deja de enviar cuadros y, después del tiempo previsto por la interfaz, los otros clientes muestran su representación sin video.

Pulsar cámara OFF. Esperar la reacción visual. Volver a ON.

[**INFORME: pág. 28–30, PVI01–PVI03.**]

Después audio:

> Para audio dejaremos un único micrófono activo para evitar retroalimentación. Voy a decir una frase corta y comprobaremos que se reproduce en otro cliente.

**Usen audífonos.**

Frase preparada:

> “Prueba de audio, uno, dos, tres.”

No intenten que hablen los tres simultáneamente solo para presumir del mezclador. Es innecesariamente riesgoso.

[**INFORME: pág. 30–31, §4.6 prueba de audio.**]

---

## 14:05–14:30 — Tom: desconexión

**Tom:**

> Finalmente, Raúl sale de la llamada.

Raúl pulsa salir.

> Los otros clientes reciben el evento `SALIO`, retiran su panel de video y eliminan su cola de audio. En el servidor se libera también el permiso del semáforo, por lo que vuelve a quedar disponible ese cupo.

[**CÓDIGO: `servidor/ManejadorParticipante.java`, líneas 65–71; `cliente/ClienteLlamada.java`, líneas 136–142.**]

Esta es una buena forma de cerrar la demo porque demuestra también **limpieza de recursos**, no solamente el caso feliz.

---

# 14:30–15:00 — Cierre

**Raúl:**

> En conclusión, el proyecto nos permitió integrar los conceptos de las primeras semanas en una sola aplicación: abstracción e interfaces para separar implementaciones, excepciones y colecciones para modelar las reglas del chat, concurrencia para coordinar tareas simultáneas y sockets TCP para comunicar distintos dispositivos.

**Tom:**

> Más que añadir video y audio al chat, el principal problema fue coordinar esas tareas sin mezclar los datos y sin bloquear la interfaz. Por eso las decisiones centrales terminaron siendo el protocolo de paquetes, un escritor único por socket y la separación de responsabilidades entre cliente, servidor y componentes multimedia.

[**INFORME: pág. 33, §7 “Conclusiones”. Mantenerla visible hasta finalizar el video.**]

**Corten ahí.** Nada de “bueno, eso sería todo creo” ni quedarse mirando la pantalla.

---

## Preparación exacta de las pestañas

Antes de grabar, yo dejaría el entorno **ya preparado en este orden**, para que cada cambio de pantalla tome 1–2 segundos:

| Nº | Recurso preparado |
|---:|---|
| 1 | Informe pág. 4 |
| 2 | Informe pág. 12 |
| 3 | `MainServidor.java` |
| 4 | `ServidorLlamada.java` |
| 5 | `CodecPaquete.java` |
| 6 | `ManejadorParticipante.java` |
| 7 | Informe pág. 18 |
| 8 | `ClienteLlamada.java` |
| 9 | `ReceptorServidor.java` |
| 10 | `ColaSalida.java` |
| 11 | `FuenteVideo.java` |
| 12 | `TransmisorVideo.java` |
| 13 | `ConfiguracionMedia.java` |
| 14 | `CapturadorAudio.java` |
| 15 | `MezcladorAudio.java` |
| 16 | Informe pág. 34 |
| 17 | Terminal servidor |
| 18 | Clientes ya listos para iniciar |

Dos detalles finales son especialmente importantes. **No hagan scroll buscando una función mientras hablan**; cada clase tiene que abrirse ya aproximadamente en la línea indicada. Y no describan una clase completa cuando solo necesitan demostrar cinco líneas: el video debe transmitir que comprenden **por qué existe el código**, no que pueden leerlo.

El informe ya documenta que el propósito era integrar POO, interfaces, excepciones, colecciones, hilos y comunicación en red dentro de una aplicación funcional; la secuencia de arriba está construida precisamente para que cada una de esas afirmaciones termine respaldada por una implementación o por la demo. :chatgpt-content-reference{index="1"}