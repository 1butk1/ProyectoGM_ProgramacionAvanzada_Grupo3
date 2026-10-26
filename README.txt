# Salvation Rain

Videojuego arcade desarrollado en **Java + LibGDX 1.11.0** por el Grupo 3 para el ramo de Programación Avanzada (PUCV).

El agua del planeta se acabó y el ataque de los bandidos me separó de mi grupo. Desde entonces, camino solo con lo único que me queda: un humilde balde.
Me refugio en un bosque rodeado de árboles y leña, aprovechando que el cielo se nubla. Aquí, la lluvia es una bendición y una trampa: mientras recojo agua limpia para sobrevivir, debo esquivar gotas ácidas que queman como el sulfúrico.
Entre unos arbustos hallo el cadáver de un científico y un cuaderno. Una nota dice: *«Benditos aquellos que encuentren el manantial de agua cristalina, donde la preocupación por sobrevivir no existe y la juventud prospera»*.
Junto al texto, un mapa señala una cueva cerca de una planta nuclear abandonada. Sin nada que perder, aprieto mi balde y me pongo en marcha.

## Integrantes

- Diego Aguilar
- Matia Rios
- Ignacio Rodriguez

---


## Ejecutar con Gradle (recomendado)

Abre una terminal en la carpeta raíz del proyecto (donde está `build.gradle`) y ejecuta:

**Windows**
```bat
gradlew.bat desktop:run
```

**macOS / Linux**
```bash
chmod +x gradlew      # solo la primera vez
./gradlew desktop:run
```

La primera ejecución tarda un poco más porque descarga las dependencias.

### Generar un .jar ejecutable

```bash
./gradlew desktop:dist
java -jar desktop/build/libs/desktop-1.0.jar
```

El `.jar` ya incluye los assets. En macOS se ejecuta con `java -XstartOnFirstThread -jar desktop/build/libs/desktop-1.0.jar`.

---

## Ejecutar desde Eclipse

1. **Cambiar el encoding a UTF-8** (para que los acentos y la ñ se vean bien):
   `Window → Preferences → General → Workspace → Text file encoding → Other → UTF-8`
2. **Importar el proyecto:**
   `File → Import → Gradle → Existing Gradle Project` → seleccionar la carpeta raíz → `Finish`.
   Aparecen el proyecto raíz y los subproyectos `ProyectoGM_Grupo3-core` y `ProyectoGM_Grupo3-desktop`.
3. **Ejecutar:** en `ProyectoGM_Grupo3-desktop`, abre `src/com/mygdx/game/DesktopLauncher.java` → clic derecho → `Run As → Java Application`.
4. **Si aparece un error de assets** (por ejemplo `File not found: bucket.png`):
   `Run → Run Configurations → DesktopLauncher → Arguments → Working directory → Other` y selecciona la carpeta `assets` del proyecto.

En macOS, agrega `-XstartOnFirstThread` en `Run Configurations → Arguments → VM arguments`.

---

## Cómo se juega

Atrapa las gotas de agua limpia con tu balde para sumar puntos. Empiezas con **3 vidas** (máximo 10) y pierdes una cada vez que te golpea algo contaminado. La partida termina cuando te quedas sin vidas.

### Modos de juego

| Modo | Descripción |
| --- | --- |
| **Historia** | Recorres las 5 zonas en orden. Cambias de zona al llegar a 150, 350, 600 y 900 gotas, y ganas al juntar **450 gotas**, lo que muestra la cinemática final. Las zonas que alcanzas quedan desbloqueadas en el selector de niveles. |
| **Infinito** | Las zonas se repiten en ciclo sin fin. Cada vuelta completa la lluvia es un 12 % más rápida (hasta un 60 %). Tiene su propio récord. |

El récord solo se registra en partidas iniciadas desde el nivel 1, y se mantiene mientras el juego esté abierto.

### Controles

**En partida**

| Acción | Teclas |
| --- | --- |
| Moverse | `←` `→` o `A` `D` (o el mouse, si lo activas en la pausa) |
| Saltar | `Espacio`, `↑` o `W` |
| Pausa | `ESC` o `P` |

**Menú de pausa**

| Acción | Tecla |
| --- | --- |
| Continuar | `ESC` |
| Reiniciar partida | `R` |
| Cambiar control teclado / mouse | `C` |
| Volver al menú principal | `M` |

**Menús** (todos los botones también funcionan con clic)

| Pantalla | Teclas |
| --- | --- |
| Menú principal | `ENTER` jugar · `O` opciones · `ESC` salir |
| Selección de modo | `H` historia · `I` infinito · `ESC` volver |
| Selector de niveles | Flechas o `WASD` para moverse · `ENTER` / `Espacio` jugar · `ESC` volver |
| Opciones | Arrastra las barras de música y efectos con el mouse · `ESC` guardar y volver |
| Game Over | `R` / `ENTER` reiniciar · `M` / `ESC` menú principal |
| Cinemáticas | `Espacio` / `ENTER` / clic avanzar · `ESC` saltar |

### Elementos del juego

| Elemento | Efecto |
| --- | --- |
| Gota de agua | +10 puntos. |
| Gota tóxica | Quita 1 vida. |
| Gota escudo | Te protege del próximo golpe. |
| Gota negra | Te ralentiza 3 segundos. Si cae al suelo deja un charco de petróleo que te frena mientras lo pisas. |
| Gota dorada | +50 puntos y +1 vida. Aparece cada 100 puntos, solo si no te golpeó nada contaminado desde la anterior. |
| Buitre | Se lanza en picada y te bota el balde. Tienes 6 segundos para recogerlo o pierdes una vida. Sin balde no puedes atrapar gotas buenas, pero corres un 20 % más rápido. |
| Oleada tóxica | Una fila de gotas tóxicas con un solo hueco libre por donde pasar. |

Además, el **viento** desvía las gotas hacia los lados, y en la **Mina** la visión queda limitada a un círculo alrededor del jugador.

---

## Problemas frecuentes

| Problema | Solución |
| --- | --- |
| `Unsupported class file major version 65` u otro error de Gradle al iniciar | Estás usando Java 21 o superior. Instala el JDK 17 y configura `JAVA_HOME`. |
| `File not found: ...png` o `...mp3` | El juego no encuentra la carpeta `assets`. Ejecuta con `gradlew desktop:run` o configura el *Working directory* en Eclipse (paso 4). |
| Letras raras en vez de acentos | Cambia el encoding del workspace a UTF-8. |
| La ventana no abre en macOS | Agrega el argumento de JVM `-XstartOnFirstThread`. |
| Quiero reiniciar los niveles desbloqueados | Borra el archivo `.prefs/ProyectoGM_progreso` en tu carpeta de usuario (Windows: `C:\Users\<usuario>\.prefs\`, macOS/Linux: `~/.prefs/`). El volumen se guarda en `.prefs/salvationrain`. |

---

Hecho con [libGDX](https://libgdx.com/).