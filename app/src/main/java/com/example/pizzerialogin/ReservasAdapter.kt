package com.example.pizzerialogin
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.graphics.Color


class ReservasAdapter(
    private val reservas: List<MostrarReservaResponse>,
    private val apiService: ApiService) :
    RecyclerView.Adapter<ReservasAdapter.ReservaViewHolder>() {

    class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtIdPedido: TextView = itemView.findViewById(R.id.txtIdPedido)
        val txtFechaEntrega: TextView = itemView.findViewById(R.id.txtFechaEntrega)
        val txtUsuario: TextView = itemView.findViewById(R.id.txtUsuario)
        val txtPrecioTotal: TextView = itemView.findViewById(R.id.txtPrecioTotal)
        val txtLineasReserva: TextView = itemView.findViewById(R.id.txtLineasReserva)
        val btnToggleEntrega: Button = itemView.findViewById(R.id.btnToggleEntrega)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reserva, parent, false)
        return ReservaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]
        holder.txtIdPedido.text = "Pedido: ${reserva.idPedido}"
        holder.txtFechaEntrega.text = "Entrega: ${reserva.fechaHoraEntrega ?: "No especificada"}"
        holder.txtUsuario.text = "Usuario: ${reserva.usuario?.usuarioPrimerNombre ?: "Desconocido"} " + (reserva.usuario?.usuarioApellido ?: "")
        holder.txtPrecioTotal.text = "Precio: ${reserva.precioTotal ?: "Desconocido"}"

        val lineasText = reserva.lineas.joinToString("\n") { "Sabor: ${it.idSabor}, Porciones: ${it.numeroPorciones}" }
        holder.txtLineasReserva.text = lineasText

        holder.txtLineasReserva.visibility = View.GONE

        // Toggle visibility when clicking on the item
        holder.itemView.setOnClickListener {
            if (holder.txtLineasReserva.visibility == View.GONE) {
                holder.txtLineasReserva.visibility = View.VISIBLE
            } else {
                holder.txtLineasReserva.visibility = View.GONE
            }
        }

        val verde = Color.parseColor("#4CAF50") // Green
        val naranja = Color.parseColor("#FF9800") // Orange

        holder.btnToggleEntrega.text = if (reserva.entregada == 1) "Entregada" else "No Entregada"
        holder.btnToggleEntrega.setBackgroundColor(if (reserva.entregada == 1) verde else naranja)

        holder.btnToggleEntrega.setOnClickListener {
            var newStatus = if (reserva.entregada == 1) 0 else 1
            val updateRequest = UpdateEntregaRequest(reserva.idPedido, newStatus)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.updateEntrega(updateRequest)
                    if (response.isSuccessful && response.body() != null) {
                        withContext(Dispatchers.Main) {
                            reserva.entregada = newStatus
                            notifyItemChanged(position)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(holder.itemView.context, "Error al actualizar estado", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(holder.itemView.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int = reservas.size
}
