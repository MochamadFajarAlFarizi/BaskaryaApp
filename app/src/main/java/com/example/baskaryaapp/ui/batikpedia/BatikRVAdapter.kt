package com.example.baskaryaapp.ui.batikpedia

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baskaryaapp.data.response.BatikItem
import com.example.baskaryaapp.databinding.ItemListBatikBinding
import com.example.baskaryaapp.ui.detailBatik.DetailBatikActivity


class BatikRVAdapter : ListAdapter<BatikItem, BatikRVAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback:OnItemClickCallback?=null
    private val list =ArrayList<BatikItem>()
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback=onItemClickCallback
    }
    fun setList(user: ArrayList<BatikItem>){
        list.clear()
        list.addAll(user)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListBatikBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val batik = getItem(position)
        holder.bind(batik)
    }

    class ListViewHolder(private val binding: ItemListBatikBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(batik: BatikItem){
            Glide.with(binding.root.context)
                .load(batik?.imageUrl)
                .into(binding.idIVBatik)
            binding.idTVBatik.text = batik?.title


            binding.root.setOnClickListener{
                val intentDetail = Intent(binding.root.context, DetailBatikActivity::class.java)
                intentDetail.putExtra(EXTRA_BATIK, batik)
                intentDetail.putExtra("key_id", batik.id)
                intentDetail.putExtra("key_title", batik.title)
                intentDetail.putExtra("key_imageUrl", batik.imageUrl)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.idIVBatik, "image"),
                    )

                itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
            }
        }


    }
    interface OnItemClickCallback{
        fun onItemCliked(batik:BatikItem)
    }
    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BatikItem>(){
            override fun areItemsTheSame(
                oldItem: BatikItem,
                newItem: BatikItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: BatikItem,
                newItem: BatikItem
            ): Boolean {
                return oldItem == newItem
            }
        }

        const val EXTRA_ID = "key_id"
        const val EXTRA_BATIK = "key_batik"
    }
}
