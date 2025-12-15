package com.example.camisetas100

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Activity principal de la aplicación. Muestra la lista de productos y la navegación principal.
 * Hereda de ActividadBase para obtener automáticamente el estilo edge-to-edge.
 */
class MainActivity : ActividadBase() {

    // Referencias a las vistas del layout que necesitaremos manipular.
    private lateinit var listaProductosRecyclerView: RecyclerView
    private lateinit var adaptadorProducto: AdaptadorProducto // Usamos nuestro nuevo adaptador

    // --- Lógica para Notificaciones ---

    // Preparamos un "lanzador" que solicita un permiso y nos permite
    // definir qué hacer cuando el usuario responde (concede o deniega el permiso).
    private val lanzadorPeticionPermisos = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { fueConcedido: Boolean ->
        if (fueConcedido) {
            // Si el usuario concede el permiso, mostramos la notificación.
            mostrarNotificacionOferta()
        } else {
            // Si lo deniega, informamos al usuario con un mensaje temporal (Toast).
            Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Al iniciar la app, creamos el canal de notificaciones.
        crearCanalDeNotificaciones()
        // Configuramos la lista de productos.
        configurarRecyclerView()
        // Configuramos los botones de navegación.
        configurarBotones()

        // Asignamos la acción al botón flotante (FAB).
        findViewById<FloatingActionButton>(R.id.fab_notificacion).setOnClickListener {
            pedirPermisoParaNotificaciones()
        }
    }

    /**
     * Crea un Canal de Notificación para las ofertas. Todas las notificaciones
     * deben estar asignadas a un canal. Los usuarios pueden configurar
     * las notificaciones de cada canal de forma independiente en los ajustes del sistema.
     */
    private fun crearCanalDeNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nombre = "Canal de Ofertas"
            val descripcion = "Notificaciones para nuevas ofertas y promociones"
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val canal = NotificationChannel(ID_CANAL, nombre, importancia).apply {
                this.description = descripcion
            }
            // Registramos el canal en el sistema. Una vez creado, no se puede cambiar, solo en los ajustes.
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
    }

    /**
     * Comprueba si se tiene permiso para enviar notificaciones. Si no, lo solicita.
     */
    private fun pedirPermisoParaNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Comprobamos si ya tenemos el permiso concedido.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // Si ya lo tenemos, mostramos la notificación directamente.
                mostrarNotificacionOferta()
            } else {
                // Si no lo tenemos, usamos el "lanzador" que preparamos antes para solicitarlo.
                lanzadorPeticionPermisos.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {

            mostrarNotificacionOferta()
        }
    }

    /**
     * Construye y muestra la notificación de oferta.
     */
    private fun mostrarNotificacionOferta() {
        // 1. Crear el Intent que se ejecutará al tocar la notificación.
        // En este caso, queremos que abra la ActividadPrincipal.
        val intent = Intent(this, MainActivity::class.java).apply {
            // Estas flags aseguran que si la app ya está abierta, no se cree una nueva instancia encima.
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // 2. Crear el PendingIntent.
        // Un PendingIntent es un "paquete" que contiene un Intent. Se lo damos al sistema operativo
        // para que ejecute el Intent en nuestro nombre en un futuro (cuando el usuario toque la notificación).
        // FLAG_IMMUTABLE indica que el Intent dentro del PendingIntent no puede ser modificado.
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // 3. Construir la notificación.
        val constructorNotificacion = NotificationCompat.Builder(this, ID_CANAL)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Icono pequeño que aparece en la barra de estado.
            .setContentTitle("¡Nueva oferta disponible!") // Título de la notificación.
            .setContentText("¡Las mejores camisetas a precios increíbles! No te lo pierdas.") // Cuerpo del mensaje.
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Prioridad de la notificación (afecta a cómo se presenta).
            .setContentIntent(pendingIntent) // Asignamos el PendingIntent que se ejecutará al tocarla.
            .setAutoCancel(true) // Hacemos que la notificación desaparezca automáticamente al ser pulsada.

        // 4. Mostrar la notificación.
        // Usamos NotificationManagerCompat para ser compatible con versiones antiguas de Android.
        with(NotificationManagerCompat.from(this)) {
            // Android Studio exige esta comprobación de permiso, aunque ya la hayamos hecho antes.
            if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                return
            }
            // 'notify()' le dice al sistema que muestre la notificación. Usamos un ID único por si luego queremos actualizarla o cancelarla.
            notify(ID_NOTIFICACION, constructorNotificacion.build())
        }
    }

    /**
     * Inicializa el RecyclerView principal, su LayoutManager y su adaptador.
     */
    private fun configurarRecyclerView() {
        listaProductosRecyclerView = findViewById(R.id.lista_productos)
        listaProductosRecyclerView.layoutManager = LinearLayoutManager(this)

        val listaProductos = crearProductosDeEjemplo()

        // Creamos el adaptador, le pasamos la lista de productos y la acción a realizar al pulsar uno.
        adaptadorProducto = AdaptadorProducto(listaProductos) { productoPulsado ->
            // La acción es crear un Intent para abrir la ActividadDetalleProducto.
            val intent = Intent(this, DetalleProducto::class.java).apply {
                // Metemos el objeto 'productoPulsado' (que es Parcelable) dentro del Intent.
                // Usamos una clave constante para recuperarlo en la otra Activity.
                putExtra(DetalleProducto.EXTRA_PRODUCTO, productoPulsado)
            }
            // Iniciamos la nueva Activity.
            startActivity(intent)
        }

        listaProductosRecyclerView.adapter = adaptadorProducto
    }

    /**
     * Asigna las acciones de clic a los botones de navegación superiores.
     */
    private fun configurarBotones() {
        findViewById<Button>(R.id.boton_ofertas).setOnClickListener {
            Toast.makeText(this, "No hay ofertas disponibles en este momento :(", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.boton_carrito).setOnClickListener {
            // Al pulsar "Carrito", creamos un Intent y abrimos la ActividadCarrito.
            val intent = Intent(this, Carrito::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.boton_acerca_de).setOnClickListener {
            // Al pulsar "Acerca de", creamos un Intent y abrimos la ActividadAcercaDe.
            val intent = Intent(this, AcercaDe::class.java)
            startActivity(intent)
        }
    }

    /**
     * Crea y devuelve una lista de productos de ejemplo para poblar la app.
     */
    private fun crearProductosDeEjemplo(): List<Producto> {
        return listOf(
            Producto(1, "Camiseta Chelsea", "La camiseta Chelsea FC 2023/24 rinde homenaje a los días de gloria de los años 90 y al icónico Stamford Bridge.", 89.99, R.drawable.chelsea),
            Producto(2, "Camiseta Manchester City", "La camiseta del Manchester City 2023/24 celebra el 20º aniversario del club en el Etihad Stadium.", 94.99, R.drawable.city),
            Producto(3, "Camiseta Real Madrid", "Elegancia sencilla y un diseño atemporal. Esta camiseta del Real Madrid es un clásico del fútbol.", 100.00, R.drawable.madrid),
            Producto(4, "Camiseta Málaga", "La nueva camiseta blanquiazul del Málaga CF para la temporada 2023/24 es un tributo a la ciudad y a su gente.", 69.95, R.drawable.malaga),
            Producto(5, "Camiseta Marsella", "La camiseta del Olympique de Marsella 2023/24 está inspirada en las famosas 'calanques' de la ciudad.", 90.00, R.drawable.marsella),
            Producto(6, "Camiseta PSG", "La camiseta del Paris Saint-Germain 2023/24 evoca la 'Ciudad de la Luz' con un diseño moderno y audaz.", 95.00, R.drawable.psg)
        )
    }

    /**
     * Companion object para constantes de la clase.
     */
    companion object {
        // ID único para nuestro canal de notificaciones. Es una buena práctica tenerlo como constante.
        private const val ID_CANAL = "canal_ofertas"
        // ID único para nuestra notificación. Nos permite actualizarla o cancelarla si es necesario.
        private const val ID_NOTIFICACION = 1
    }
}
