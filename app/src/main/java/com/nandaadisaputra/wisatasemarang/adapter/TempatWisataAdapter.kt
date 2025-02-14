package com.nandaadisaputra.wisatasemarang.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nandaadisaputra.wisatasemarang.databinding.ItemWisataBinding
import com.nandaadisaputra.wisatasemarang.model.TempatWisata
import com.nandaadisaputra.wisatasemarang.ui.detail.DetailWisataActivity

/**
 * Adapter untuk RecyclerView yang menampilkan daftar tempat wisata.
 * Menggunakan ListAdapter untuk mengelola perubahan data secara efisien.
 */
class TempatWisataAdapter : ListAdapter<TempatWisata, TempatWisataAdapter.WisataViewHolder>(
    DiffCallback()
) {

    /**
     * Membuat ViewHolder baru saat RecyclerView membutuhkannya.
     *
     * @param parent ViewGroup induk tempat ViewHolder akan ditambahkan.
     * @param viewType Jenis tampilan (tidak digunakan di sini karena hanya ada satu jenis tampilan).
     * @return WisataViewHolder yang sudah terhubung dengan tampilan item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WisataViewHolder {
        val binding = ItemWisataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WisataViewHolder(binding)
    }

    /**
     * Mengikat data ke dalam ViewHolder berdasarkan posisi dalam daftar.
     *
     * @param holder WisataViewHolder yang akan diperbarui.
     * @param position Posisi item dalam daftar.
     */
    override fun onBindViewHolder(holder: WisataViewHolder, position: Int) {
        val tempatWisata = getItem(position) // Mendapatkan item berdasarkan posisi
        holder.bind(tempatWisata) // Memasukkan data ke dalam tampilan
    }

    /**
     * Memperbarui daftar data dengan item terbaru berada di paling atas.
     *
     * @param newList Daftar terbaru dari tempat wisata.
     */
    fun updateData(newList: List<TempatWisata>) {
        submitList(newList.reversed()) // Membalik daftar agar item terbaru berada di atas
    }

    /**
     * ViewHolder untuk menampilkan data tempat wisata di dalam RecyclerView.
     */
    class WisataViewHolder(private val binding: ItemWisataBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Mengikat data tempat wisata ke tampilan yang sesuai.
         *
         * @param tempatWisata Objek TempatWisata yang akan ditampilkan.
         */
        fun bind(tempatWisata: TempatWisata) {
            // Menetapkan nama dan lokasi tempat wisata ke TextView
            binding.tvNama.text = tempatWisata.nama
            binding.tvLokasi.text = tempatWisata.lokasi

            // Menampilkan gambar tempat wisata menggunakan Glide jika URL gambar tersedia
            if (!tempatWisata.gambar.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load(tempatWisata.gambar)
                    .into(binding.imgWisata)
            }

            // Menambahkan event klik pada item untuk membuka DetailWisataActivity
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailWisataActivity::class.java).apply {
                    putExtra("WISATA_ID", tempatWisata.id) // Mengirimkan ID tempat wisata ke DetailWisataActivity
                }
                context.startActivity(intent) // Memulai aktivitas baru untuk menampilkan detail wisata
            }
        }
    }

    /**
     * DiffCallback digunakan untuk menentukan apakah ada perubahan pada daftar
     * guna meningkatkan efisiensi pembaruan RecyclerView.
     */
    class DiffCallback : DiffUtil.ItemCallback<TempatWisata>() {

        /**
         * Memeriksa apakah dua objek tempat wisata adalah item yang sama berdasarkan ID.
         */
        override fun areItemsTheSame(oldItem: TempatWisata, newItem: TempatWisata): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         * Memeriksa apakah isi dari dua objek tempat wisata sama.
         */
        override fun areContentsTheSame(oldItem: TempatWisata, newItem: TempatWisata): Boolean {
            return oldItem == newItem
        }
    }
}
