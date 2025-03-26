package com.example.pizzerialogin
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ReservasAdapter(private val reservas: List<MostrarReservaResponse>) :
    RecyclerView.Adapter<ReservasAdapter.ReservaViewHolder>() {

    class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtIdPedido: TextView = itemView.findViewById(R.id.txtIdPedido)
        val txtFechaEntrega: TextView = itemView.findViewById(R.id.txtFechaEntrega)
        val txtUsuario: TextView = itemView.findViewById(R.id.txtUsuario)
        val txtPrecioTotal: TextView = itemView.findViewById(R.id.txtPrecioTotal)
        val txtLineasReserva: TextView = itemView.findViewById(R.id.txtLineasReserva)

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

    }

    override fun getItemCount(): Int = reservas.size
}
