package com.example.camisetas100

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

/**
 * Un DialogFragment que muestra un diálogo de confirmación al usuario.
 *
 * Usar un DialogFragment es la práctica recomendada para mostrar diálogos. Android se encarga
 * de gestionar su ciclo de vida, evitando problemas como cierres inesperados si el usuario
 * gira la pantalla mientras el diálogo está visible.
 *
 * @param alConfirmar Una función lambda que se ejecutará solo si el usuario pulsa el botón positivo ("Vaciar").
 */
class DialogoConfirmarVaciarCarrito(private val alConfirmar: () -> Unit) : DialogFragment() {

    /**
     * Este método se llama para construir el diálogo que se va a mostrar.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // El 'activity?.let' es una forma segura de ejecutar código solo si la Activity asociada
        // a este fragmento no es nula, evitando posibles crashes.
        return activity?.let {
            // Usamos AlertDialog.Builder para construir el diálogo de forma fluida.
            val builder = AlertDialog.Builder(it)

            // 1. Establecemos el mensaje principal del diálogo.
            builder.setMessage("¿Estás seguro de que quieres vaciar el carrito?")
                // 2. Definimos el botón "positivo" (de confirmación).
                .setPositiveButton("Vaciar") { _, _ ->
                    // Cuando el usuario pulsa "Vaciar", ejecutamos la función lambda 'alConfirmar'
                    // que nos pasaron al crear la instancia de este diálogo.
                    alConfirmar()
                }
                // 3. Definimos el botón "negativo" (de cancelación).
                .setNegativeButton("Cancelar") { _, _ ->
                    // Cuando el usuario pulsa "Cancelar", simplemente cerramos el diálogo.
                    // 'dismiss()' es el método para cerrar un DialogFragment.
                    dismiss()
                }

            // 4. Creamos y devolvemos el objeto AlertDialog ya construido.
            builder.create()
        } ?: throw IllegalStateException("La Activity no puede ser nula al crear el diálogo") // Si la activity es null, lanzamos una excepción.
    }
}
