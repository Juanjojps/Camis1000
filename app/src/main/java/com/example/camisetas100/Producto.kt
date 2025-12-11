package com.example.camisetas100

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

/**
 * Clase de datos que representa un único producto en nuestra tienda.
 *
 * Una 'data class' en Kotlin es una clase cuyo propósito principal es contener datos.
 * El compilador genera automáticamente mucho código útil como equals(), hashCode(), toString(), etc.
 *
 * Implementa la interfaz 'Parcelable'. Esto permite "empaquetar" un objeto de esta clase
 * para poder pasarlo de forma eficiente entre diferentes componentes de Android, como
 * por ejemplo de una Activity a otra a través de un Intent.
 *
 * @param id El identificador único del producto. Usamos Long para tener un gran rango de valores.
 * @param nombre El nombre del producto que se mostrará en la interfaz.
 * @param descripcion Una descripción más larga y detallada del producto.
 * @param precio El precio del producto. Usamos Double para permitir decimales.
 * @param idRecursoImagen El identificador del recurso 'drawable' de la imagen del producto.
 *                        Es un Int (por ejemplo, R.drawable.camiseta_clasica).
 *                        La anotación @DrawableRes ayuda a Android Studio a verificar que
 *                        el número que pasamos aquí es realmente un recurso de imagen válido.
 */
@Parcelize // Anotación mágica que genera automáticamente el código necesario para implementar Parcelable.
data class Producto(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    @DrawableRes val idRecursoImagen: Int
) : Parcelable
