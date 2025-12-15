package com.example.camisetas100

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Activity que muestra el contenido del carrito de compras.
 * Hereda de ActividadBase para obtener automáticamente el estilo edge-to-edge.
 */
class Carrito : ActividadBase() {

    // Referencias a las vistas del layout que necesitaremos manipular.
    private lateinit var listaCarritoRecyclerView: RecyclerView
    private lateinit var adaptadorCarrito: AdaptadorCarrito
    private lateinit var mensajeCarritoVacio: TextView
    private lateinit var botonVaciar: Button
    private lateinit var botonComprar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Inicializamos las vistas que vamos a necesitar.
        inicializarVistas()

        // Configuramos el RecyclerView y sus componentes.
        configurarRecyclerView()
        // Empezamos a "observar" los cambios en el carrito de compras.
        observarProductosDelCarrito()

        // Asignamos las acciones a los botones.
        configurarBotones()
    }

    /**
     * Obtiene las referencias a las vistas del layout XML.
     */
    private fun inicializarVistas() {
        listaCarritoRecyclerView = findViewById(R.id.lista_productos_carrito)
        mensajeCarritoVacio = findViewById(R.id.mensaje_carrito_vacio)
        botonVaciar = findViewById(R.id.boton_vaciar_carrito)
        botonComprar = findViewById(R.id.boton_comprar)
    }

    /**
     * Inicializa el RecyclerView, su LayoutManager y su adaptador.
     */
    private fun configurarRecyclerView() {
        listaCarritoRecyclerView.layoutManager = LinearLayoutManager(this)
        adaptadorCarrito = AdaptadorCarrito(listOf())
        listaCarritoRecyclerView.adapter = adaptadorCarrito
    }

    /**
     * Asigna los listeners de clic a los botones de la pantalla.
     */
    private fun configurarBotones() {
        botonVaciar.setOnClickListener {
            mostrarDialogoConfirmacion()
        }

        botonComprar.setOnClickListener {
            // Mostramos el Toast de confirmación.
            Toast.makeText(this, "Artículos comprados correctamente", Toast.LENGTH_LONG).show()
            // Vaciamos el carrito después de la compra.
            RepositorioCarrito.vaciarCarrito()
        }
    }

    /**
     * Configura un observador en el LiveData del RepositorioCarrito.
     * Este código se ejecutará automáticamente cada vez que la lista de productos en el carrito cambie.
     */
    private fun observarProductosDelCarrito() {
        RepositorioCarrito.productosEnCarrito.observe(this) { productos ->
            val carritoEstaVacio = productos.isNullOrEmpty()

            if (carritoEstaVacio) {
                // Si está vacío, ocultamos la lista y mostramos el mensaje de "El carrito está vacío".
                listaCarritoRecyclerView.visibility = View.GONE
                mensajeCarritoVacio.visibility = View.VISIBLE
            } else {
                // Si tiene productos, mostramos la lista y ocultamos el mensaje.
                listaCarritoRecyclerView.visibility = View.VISIBLE
                mensajeCarritoVacio.visibility = View.GONE
                // Le pasamos la nueva lista al adaptador para que la dibuje.
                adaptadorCarrito.actualizarProductos(productos)
            }
            
            // Habilitamos o deshabilitamos los botones según si el carrito está vacío o no.
            botonComprar.isEnabled = !carritoEstaVacio
            botonVaciar.isEnabled = !carritoEstaVacio
        }
    }

    /**
     * Muestra el diálogo de confirmación para vaciar el carrito.
     */
    private fun mostrarDialogoConfirmacion() {
        val dialogo = DialogoConfirmarVaciarCarrito {
            RepositorioCarrito.vaciarCarrito()
        }
        dialogo.show(supportFragmentManager, "DialogoConfirmarVaciarCarrito")
    }
}
