package com.example.androidkotlinassignment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlinassignment.databinding.ListItemBinding
import com.example.androidkotlinassignment.models.Fact

class FactAdapter(private val facts:List<Fact>): RecyclerView.Adapter<FactAdapter.FactViewHolder>() {

    class FactViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(fact: Fact){
            binding.fact = fact
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater,parent,false)
        return FactViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return facts.size
    }

    override fun onBindViewHolder(holder: FactViewHolder, position: Int) {
        val fact = facts[position]
        holder.bind(fact)
    }
}