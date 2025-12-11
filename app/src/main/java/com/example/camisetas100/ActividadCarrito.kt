package com.example.camisetas100

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Activity que muestra el contenido del carrito de compras.
 * Hereda de ActividadBase para obtener automáticamente el estilo edge-to-edge.
 */
class ActividadCarrito : ActividadBase() {

    // Referencias a las vistas del layout que necesitaremos manipular.
    private lateinit var listaCarritoRecyclerView: RecyclerView
    private lateinit var adaptadorCarrito: AdaptadorCarrito // Usamos nuestro nuevo adaptador
    private lateinit var mensajeCarritoVacio: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart) // Establecemos el layout para esta Activity

        // Configuramos el RecyclerView y sus componentes.
        configurarRecyclerView()
        // Empezamos a "observar" los cambios en el carrito de compras.
        observarProductosDelCarrito()

        // Buscamos el botón de vaciar carrito y le asignamos una acción al pulsarlo.
        findViewById<Button>(R.id.boton_vaciar_carrito).setOnClickListener {
            showConfirmationDialog()
        }
    }

    /**
     * Inicializa el RecyclerView, su LayoutManager y su adaptador.
     */
    private fun configurarRecyclerView() {
        // Obtenemos las referencias a las vistas desde el layout XML.
        listaCarritoRecyclerView = findViewById(R.id.lista_productos_carrito)
        mensajeCarritoVacio = findViewById(R.id.mensaje_carrito_vacio)

        // 1. LayoutManager: Le dice al RecyclerView cómo posicionar los elementos (en este caso, como una lista vertical).
        listaCarritoRecyclerView.layoutManager = LinearLayoutManager(this)
        // 2. Adaptador: Creamos una instancia de nuestro adaptador, inicialmente con una lista vacía.
        adaptadorCarrito = AdaptadorCarrito(listOf())
        // 3. Asignamos el adaptador al RecyclerView.
        listaCarritoRecyclerView.adapter = adaptadorCarrito
    }

    /**
     * Configura un observador en el LiveData del RepositorioCarrito.
     * Este código se ejecutará automáticamente cada vez que la lista de productos en el carrito cambie.
     */
    private fun observarProductosDelCarrito() {
        // 'this' como primer argumento indica que el observador está ligado al ciclo de vida de esta Activity.
        // De esta forma, Android se encarga de activarlo o desactivarlo automáticamente.
        RepositorioCarrito.productosEnCarrito.observe(this) { productos ->
            // Comprobamos si la lista de productos que nos llega es nula o está vacía.
            if (productos.isNullOrEmpty()) {
                // Si está vacía, ocultamos la lista y mostramos el mensaje de "El carrito está vacío".
                listaCarritoRecyclerView.visibility = View.GONE
                mensajeCarritoVacio.visibility = View.VISIBLE
            } else {
                // Si tiene productos, mostramos la lista y ocultamos el mensaje.
                listaCarritoRecyclerView.visibility = View.VISIBLE
                mensajeCarritoVacio.visibility = View.GONE
                // Le pasamos la nueva lista al adaptador para que la dibuje.
                adaptadorCarrito.actualizarProductos(productos)
            }
        }
    }

    /**
     * Muestra el diálogo de confirmación para vaciar el carrito.
     */
    private fun showConfirmationDialog() {
        // Creamos una instancia de nuestro DialogFragment personalizado.
        // Le pasamos una función lambda que se ejecutará si el usuario confirma la acción.
        val dialogo = DialogoConfirmarVaciarCarrito {
            // La acción a ejecutar es llamar al método para vaciar el carrito en nuestro repositorio.
            RepositorioCarrito.vaciarCarrito()
        }
        // Mostramos el diálogo. 'supportFragmentManager' es el gestor de fragmentos de la Activity.
        dialogo.show(supportFragmentManager, "DialogoConfirmarVaciarCarrito")
    }
}
