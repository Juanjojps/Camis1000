package com.example.camisetas100

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/**
 * Una Activity base abstracta que contiene la lógica común para todas las Activities de la app.
 * Heredar de esta clase nos permite aplicar un comportamiento consistente (como el modo edge-to-edge)
 * a todas las pantallas sin repetir código.
 *
 * Es 'abstract' porque no se puede crear una instancia directa de ella, solo se puede heredar.
 */
abstract class ActividadBase : AppCompatActivity() {

    /**
     * Se llama cuando se crea la Activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hacemos que la app se dibuje a pantalla completa, ocupando también el espacio
        // de la barra de estado (arriba) y la barra de navegación (abajo).
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    /**
     * Sobrescribimos el método que establece el layout de la Activity.
     * Esto nos permite ejecutar nuestro código de ajuste de padding justo después de que
     * el layout XML haya sido "inflado" y esté listo en la pantalla.
     */
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        // Llamamos a nuestro método para aplicar los paddings después de establecer el contenido.
        aplicarMargenesEdgeToEdge()
    }

    /**
     * Este método se encarga de aplicar un padding a la vista raíz para que el contenido
     * principal de la app no quede oculto debajo de las barras del sistema.
     */
    private fun aplicarMargenesEdgeToEdge() {
        // Obtenemos la vista raíz del contenido de la Activity (el contenedor principal del layout).
        // android.R.id.content es un ID que Android le da al contenedor raíz de cualquier layout de Activity.
        val vistaRaiz = findViewById<View>(android.R.id.content)

        // Añadimos un listener que "escucha" los cambios en los "insets" del sistema.
        // Los insets son los espacios que ocupan las barras del sistema (status bar, navigation bar).
        ViewCompat.setOnApplyWindowInsetsListener(vistaRaiz) { vista, margenes ->
            // Obtenemos las dimensiones de las barras del sistema.
            val barrasSistema = margenes.getInsets(WindowInsetsCompat.Type.systemBars())

            // Aplicamos un padding a nuestra vista raíz. El padding será igual al tamaño
            // de las barras, empujando nuestro contenido para que no quede solapado.
            vista.updatePadding(
                left = barrasSistema.left,
                top = barrasSistema.top,
                right = barrasSistema.right,
                bottom = barrasSistema.bottom
            )
            // Devolvemos los insets originales para que el sistema continúe su flujo normal.
            margenes
        }
    }
}
