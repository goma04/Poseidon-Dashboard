package hu.bme.aut.poseidondashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.poseidondashboard.databinding.ItemSmsListBinding

import hu.bme.aut.poseidondashboard.model.Sms

class SmsAdapter : RecyclerView.Adapter<SmsAdapter.SmsViewHolder>() {

    inner class SmsViewHolder(val binding: ItemSmsListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val items = mutableListOf<Sms>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        return SmsViewHolder(
            ItemSmsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SmsAdapter.SmsViewHolder, position: Int) {
        val sms = items[position]
        holder.binding.tvPhoneNumber.text = sms.phoneNumber
        holder.binding.tvSmsText.text = sms.body
    }

    override fun getItemCount() = items.size

    fun update(smsList: List<Sms>) {
        items.clear()
        items.addAll(smsList)
        notifyDataSetChanged()
    }

}