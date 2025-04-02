package com.example.pizzerialogin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredienteAdapter(private val ingredientes: List<Ingrediente>) :
    RecyclerView.Adapter<IngredienteAdapter.IngredienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingrediente, parent, false)
        return IngredienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredienteViewHolder, position: Int) {
        val ingrediente = ingredientes[position]
        holder.txtDescripcion.text = ingrediente.Descripcion
        holder.txtExistencias.text = ingrediente.Existenciaskg.toString()

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#2E7D32")) // Dark Green
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#4CAF50")) // Lighter Green
        }
    }

    override fun getItemCount(): Int = ingredientes.size

    class IngredienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDescripcion: TextView = itemView.findViewById(R.id.txtDescripcion)
        val txtExistencias: TextView = itemView.findViewById(R.id.txtExistencias)
    }
}
