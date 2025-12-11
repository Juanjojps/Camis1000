package com.example.camisetas100

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptador para el RecyclerView que muestra la lista de productos en la pantalla principal.
 * Su funcionamiento es muy similar al AdaptadorCarrito, pero este gestiona también
 * el clic sobre un elemento para navegar a la pantalla de detalles.
 *
 * @param productos La lista inicial de productos a mostrar.
 * @param alPulsarProducto Es una función "lambda" que se ejecutará cuando el usuario pulse
 *                       en uno de los productos de la lista. Recibe el Producto pulsado
 *                       como parámetro para que quien use el adaptador sepa qué hacer con él
 *                       (por ejemplo, abrir la pantalla de detalles de ese producto).
 */
class AdaptadorProducto(
    private val productos: List<Producto>,
    private val alPulsarProducto: (Producto) -> Unit // Usamos la nueva clase 'Producto'
) : RecyclerView.Adapter<AdaptadorProducto.ProductoViewHolder>() {

    /**
     * Crea una nueva vista (ViewHolder) para un elemento de la lista.
     * Se llama cuando el RecyclerView necesita una nueva vista para dibujar un producto.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductoViewHolder(vista)
    }

    /**
     * Rellena (enlaza) una vista (ViewHolder) con los datos del producto en una posición específica.
     */
    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.enlazar(producto)
    }

    /**
     * Devuelve el número total de productos en la lista.
     */
    override fun getItemCount(): Int = productos.size

    /**
     * ViewHolder para un producto. Contiene las referencias a las vistas dentro de la tarjeta del producto.
     * También gestiona la asignación del listener de clic.
     */
    inner class ProductoViewHolder(vistaItem: View) : RecyclerView.ViewHolder(vistaItem) {
        // Referencias a las vistas del layout item_product.xml
        private val nombreProducto: TextView = vistaItem.findViewById(R.id.nombre_producto)
        private val precioProducto: TextView = vistaItem.findViewById(R.id.precio_producto)
        private val imagenProducto: ImageView = vistaItem.findViewById(R.id.imagen_producto)

        /**
         * Rellena las vistas con los datos de un producto y configura el listener de clic.
         * @param producto El producto a mostrar.
         */
        fun enlazar(producto: Producto) {
            nombreProducto.text = producto.nombre
            precioProducto.text = "€${producto.precio}"
            imagenProducto.setImageResource(producto.idRecursoImagen)

            // Asignamos el listener de clic a toda la vista de la tarjeta (el item completo).
            // Cuando se pulse, se ejecutará la función lambda 'alPulsarProducto'
            // que nos pasaron al crear el adaptador.
            itemView.setOnClickListener { alPulsarProducto(producto) }
        }
    }
}
