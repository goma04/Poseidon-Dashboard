package hu.bme.aut.poseidondashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.poseidondashboard.R
import hu.bme.aut.poseidondashboard.databinding.ItemPhoneListBinding
import hu.bme.aut.poseidondashboard.model.Phone
import kotlin.random.Random

class PhoneAdapter : RecyclerView.Adapter<PhoneAdapter.PhoneViewHolder>() {

    inner class PhoneViewHolder(val binding: ItemPhoneListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var phone: Phone? = null

        init {
            itemView.setOnClickListener {
                phone?.let { phone ->
                    itemClickListener?.onItemClick(phone)
                }
            }
        }
    }

    private val items = mutableListOf<Phone>()
    var itemClickListener: PhoneItemClickListener? = null

    interface PhoneItemClickListener {
        fun onItemClick(phone: Phone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneViewHolder {
        return PhoneViewHolder(
            ItemPhoneListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhoneViewHolder, position: Int) {
        val phone = items[position]
        holder.phone = phone
        holder.binding.phoneName.text = phone.name
    }

    override fun getItemCount() = items.size

    fun addItem(item: Phone) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(phones: List<Phone>) {
        items.clear()
        items.addAll(phones)
        notifyDataSetChanged()
    }

}