# Optimización de SmartTube · Mikel

## Cambios

- Glide conserva una pantalla de caché y una pantalla de bitmaps reutilizables en dispositivos con hasta 2 GiB de RAM o con el indicador low-RAM. El cálculo respeta un límite conjunto del 15 % del heap de la aplicación; las imágenes visibles activas y otros componentes consumen memoria adicional.
- Las cargas alternativas de miniaturas usan las mismas dimensiones que la carga principal.
- Los canales cancelan las peticiones Glide al reciclarse. Las cabeceras cancelan también la petición anterior al sustituir una imagen remota por un icono local.
- Variante `optimized`: sin depuración, R8 y reducción de recursos. Conserva J2V8 porque su biblioteca nativa carga clases y callbacks por nombre.
- Sin modificaciones en layouts, colores, animaciones ni resolución de vídeo. No se fuerza un códec ni un límite de resolución sin medir el dispositivo real.

## Validación

La compilación inicial reveló un cierre nativo por eliminación de `com.eclipsesource.v8.inspector.V8Inspector`. Se conserva el paquete J2V8 completo para proteger las referencias desde JNI.

Las cifras de rendimiento en la CHiQ L32QM9G están pendientes de medición física. Un tamaño de APK menor no equivale a un porcentaje de mejora en velocidad o RAM.

También se conservan los componentes Leanback usados por reflexión, los modelos y conversores del servicio y los mensajes Protobuf Lite del reproductor. Las pruebas detectaron y permitieron corregir tanto el estrechamiento del tipo de un listener como la eliminación de campos de los modelos y del protocolo SABR.

## Resultado de la versión 32.44-mikel.1

- `assembleStfdroidOptimized`: compilación y lint vital correctos (751 tareas).
- Instalación sobre la APK anterior en Android TV 34 con aproximadamente 2 GiB de RAM, conservando los datos; paquete sin DEBUGGABLE.
- Inicio y búsqueda con resultados e imágenes, reproducción con posición avanzando de 12,2 a 18,3 segundos y estado PLAYING sin error. Comprobación de retorno a navegación.
- Sin entradas de cierre en el buffer de errores tras la instalación final.
- Firma ARM verificada y coincidente con la anterior; firma x86 verificada.
- APK ARM universal: 32.534.105 bytes frente a 44.309.800 bytes anteriores (26,6 % menos). APK x86: 22.766.690 bytes.
- Persisten avisos de compilación heredados, incluidos adaptadores DNS para clases de Java de escritorio ausentes en Android. No se han ocultado los avisos.

No se ha probado todavía en la CHiQ física ni se ha hecho una prueba completa con cuentas autenticadas. Las capturas del README ilustran la interfaz, no constituyen una medición de rendimiento.
