# Proyecto Final PMDM - Tienda de Camisetas

Este es el proyecto final para la asignatura de Programación Multimedia y Dispositivos Móviles (PMDM). Se trata de una aplicación Android nativa que simula una tienda online de camisetas, desarrollada en Kotlin y siguiendo las directrices de Material Design.

## 📋 Características Implementadas

La aplicación cumple con todos los requisitos obligatorios del proyecto, incluyendo:

*   **Pantalla Principal:** Muestra una lista de productos con un `RecyclerView` y tarjetas personalizadas (`MaterialCardView`). Incluye un banner superior y botones de navegación.
*   **Navegación entre Pantallas:** Uso de `Intents` para navegar entre las distintas `Activities` de la aplicación.
*   **Pantalla de Detalles:** Al pulsar en un producto, se abre una nueva pantalla con su imagen en grande, descripción y precio.
*   **Carrito de la Compra:**
    *   Se pueden añadir productos al carrito desde la pantalla de detalles.
    *   La acción de añadir muestra un `Snackbar` con una opción para **"Deshacer"**.
    *   Una pantalla dedicada muestra la lista de productos en el carrito.
    *   Incluye un botón para vaciar el carrito que lanza un `DialogFragment` de confirmación.
*   **Notificaciones:**
    *   Un `FloatingActionButton` en la pantalla principal permite lanzar una notificación push.
    *   La notificación avisa de una "Nueva oferta disponible" y, al tocarla, abre la pantalla principal de la app (`PendingIntent`).
*   **Arquitectura y Buenas Prácticas:**
    *   **Actividad Base:** Se utiliza una `ActividadBase` para contener código común, como la implementación del modo *edge-to-edge* (pantalla completa).
    *   **Gestión de Estado:** Se usa un Singleton (`object RepositorioCarrito`) para gestionar el estado del carrito de forma centralizada y accesible desde toda la app.
    *   **Modelo de Datos:** La clase `Producto` es una `data class` que implementa `Parcelable` para pasar objetos entre Activities de forma eficiente.

## 🛠️ Tecnologías y Componentes Utilizados

*   **Lenguaje:** 100% Kotlin.
*   **Layouts:**
    *   `ConstraintLayout` para la mayoría de las pantallas, permitiendo interfaces complejas y flexibles.
    *   `ScrollView` y `HorizontalScrollView` para contenido que no cabe en la pantalla.
*   **Componentes de Material Design:**
    *   `RecyclerView` con `ViewHolder` y `Adapter` personalizados.
    *   `MaterialCardView` para las tarjetas de producto.
    *   `FloatingActionButton` (FAB).
    *   `Snackbar` y `Toast` para feedback al usuario.
*   **Ciclo de Vida y Navegación:**
    *   Múltiples `Activities`.
    *   Paso de datos con `Intents` y `Parcelable`.
    *   Gestión de permisos en tiempo de ejecución (Notificaciones en Android 13+).
*   **Diálogos y Notificaciones:**
    *   `DialogFragment` para diálogos de confirmación.
    *   `NotificationChannel` y `NotificationCompat` para notificaciones push.

## 🚀 Cómo Ejecutar

1.  Clonar este repositorio.
2.  Abrir el proyecto con Android Studio.
3.  Sincronizar el proyecto con Gradle.
4.  Ejecutar en un emulador o dispositivo físico.

---
*Realizado por Juanjo para el curso 2º DAM.*