package com.example.byheart.qa

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.R

class QaListAdapter internal constructor(context: Context) : RecyclerView.Adapter<QaListAdapter.QaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var qas = emptyList<Qa>() // Cached copy

    inner class QaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val qaItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_qa_item, parent, false)
        return QaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QaViewHolder, position: Int) {
        val current = qas[position]
        holder.qaItemView.text = current.question
        holder.qaItemView.setOnClickListener {
            if (holder.qaItemView.text == current.question) {
                holder.qaItemView.text = current.answer
                holder.qaItemView.setTextColor(Color.WHITE)
                holder.qaItemView.setBackgroundResource(R.drawable.card_answer)
            } else {
                holder.qaItemView.text = current.question
                holder.qaItemView.setTextColor(Color.BLACK)
                holder.qaItemView.setBackgroundResource(R.drawable.card)
            }
        }
    }

    internal fun setQas(qas: List<Qa>) {
        this.qas = qas
        notifyDataSetChanged()
    }

    override fun getItemCount() = qas.size
}