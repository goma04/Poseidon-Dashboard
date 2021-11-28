package hu.bme.aut.poseidondashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.poseidondashboard.model.Phone
import hu.bme.aut.poseidondashboard.adapter.PhoneAdapter
import hu.bme.aut.poseidondashboard.databinding.ActivityEntryBinding
import hu.bme.aut.poseidondashboard.model.Sms
import kotlin.concurrent.thread

class EntryActivity : AppCompatActivity(), PhoneAdapter.PhoneItemClickListener {
    private lateinit var binding: ActivityEntryBinding
    private lateinit var adapter: PhoneAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Firebase.firestore
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

            db.collection("phones").get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        items.add(
                            Phone(
                                document.id
                            )
                        )
                    }
                    runOnUiThread {
                        adapter.update(items)
                    }
                }
        }
    }

    override fun onItemClick(phone: Phone) {
        val intent = Intent(this, PhoneDetailsActivity::class.java)

        intent.putExtra("phone", phone)
        startActivity(intent)
    }
}