package hu.bme.aut.poseidondashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.poseidondashboard.adapter.Phone
import hu.bme.aut.poseidondashboard.adapter.PhoneAdapter
import hu.bme.aut.poseidondashboard.databinding.ActivityEntryBinding
import kotlin.concurrent.thread

class EntryActivity : AppCompatActivity(), PhoneAdapter.PhoneItemClickListener {
    private lateinit var binding: ActivityEntryBinding
    private lateinit var adapter: PhoneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = PhoneAdapter()
        adapter.itemClickListener = this
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = mutableListOf<Phone>()
            for (i in 0..20){
                items.add(Phone("TestPhone${i}"))
        }
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemClick(phone: Phone) {
        val intent = Intent(this,PhoneDetailsActivity::class.java)
        intent.putExtra("PhoneName", phone.name)
        startActivity(intent)
    }
}