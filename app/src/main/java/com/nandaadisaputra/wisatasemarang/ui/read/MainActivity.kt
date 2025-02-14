package com.nandaadisaputra.wisatasemarang.ui.read

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nandaadisaputra.wisatasemarang.adapter.TempatWisataAdapter
import com.nandaadisaputra.wisatasemarang.databinding.ActivityMainBinding
import com.nandaadisaputra.wisatasemarang.ui.add.TambahWisataActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var wisataViewModel: MainViewModel
    private lateinit var adapter: TempatWisataAdapter
    private var currentPage = 1 // Halaman awal
    private val limit = 10 // Jumlah item per halaman

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView() // Mengatur tampilan daftar wisata
        setupViewModel() // Menghubungkan ViewModel dengan UI
        setupSwipeRefresh() // Menyediakan fitur refresh data
        setupSearch() // Mengaktifkan pencarian data
        setupFabButton() // Menyiapkan tombol untuk menambah data
    }

    /**
     * Mengatur RecyclerView dengan adapter dan layout manager.
     */
    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TempatWisataAdapter()
        binding.recyclerView.adapter = adapter
    }

    /**
     * Mengatur ViewModel dan observasi LiveData.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun setupViewModel() {
        wisataViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Mengamati perubahan data wisata dan memperbarui tampilan
        wisataViewModel.wisataData.observe(this) { data ->
            data?.let {
                val sortedList = it.sortedByDescending { wisata -> wisata.id }
                adapter.submitList(sortedList) // Menampilkan item terbaru di bagian atas
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }

        // Mengamati perubahan pesan error jika terjadi kesalahan
        wisataViewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        // Mengambil data wisata pertama kali saat aplikasi dibuka
        wisataViewModel.getWisataData(currentPage, limit)
        adapter.notifyDataSetChanged()
    }

    /**
     * Mengatur SwipeRefreshLayout untuk memuat ulang data saat pengguna menarik ke bawah.
     */
    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            wisataViewModel.getWisataData(currentPage, limit) // Ambil ulang data wisata
        }
    }

    /**
     * Mengatur fitur pencarian wisata berdasarkan input pengguna.
     */
    private fun setupSearch() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wisataViewModel.searchWisata(s.toString().trim()) // Memfilter data wisata sesuai kata kunci
            }
        })
    }

    /**
     * Mengatur tombol Floating Action Button untuk menambah wisata baru.
     */
    private fun setupFabButton() {
        val tambahWisataLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                wisataViewModel.getWisataData(currentPage, limit) // Perbarui data setelah menambahkan wisata baru
            }
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, TambahWisataActivity::class.java)
            tambahWisataLauncher.launch(intent)
        }
    }
}