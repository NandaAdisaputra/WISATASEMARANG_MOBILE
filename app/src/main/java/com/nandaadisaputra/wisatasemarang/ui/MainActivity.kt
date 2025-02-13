package com.nandaadisaputra.wisatasemarang.ui

// Import library yang diperlukan
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nandaadisaputra.wisatasemarang.R
import com.nandaadisaputra.wisatasemarang.adapter.TempatWisataAdapter

/**
 * MainActivity merupakan activity utama yang menampilkan daftar tempat wisata.
 * Menggunakan RecyclerView untuk menampilkan data dan SearchView untuk fitur pencarian.
 */
class MainActivity : AppCompatActivity() {

    // Deklarasi variabel untuk ViewModel, RecyclerView, Adapter, dan SearchView
    private lateinit var wisataViewModel: WisataViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TempatWisataAdapter
    private lateinit var searchView: SearchView

    // Variabel untuk pagination
    private var currentPage = 1 // Halaman awal
    private val limit = 10 // Jumlah item per halaman

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi RecyclerView dan SearchView dari layout
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView) // Pastikan castingnya benar

        // Menggunakan LinearLayoutManager agar daftar ditampilkan secara vertikal
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi adapter untuk RecyclerView
        adapter = TempatWisataAdapter()
        recyclerView.adapter = adapter

        // Inisialisasi ViewModel menggunakan ViewModelProvider
        wisataViewModel = ViewModelProvider(this).get(WisataViewModel::class.java)

        // Observasi perubahan pada data wisata
        wisataViewModel.wisataData.observe(this, Observer { data ->
            adapter.submitList(data) // Perbarui daftar di adapter saat ada perubahan data
        })

        // Observasi jika terjadi error dan tampilkan pesan kesalahan menggunakan Toast
        wisataViewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })

        // Ambil data wisata pada halaman pertama
        loadData()

        // Atur fitur pencarian
        setupSearch()
    }

    /**
     * Fungsi untuk memuat data wisata berdasarkan halaman yang ditentukan.
     */
    private fun loadData() {
        wisataViewModel.getWisataData(currentPage, limit)
    }

    /**
     * Fungsi untuk mengatur fitur pencarian menggunakan SearchView.
     * Pencarian akan dilakukan saat pengguna menekan tombol submit pada keyboard.
     */
    private fun setupSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    wisataViewModel.searchWisata(query) // Panggil fungsi pencarian di ViewModel
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Tidak ada aksi saat teks berubah (hanya mencari saat submit ditekan)
                return false
            }
        })
    }
}
