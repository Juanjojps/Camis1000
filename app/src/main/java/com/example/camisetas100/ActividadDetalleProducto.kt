package com.example.camisetas100

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

/**
 * Activity que muestra los detalles de un único producto.
 * Hereda de ActividadBase para obtener automáticamente el estilo edge-to-edge.
 */
class ActividadDetalleProducto : ActividadBase() {

    // Variable para guardar el producto que estamos mostrando. Es nullable (?) porque al principio no tenemos producto.
    private var productoActual: Producto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        // Recuperamos el objeto Producto que la Activity anterior nos envió a través del Intent.
        recuperarProductoDelIntent()

        // Si hemos recuperado el producto correctamente, rellenamos la interfaz con sus datos.
        productoActual?.let {
            rellenarDatosDelProducto(it)
        }

        // Asignamos la acción al botón "Añadir al carrito".
        findViewById<Button>(R.id.boton_anadir_al_carrito).setOnClickListener {
            // Nos aseguramos de que el producto no es nulo antes de intentar añadirlo.
            productoActual?.let { producto ->
                anadirProductoAlCarrito(producto)
            }
        }
    }

    /**
     * Recupera el objeto Producto empaquetado en el Intent que inició esta Activity.
     */
    private fun recuperarProductoDelIntent() {
        // La forma de recuperar un objeto Parcelable cambió en Android 13 (API 33, Tiramisu).
        // Este bloque if/else asegura que usemos el método correcto según la versión de Android del dispositivo.
        productoActual = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_PRODUCTO, Producto::class.java)
        } else {
            // Para versiones anteriores, se usa el método obsoleto.
            // La anotación @Suppress evita que Android Studio nos muestre un warning por usar un método obsoleto.
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_PRODUCTO)
        }
    }

    /**
     * Rellena las vistas de la pantalla con los datos del producto recibido.
     * @param producto El producto cuyos datos se van a mostrar.
     */
    private fun rellenarDatosDelProducto(producto: Producto) {
        findViewById<ImageView>(R.id.imagen_detalle_producto).setImageResource(producto.idRecursoImagen)
        findViewById<TextView>(R.id.nombre_detalle_producto).text = producto.nombre
        findViewById<TextView>(R.id.precio_detalle_producto).text = "€${producto.precio}"
        findViewById<TextView>(R.id.descripcion_detalle_producto).text = producto.descripcion

        // También establecemos el nombre del producto como título en la barra superior (ActionBar).
        title = producto.nombre
    }

    /**
     * Añade el producto actual al carrito y muestra una confirmación con un Snackbar.
     * @param producto El producto que se va a añadir.
     */
    private fun anadirProductoAlCarrito(producto: Producto) {
        // Usamos nuestro repositorio para añadir el producto.
        RepositorioCarrito.agregarProducto(producto)

        // Creamos y mostramos un Snackbar para notificar al usuario.
        // Un Snackbar es una barra de mensaje temporal que aparece en la parte inferior de la pantalla.
        Snackbar.make(findViewById(android.R.id.content), "'${producto.nombre}' añadido al carrito", Snackbar.LENGTH_LONG)
            .setAction("Deshacer") { 
                // El método setAction define un botón de acción en el Snackbar.
                // Si el usuario pulsa "Deshacer", ejecutamos este bloque de código.
                RepositorioCarrito.eliminarProducto(producto)
            }
            .show()
    }

    /**
     * Un 'companion object' es similar a los miembros estáticos en Java. Permite definir constantes
     * o métodos que pertenecen a la clase en sí, no a una instancia de ella.
     */
    companion object {
        // Definimos una constante para la clave que usaremos para pasar el producto en el Intent.
        // Es una buena práctica usar constantes para estas claves para evitar errores de escritura.
        const val EXTRA_PRODUCTO = "ID_PRODUCTO"
    }
}
