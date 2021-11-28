package hu.bme.aut.poseidondashboard

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import hu.bme.aut.poseidondashboard.adapter.PhoneAdapter
import hu.bme.aut.poseidondashboard.adapter.SmsAdapter

import hu.bme.aut.poseidondashboard.databinding.ActivityPhonedetailsBinding
import hu.bme.aut.poseidondashboard.model.Phone
import hu.bme.aut.poseidondashboard.model.Sms
import kotlin.concurrent.thread


class PhoneDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityPhonedetailsBinding
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    private var marker: Marker? = null
    private lateinit var phone: Phone
    private lateinit var adapter: SmsAdapter
    private lateinit var db: FirebaseFirestore



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhonedetailsBinding.inflate(layoutInflater)
        window.statusBarColor = getColor(R.color.backgroundTop )

        setContentView(binding.root)
        db = Firebase.firestore

        phone = intent.getSerializableExtra("phone") as Phone
        binding.textView1.text = phone.name



        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.map, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)

        val locationDocRef = db.collection("phones").document(phone.name).collection("location").document("location")
        thread {
            while (true) {
                Thread.sleep(1000)
                val oldLat = latitude
                val oldLong = longitude
                locationDocRef.get()
                    .addOnSuccessListener { document ->
                        if (document.get("longitude") != null) {
                            longitude = document.get("longitude") as Double
                            latitude = document.get("latitude") as Double

                            updateMarker(latitude, longitude)

                            val results = FloatArray(3)
                            Location.distanceBetween(oldLat, oldLong, latitude, longitude, results)
                            Log.d("dist", results[0].toString())
                            if(results[0]> 100) {
                                googleMap.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(latitude, longitude),
                                        13F
                                    )
                                )
                            }
                        }
                    }
            }
        }

        initRecyclerView()
    }

    var first = true
    private fun updateMarker(lat: Double, long: Double) {
        if (first) {
            marker = googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(latitude, longitude))
                    .title("Marker in Sydney")
            )
            first = false
        } else {
            marker?.position = LatLng(lat, long);
        }


    }

    private fun initRecyclerView() {
        adapter = SmsAdapter()

        binding.rvSMS.layoutManager = LinearLayoutManager(this)
        binding.rvSMS.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = mutableListOf<Sms>()

            val docRef = db.collection("phones").document(phone.name).collection("messages")

            docRef.get()
                .addOnSuccessListener { documents ->

                    for (document in documents) {
                        items.add(
                            Sms(
                                document.get("number") as String,
                                document.get("body") as String
                            )
                        )
                    }

                    runOnUiThread {

                        adapter.update(items)
                    }
                }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        val sydney = LatLng(-33.852, 151.211)
        this.googleMap = googleMap
        googleMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(latitude, longitude),
                8F
            )
        )
    }
}