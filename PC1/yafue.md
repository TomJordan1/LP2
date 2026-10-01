Sí: después de revisar el informe, las notas del profesor y la estructura real del ZIP, yo no plantearía el video como una exposición tradicional. Lo plantearía como una **mini defensa técnica de 15 minutos**, porque el proyecto ya es suficientemente grande como para que intentar explicar “todas las clases” los mate.

El proyecto parte del chat TCP de clase, pero ya creció a un sistema cliente-servidor con texto, video y audio, usando JavaCV/OpenCV, Java Sound, concurrencia y un protocolo de paquetes propio. Eso coincide además con lo que ustedes mismos documentaron en el informe. :chatgpt-content-reference{index="0"} Y hay una conexión bastante limpia con el curso: la semana 4 enfatiza que TCP es un **flujo de bytes**, no conserva fronteras de mensajes y por eso la aplicación debe definir su propio protocolo. :chatgpt-content-reference{index="1"}

La regla para el video debería ser:

> **No explicar el código archivo por archivo. Explicar qué problema resuelve cada bloque, seguir un dato real por el sistema y demostrarlo inmediatamente.**

### Distribución que usaría para los 15 minutos

| Tiempo | Quién | Qué debe explicar | Qué código mostrar |
|---|---|---|---|
| **0:00–0:35** | Tom | Problema, objetivo y arquitectura general | Diagrama simple Cliente → Servidor → Clientes |
| **0:35–4:15** | Tom | Servidor, sockets TCP y protocolo de paquetes | `ServidorLlamada`, `ManejadorParticipante`, `CodecPaquete`, `Paquete` |
| **4:15–8:00** | Ricardo | Cliente + POO + concurrencia | `ClienteLlamada`, `ConexionServidor`, `ColaSalida`, `ServicioChat`, `RepositorioMemoria` |
| **8:00–11:45** | Raúl | Video y audio: captura, compresión, transmisión y mezcla | `FuenteVideo`, `TransmisorVideo`, `ConfiguracionMedia`, `CapturadorAudio`, `MezcladorAudio` |
| **11:45–14:30** | Los 3 | Demo preparada y comentada | Servidor + 2/3 clientes |
| **14:30–15:00** | Raúl/Tom | Limitaciones y cierre | Sin código o diagrama general |

Eso deja aproximadamente **4 minutos efectivos por integrante**, porque durante la demo también pueden intervenir los tres.

## 1. Tom: arquitectura, servidor y protocolo

La primera parte debe responder algo muy concreto: **“¿cómo logra un cliente enviar texto, audio y video por una única conexión TCP sin que el receptor confunda los datos?”**

No empieces diciendo “tenemos 47 clases”. Empieza con algo del estilo:

> “Nuestro sistema usa una arquitectura cliente-servidor. Cada cliente mantiene una conexión TCP con el servidor. El servidor no procesa el video ni el audio: principalmente identifica quién lo envió y lo retransmite al resto. Como por el mismo socket viajan tres tipos de información, creamos nuestro propio formato de paquete.”

Ahí muestras `Paquete` y `CodecPaquete`.

La estructura importante es:

`tipo | origen | longitud | datos`

Eso tiene una justificación muy buena frente al profesor: **TCP no les entrega “un mensaje” cada vez que escriben**, sino un flujo de bytes. La propia guía del curso insiste en que una escritura no equivale necesariamente a una lectura y que el protocolo debe delimitar los mensajes. :chatgpt-content-reference{index="2"} Además, el material define precisamente un protocolo de aplicación como el contrato que establece formato, comandos, codificación, respuestas y errores. :chatgpt-content-reference{index="3"}

Después sigue un paquete:

`Cliente → ColaSalida → CodecPaquete → socket TCP → CodecPaquete → ManejadorParticipante`

Y muestras cómo `ManejadorParticipante` hace el `switch`:

`TEXTO` → `ProtocoloChat` / `ServicioChat`  
`VIDEO` → `SalaLlamada`  
`AUDIO` → `SalaLlamada`

Eso es mucho más potente que explicar cada método.

Cuando llegues a `ServidorLlamada`, la pregunta natural es:

**¿Por qué hay un `Semaphore` y un `ExecutorService` si ambos limitan recursos?**

La respuesta correcta sería: el pool limita cuántos manejadores pueden ejecutarse; el semáforo representa explícitamente los **cupos de la sala** y permite rechazar inmediatamente una nueva conexión cuando está llena, en lugar de dejarla esperando indefinidamente en una cola.

El material de clase justamente distingue la atención concurrente de clientes y advierte que hay que limitar tanto trabajadores como recursos. :chatgpt-content-reference{index="4"}

---

## 2. Ricardo: POO y concurrencia, pero usando casos reales

Acá es donde pueden sumar bastantes puntos si evitan la exposición escolar de:

“esto es herencia, esto es interfaz, esto es polimorfismo”.

Hay que decir **para qué sirvió**.

Por ejemplo, mostrar:

`FuenteVideo`

y luego:

`CamaraOpenCV implements FuenteVideo`  
`CamaraSimulada implements FuenteVideo`

La explicación sería:

> “El transmisor no necesita saber si la imagen proviene de una webcam real o de una cámara simulada. Depende del contrato `FuenteVideo`. Por eso podemos sustituir una implementación por otra sin modificar `TransmisorVideo`.”

Eso conecta directamente con el material del profesor: una interfaz define un contrato, mientras que composición permite entregar colaboradores diferentes sin reescribir al cliente. :chatgpt-content-reference{index="5"}

Después pasaría a la parte realmente importante: **concurrencia**.

En este programa hay un problema muy fácil de defender: cámara, micrófono y chat pueden producir datos simultáneamente, pero **no deberían escribir todos directamente sobre el mismo `DataOutputStream`**.

Entonces aparece:

`muchos productores → BlockingQueue → un escritor → socket`

Eso es `ColaSalida`.

Y ahí pueden decir literalmente:

> “No usamos la cola solo para guardar cosas. La usamos para serializar el acceso al socket: muchos hilos pueden producir paquetes, pero únicamente un hilo escribe físicamente sus bytes.”

Eso está tremendamente alineado con productor-consumidor. La guía explica que `BlockingQueue` coordina productor y consumidor sin espera activa y que una capacidad limitada evita crecimiento ilimitado de memoria. :chatgpt-content-reference{index="6"}

Después tienen dos ejemplos cortos buenísimos:

`ConcurrentHashMap + putIfAbsent`

para nombres de usuario, y

`ReentrantLock`

en `GeneradorIds`.

No digan:

> “ConcurrentHashMap hace todo thread-safe.”

Porque GPT-Astra-satanás les responderá: **“¿una secuencia de dos operaciones sobre ConcurrentHashMap es atómica?”**

No.

Digan:

> “ConcurrentHashMap hace seguras sus operaciones individuales. Cuando necesitamos verificar y registrar un usuario sin carrera usamos `putIfAbsent`, porque esa operación sí realiza ambos pasos atómicamente.”

Y para `GeneradorIds`:

> “El `ultimo++` parece una sola instrucción, pero conceptualmente implica lectura, incremento y escritura. Por eso lo encerramos en una sección crítica.”

Eso coincide exactamente con las notas del profesor sobre carreras y `ReentrantLock`. :chatgpt-content-reference{index="7"} :chatgpt-content-reference{index="8"}

---

## 3. Raúl: video y audio

Esta parte tiene que ser muy visual.

Para video, no expliquen OpenCV internamente. Expliquen el flujo:

`CamaraOpenCV → BufferedImage → JPEG → Paquete VIDEO → servidor → otros clientes → JPEG → BufferedImage → PanelVideo`

Y luego la decisión técnica:

**320 × 240, 12 FPS, JPEG 60 %.**

La pregunta inevitable será:

**“¿Por qué JPEG y no H.264?”**

Respuesta:

> “JPEG por cuadro es menos eficiente en ancho de banda, pero cada imagen es independiente y la implementación es mucho más sencilla de comprender, depurar y relacionar con los contenidos del curso. H.264 sería una mejora posterior.”

Muy defendible.

Luego audio:

`16000 Hz · 16 bits · mono · signed · little-endian`

Y aquí tienen que saber hacer esta cuenta sin titubear:

`16000 muestras/s × 2 bytes/muestra × 1 canal = 32000 bytes/s`

Cada paquete es de `1280 bytes`.

`1280 / 2 = 640 muestras`

`640 / 16000 = 0.04 s = 40 ms`

Esa es la razón por la que el código dice **40 ms por bloque**.

Luego `MezcladorAudio`:

cada participante remoto tiene una cola → se toman las muestras → se suman → se hace clipping a `Short.MIN_VALUE ... Short.MAX_VALUE` → se reproduce.

Y la parte de tiempo real:

> “No queremos reproducir perfectamente todos los datos si eso significa escuchar algo ocurrido hace cuatro segundos. Por eso limitamos las colas y descartamos información atrasada.”

Eso también les permite explicar por qué `ColaSalida` puede descartar cuadros de video cuando está congestionada.

Muy importante: **no digan que TCP es ideal para tiempo real**. Digan:

> “Elegimos TCP por simplicidad, entrega ordenada y facilidad de conexión. Sabemos que una retransmisión puede introducir latencia y head-of-line blocking. Mitigamos parte del retraso evitando acumular video antiguo, aunque una evolución natural sería transportar medios mediante UDP/RTP.”

Esa respuesta demuestra que conocen tanto la implementación como su limitación.

---

# La demo de 2:45 que yo grabaría

No hagan una demo de “miren, prende”.

Debe parecer una **prueba de hipótesis**.

Primero arranquen `MainServidor`. Que se vea el puerto `6767`. Después entren dos clientes, por ejemplo Tom y Ricardo. Envíen un mensaje grupal y muestren que llega al otro. Luego envíen `@Ricardo hola` para mostrar el privado. Después intenten escribir a un usuario inexistente: esto demuestra manejo de excepciones **sin tumbar la conexión**.

Después Raúl entra como tercer cliente. Ahí se ve que el servidor realmente soporta más de los dos clientes del ejemplo inicial de clase; aquel material estaba intencionalmente limitado a dos clientes para enseñar el mecanismo fundamental. :chatgpt-content-reference{index="9"}

Luego apaguen la cámara de uno: debe desaparecer el frame y quedar su representación alternativa. Reactívenla. Si quieren demostrar el polimorfismo en 10 segundos, abran uno de los clientes con `CamaraSimulada`: la interfaz funciona exactamente igual aunque la fuente cambie.

Para audio, **usen audífonos**. No intenten demostrar tres micrófonos abiertos alrededor de una laptop porque convertirán la sustentación en un experimento de feedback acústico del CERN. Basta con que uno diga una frase y el otro confirme recepción, o que muestren el mensaje de inicialización correcta.

Finalmente uno sale y los otros deben recibir el evento correspondiente.

En menos de tres minutos demostraron:

`LOGIN → chat grupal → privado → excepción → tres participantes → video → polimorfismo → audio → desconexión`

Eso vale muchísimo más que veinte minutos moviendo el mouse por clases.

---

## El evaluador “modo caos” probablemente atacará aquí

| Pregunta | Respuesta que deberían tener lista |
|---|---|
| **¿Por qué no mandar directamente objetos Java?** | Queremos un protocolo explícito e independiente de referencias internas; serializamos bytes con tipo, origen y longitud. |
| **¿Por qué existe `largo` si TCP ya sabe cuántos bytes llegaron?** | TCP solo entrega un flujo; necesitamos saber dónde termina un paquete y comienza el siguiente. |
| **¿Por qué un único escritor por socket?** | Para que dos hilos no intercalen los bytes de paquetes diferentes. |
| **¿Por qué `BlockingQueue`?** | Implementa productor-consumidor de forma thread-safe y además podemos limitar la capacidad. |
| **¿Por qué `Semaphore` si existe un pool?** | El semáforo modela cupos y permite rechazo inmediato; el pool administra trabajadores. |
| **¿Por qué `ConcurrentHashMap` no basta para generar IDs?** | Porque la operación que queremos proteger no está en ese mapa; `ultimo++` es un estado compartido diferente. |
| **¿Para qué `volatile`?** | Para visibilidad entre hilos; no convierte operaciones compuestas en atómicas. |
| **¿Por qué TCP y no UDP?** | Simplicidad y confiabilidad frente a una solución académica; aceptamos la penalización de latencia y reconocemos UDP/RTP como mejora. |
| **¿Qué ocurre si llegan datos corruptos gigantes?** | `CodecPaquete` comprueba que la longitud esté dentro del máximo definido para el tipo antes de reservar el arreglo. |
| **¿El servidor decodifica los videos?** | No. Funciona como relay: identifica el origen y reenvía los bytes al resto. |
| **¿Qué escala mal?** | El servidor. Con N usuarios recibe N streams y retransmite aproximadamente N(N−1) flujos. |
| **¿Qué pruebas hicieron realmente?** | Pruebas funcionales/integración manual de conexión, chat, video y audio; no deberían venderlas como pruebas unitarias o de carga automatizadas. |

## Tres cosas del código que yo arreglaría o, como mínimo, tendría preparadas

La primera es importante. `Paquete` tiene un comentario que lo llama **inmutable**, pero contiene un `byte[]` y `datos()` devuelve directamente ese mismo arreglo. Técnicamente no es completamente inmutable. Un evaluador suficientemente quisquilloso puede detectarlo. La defensa correcta no es inventar: “En esta versión no hacemos copia defensiva; para garantizar inmutabilidad estricta deberíamos usar `Arrays.copyOf` al recibir y devolver el arreglo”.

La segunda está en `SalaLlamada.unir()`. Usan un `ConcurrentHashMap`, pero “consultar nombres → notificar → hacer put” es una **secuencia de varias operaciones**. Dos usuarios que entren exactamente a la vez pueden intercalar esos pasos. No digan que el `ConcurrentHashMap` vuelve atómica toda la operación. Si tienen tiempo antes de entregar, yo revisaría esta sección.

La tercera es mucho más sencilla: `TIEMPO_CONEXION_MS = 6767`. Funciona, pero parece sospechosamente elegido porque coincide con el puerto. Si no existe una razón específica para **6767 ms**, yo usaría un timeout convencional como 5000 ms o estaría preparado para decir que es un parámetro configurable y que ese valor no tiene significado de protocolo.

Y una última pauta que probablemente sea la más importante para la nota: **cada afirmación debe poder apuntar a una línea del código o a una acción de la demo**. El material del profesor constantemente plantea “qué es, para qué sirve y cómo se entiende en un caso”, no solo definiciones. :chatgpt-content-reference{index="10"} Su video debería hacer exactamente eso.

Si dicen “usamos polimorfismo”, inmediatamente muestran `FuenteVideo`. Si dicen “evitamos una condición de carrera”, muestran `GeneradorIds` o `putIfAbsent`. Si dicen “tenemos productor-consumidor”, muestran `ColaSalida`. Si dicen “nuestro protocolo distingue multimedia”, muestran `CodecPaquete`. Si dicen “el programa funciona”, ejecutan la acción.

Con ese enfoque, aun si el corrector intenta buscar huecos, la defensa deja de depender de memorizar 40 clases: ustedes entienden **cuatro historias completas** —conexión, mensaje, video y audio— y saben justificar las decisiones técnicas que las conectan.