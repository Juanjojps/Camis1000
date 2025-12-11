package com.example.camisetas100

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptador para el RecyclerView que muestra la lista de productos en el carrito.
 *
 * Un adaptadro de RecyclerView es el componente que conecta los datos (la lista de productos)
 * con la interfaz de usuario (el RecyclerView). Se encarga de crear las vistas para cada
 * elemento de la lista y de "rellenar" esas vistas con los datos correspondientes.
 *
 * @param productosDelCarrito La lista inicial de productos a mostrar.
 */
class AdaptadorCarrito(
    private var productosDelCarrito: List<Producto> // Usamos la nueva clase 'Producto'
) : RecyclerView.Adapter<AdaptadorCarrito.CarritoViewHolder>() {

    /**
     * Se llama cuando el RecyclerView necesita crear una nueva vista para un elemento.
     * @param parent El ViewGroup al que se añadirá la nueva vista (el propio RecyclerView).
     * @param viewType El tipo de vista (útil cuando hay varios tipos de elementos en la lista).
     * @return Una nueva instancia de CarritoViewHolder que contiene el layout inflado para un elemento.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        // "Inflar" significa convertir un archivo de layout XML en objetos View de Kotlin/Java.
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_product, parent, false) // Usamos el layout para un item del carrito
        return CarritoViewHolder(vista)
    }

    /**
     * Se llama cuando el RecyclerView quiere mostrar (o "enlazar") los datos de un producto
     * en una vista (ViewHolder) concreta.
     * @param holder El ViewHolder que debe ser actualizado para representar el contenido del elemento
     *               en la posición dada en el conjunto de datos.
     * @param position La posición del elemento dentro del conjunto de datos del adaptador.
     */
    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        // Obtenemos el producto de la lista en la posición actual
        val producto = productosDelCarrito[position]
        // Llamamos al método 'enlazar' del ViewHolder para que rellene la vista con los datos del producto.
        holder.enlazar(producto)
    }

    /**
     * Devuelve el número total de elementos en la lista de datos.
     * El RecyclerView lo usa para saber cuántos elementos necesita dibujar.
     * @return El tamaño de la lista de productos del carrito.
     */
    override fun getItemCount(): Int = productosDelCarrito.size

    /**
     * Método público para actualizar la lista de productos que muestra el adaptador.
     * @param nuevosProductos La nueva lista de productos.
     */
    fun actualizarProductos(nuevosProductos: List<Producto>) {
        productosDelCarrito = nuevosProductos
        // Notifica al RecyclerView que los datos han cambiado para que vuelva a dibujar la lista.
        // notifyDataSetChanged() es simple pero ineficiente. Más adelante se puede mejorar con DiffUtil.
        notifyDataSetChanged()
    }

    /**
     * Clase interna que representa la vista de un único elemento en la lista del RecyclerView.
     * "ViewHolder" significa literalmente "contenedor de vistas". Su trabajo es "sujetar"
     * las referencias a las vistas internas del layout (TextViews, ImageView, etc.) para
     * evitar tener que buscarlas con findViewById() cada vez, lo cual es costoso.
     *
     * @param vistaItem La vista raíz del layout de un elemento de la lista (el ConstraintLayout en este caso).
     */
    inner class CarritoViewHolder(vistaItem: View) : RecyclerView.ViewHolder(vistaItem) {
        // Guardamos las referencias a las vistas que vamos a necesitar manipular.
        // Los ID deben coincidir con los que definamos en el archivo item_cart_product.xml
        private val nombreProducto: TextView = vistaItem.findViewById(R.id.nombre_producto_carrito)
        private val precioProducto: TextView = vistaItem.findViewById(R.id.precio_producto_carrito)
        private val imagenProducto: ImageView = vistaItem.findViewById(R.id.imagen_producto_carrito)

        /**
         * Rellena las vistas del ViewHolder con los datos de un objeto Producto.
         * @param producto El producto cuyos datos se van a mostrar.
         */
        fun enlazar(producto: Producto) {
            nombreProducto.text = producto.nombre
            precioProducto.text = "€${producto.precio}"
            imagenProducto.setImageResource(producto.idRecursoImagen)
        }
    }
}
