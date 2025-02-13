package com.nandaadisaputra.wisatasemarang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nandaadisaputra.wisatasemarang.databinding.ItemWisataBinding
import com.nandaadisaputra.wisatasemarang.model.TempatWisata

/**
 * Adapter untuk RecyclerView yang menampilkan daftar tempat wisata.
 * Menggunakan ListAdapter agar lebih efisien dalam memperbarui data.
 */
class TempatWisataAdapter : ListAdapter<TempatWisata, TempatWisataAdapter.WisataViewHolder>(
    DiffCallback() // Digunakan untuk membandingkan data agar RecyclerView hanya memperbarui item yang berubah
) {

    /**
     * Membuat instance ViewHolder baru ketika RecyclerView membutuhkannya.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WisataViewHolder {
        // Inflate layout item wisata menggunakan View Binding
        val binding = ItemWisataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WisataViewHolder(binding)
    }

    /**
     * Mengikat (bind) data dari daftar tempat wisata ke tampilan yang sesuai.
     */
    override fun onBindViewHolder(holder: WisataViewHolder, position: Int) {
        val tempatWisata = getItem(position) // Mengambil item berdasarkan posisi
        holder.bind(tempatWisata) // Memanggil fungsi bind untuk menampilkan data
    }

    /**
     * ViewHolder untuk menampilkan data tempat wisata pada item RecyclerView.
     */
    class WisataViewHolder(private val binding: ItemWisataBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Menampilkan data tempat wisata ke dalam tampilan item.
         */
        fun bind(tempatWisata: TempatWisata) {
            binding.tvNama.text = tempatWisata.nama // Menampilkan nama tempat wisata
            binding.tvLokasi.text = tempatWisata.lokasi // Menampilkan lokasi tempat wisata

            // Menampilkan gambar jika tersedia
            if (!tempatWisata.gambar.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load(tempatWisata.gambar) // Memuat gambar dari URL menggunakan Glide
                    .into(binding.imgWisata) // Menampilkan gambar ke dalam ImageView
            }
        }
    }

    /**
     * Kelas DiffCallback untuk membandingkan data lama dan baru agar RecyclerView hanya memperbarui item yang berubah.
     */
    class DiffCallback : DiffUtil.ItemCallback<TempatWisata>() {

        /**
         * Memeriksa apakah dua item memiliki ID yang sama.
         */
        override fun areItemsTheSame(oldItem: TempatWisata, newItem: TempatWisata): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         * Memeriksa apakah isi dari dua item sama.
         */
        override fun areContentsTheSame(oldItem: TempatWisata, newItem: TempatWisata): Boolean {
            return oldItem == newItem
        }
    }
}
