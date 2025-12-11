package com.example.camisetas100

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Un Singleton que funciona como un repositorio central para gestionar el estado del carrito de compras.
 *
 * Un Singleton (declarado con la palabra clave 'object' en Kotlin) es un patrón de diseño que garantiza
 * que solo exista una única instancia de esta clase en toda la aplicación. Esto es perfecto para
 * gestionar un estado global, como el carrito, ya que todas las Activities y componentes
 * accederán a la misma lista de productos.
 */
object RepositorioCarrito {

    // _productosEnCarrito es un MutableLiveData. Es "mutable" porque podemos cambiar su valor (añadir o quitar productos).
    // Lo hacemos privado para que solo este repositorio pueda modificar la lista directamente.
    // LiveData es un contenedor de datos observable. Notifica a sus observadores (como las Activities)
    // cuando los datos que contiene cambian.
    private val _productosEnCarrito = MutableLiveData<MutableList<Producto>>(mutableListOf()) // Usamos la nueva clase 'Producto'

    // productosEnCarrito es la versión pública y "no mutable" (LiveData en lugar de MutableLiveData).
    // Las Activities observarán esta variable para recibir actualizaciones, pero no podrán modificarla,
    // asegurando que toda la lógica de modificación se mantenga dentro de este repositorio.
    val productosEnCarrito: LiveData<MutableList<Producto>> = _productosEnCarrito

    /**
     * Añade un producto a la lista del carrito.
     * @param producto El producto a añadir.
     */
    fun agregarProducto(producto: Producto) {
        // Obtenemos la lista actual de productos del LiveData. Si es nula, creamos una nueva lista vacía.
        val listaActual = _productosEnCarrito.value ?: mutableListOf()

        // Buena práctica: Comprobamos si el producto ya existe en el carrito (por su id)
        // para evitar duplicados. La función 'any' devuelve true si algún elemento cumple la condición.
        if (!listaActual.any { it.id == producto.id }) {
            listaActual.add(producto)
            // Actualizamos el valor del LiveData con la nueva lista. Esto notificará a todos los observadores.
            _productosEnCarrito.value = listaActual
        }
    }

    /**
     * Elimina un producto de la lista del carrito.
     * @param producto El producto a eliminar.
     */
    fun eliminarProducto(producto: Producto) {
        val listaActual = _productosEnCarrito.value ?: mutableListOf()
        listaActual.remove(producto)
        // Actualizamos el valor del LiveData para notificar a los observadores.
        _productosEnCarrito.value = listaActual
    }

    /**
     * Vacía completamente el carrito, eliminando todos los productos.
     */
    fun vaciarCarrito() {
        // Asignamos una nueva lista vacía al LiveData.
        _productosEnCarrito.value = mutableListOf()
    }
}
