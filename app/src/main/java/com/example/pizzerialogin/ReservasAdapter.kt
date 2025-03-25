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

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reserva, parent, false)
        return ReservaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]
        holder.txtIdPedido.text = "ID: ${reserva.idPedido}"
        holder.txtFechaEntrega.text = "Entrega: ${reserva.fechaHoraEntrega ?: "No especificada"}"
        holder.txtUsuario.text = "Usuario: ${reserva.usuarioDocumento ?: "Desconocido"}"
        holder.txtPrecioTotal.text = "Precio: ${reserva.precioTotal ?: "Desconocido"}"

    }

    override fun getItemCount(): Int = reservas.size
}
