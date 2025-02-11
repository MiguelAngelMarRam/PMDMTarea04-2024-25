# Spyro The Dragon - Guía Interactiva y Easter Eggs

## Introducción

Este proyecto actualiza una aplicación inspirada en *Spyro the Dragon* para hacerla más atractiva y fácil de usar. Se han implementado dos elementos clave:

- **Guía de inicio interactiva**: Una serie de pantallas que explican las principales funcionalidades de la app con animaciones, sonidos y elementos visuales.
- **Easter Eggs**: Se han agregado dos easter eggs ocultos que aportan un extra de interactividad y sorpresa a la aplicación.

## Características Principales

### Guía de Inicio Interactiva

La guía se compone de seis pantallas diseñadas en XML, que explican paso a paso la aplicación:

1. **Pantalla de Bienvenida**: Presentación de la guía con un diseño inspirado en *Spyro the Dragon* y un botón para iniciar la guía.
2. **Pantalla de Personajes**: Explicación sobre la sección de personajes con un bocadillo animado.
3. **Pantalla de Mundos**: Destaca los mundos disponibles dentro del juego con un bocadillo informativo animado.
4. **Pantalla de Coleccionables**: Muestra la sección de coleccionables y sus funciones.
5. **Pantalla de Información**: Explicación sobre el icono de información en la *Action Bar*.
6. **Pantalla de Cierre**: Resumen de la guía y botón para comenzar a usar la aplicación.

### Navegación y Experiencia de Usuario

- **Avance en la guía**: Cada pantalla tiene un botón para avanzar a la siguiente.
- **Omitir la guía**: Se incluye la opción de saltar la guía en cualquier momento.
- **Bloqueo de interacción**: Se impide la interacción con la app mientras la guía está activa.
- **Mostrado único**: La guía solo se muestra la primera vez que se abre la app, utilizando *SharedPreferences*.

### Animaciones y Sonidos

- **Animaciones en los bocadillos**: Los mensajes informativos incluyen efectos visuales para mejorar la experiencia.
- **Transiciones suaves**: Se han implementado efectos de transición entre pantallas.
- **Efectos de sonido**: Se reproducen sonidos temáticos al interactuar con la guía.

### Easter Eggs

- **Easter Egg con Video**: Se activa al pulsar cuatro veces consecutivas sobre las gemas en la pestaña de coleccionables, reproduciendo un video temático a pantalla completa.
- **Easter Egg con Animación**: Se activa al realizar una pulsación prolongada sobre Spyro en la pestaña de personajes, mostrando una animación con *Canvas* simulando una llama de fuego.

## Tecnologías Utilizadas

- **Android Studio**
- **Java**
- **XML para diseño de interfaces**
- **SharedPreferences**
- **Canvas**
- **MediaPlayer** (para la reproducción de sonidos y videos)

## Instrucciones de Uso

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/MiguelAngelMarRam/PMDMTarea04-2024-25.git
   ```
2. Abrir el proyecto en Android Studio.
3. Ejecutar la aplicación en un emulador o dispositivo físico.

## Conclusiones del Desarrollador

Este proyecto ha sido una gran oportunidad para mejorar habilidades en el desarrollo de interfaces de usuario, animaciones y manejo de eventos en Android. La implementación de los *Easter Eggs* ha sido un reto interesante que ha permitido profundizar en el uso de *Canvas* y *MediaPlayer*. Como posible mejora futura, se podría incluir una mayor personalización de la guía según las preferencias del usuario.

## Capturas de Pantalla
# Spyro The Dragon - Guía Interactiva y Easter Eggs
