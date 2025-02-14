package com.nandaadisaputra.wisatasemarang.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.nandaadisaputra.wisatasemarang.R
import com.nandaadisaputra.wisatasemarang.databinding.ActivityDetailWisataBinding
import com.nandaadisaputra.wisatasemarang.model.DetailWisataResponse
import com.nandaadisaputra.wisatasemarang.ui.edit.EditWisataActivity

class DetailWisataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailWisataBinding
    private val viewModel: DetailWisataViewModel by viewModels()
    private var wisataId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWisataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan ID wisata dari intent
        wisataId = intent.getIntExtra("WISATA_ID", -1)

        if (wisataId != -1) {
            binding.swipeRefresh.isRefreshing = true // Menampilkan indikator refresh saat membuka halaman
            fetchData()
        } else {
            Toast.makeText(this, "ID wisata tidak valid!", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Menyegarkan data saat pengguna melakukan swipe refresh
        binding.swipeRefresh.setOnRefreshListener {
            fetchData()
        }

        // Event saat tombol Edit diklik
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditWisataActivity::class.java).apply {
                putExtra("WISATA_ID", wisataId)
                putExtra("WISATA_NAMA", binding.tvDetailNama.text.toString())
                putExtra("WISATA_LOKASI", binding.tvDetailLokasi.text.toString())
                putExtra("WISATA_DESKRIPSI", binding.tvDetailDeskripsi.text.toString())
            }
            startActivity(intent)
        }

        // Event saat tombol Hapus diklik
        binding.btnHapus.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin menghapus data ini?")
                .setPositiveButton("Ya") { _, _ ->
                    viewModel.deleteWisata(wisataId)
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        // Observer untuk status penghapusan wisata
        viewModel.deleteStatus.observe(this, Observer { (success, message) ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            if (success) {
                val resultIntent = Intent().apply {
                    putExtra("status", "success")
                    putExtra("message", "Wisata berhasil dihapus")
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.swipeRefresh.isRefreshing = true // Refresh otomatis saat kembali ke halaman
        fetchData()
    }

    // Mengambil data detail wisata dari ViewModel
    private fun fetchData() {
        viewModel.fetchDetailWisata(wisataId)
        viewModel.wisataDetail.observe(this, Observer { wisata ->
            binding.swipeRefresh.isRefreshing = false
            wisata?.let { displayDetail(it) } ?: showError()
        })
    }

    // Menampilkan detail wisata pada tampilan
    private fun displayDetail(wisata: DetailWisataResponse) {
        binding.tvDetailNama.text = wisata.nama
        binding.tvDetailLokasi.text = wisata.lokasi
        binding.tvDetailDeskripsi.text = wisata.deskripsi
        Glide.with(this).load(wisata.gambar)
            .placeholder(R.drawable.placeholder_image) // Placeholder saat gambar dimuat
            .error(R.drawable.image_not_found) // Gambar default jika gagal memuat
            .into(binding.imgDetailWisata)
    }

    // Menampilkan pesan error jika data tidak ditemukan
    private fun showError() {
        Toast.makeText(this, "Data wisata tidak ditemukan!", Toast.LENGTH_LONG).show()
    }
}