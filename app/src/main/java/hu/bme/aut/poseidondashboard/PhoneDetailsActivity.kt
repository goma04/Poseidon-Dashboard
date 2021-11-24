package hu.bme.aut.poseidondashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

import hu.bme.aut.poseidondashboard.databinding.ActivityPhonedetailsBinding
import kotlin.concurrent.thread

class PhoneDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhonedetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhonedetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val db = Firebase.firestore

        val docRef = db.collection("locations").document("clientLocation")
        binding.textView1.text = intent.extras?.getString("PhoneName")
        thread {
            while (true) {
                Thread.sleep(1000)

                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            binding.textView2.text = "Longitude: ${document.get("longitude")}"
                            binding.textView3.text = "Latitude: ${document.get("latitude")}"

                        }
                    }
            }
        }
    }
}