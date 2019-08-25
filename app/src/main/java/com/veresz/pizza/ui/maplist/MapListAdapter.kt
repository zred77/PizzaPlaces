package com.veresz.pizza.ui.maplist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.veresz.pizza.R
import com.veresz.pizza.model.Place
import kotlinx.android.synthetic.main.item_maplist.view.address
import kotlinx.android.synthetic.main.item_maplist.view.image
import kotlinx.android.synthetic.main.item_maplist.view.name

class MapListAdapter(
    private val listener: (View, Place) -> Unit
) : ListAdapter<Place, MapListAdapter.ViewHolder>(PlaceDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_maplist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Place = getItem(position)
        with(holder) {
            holder.image.load(item.images[0].url) {
                placeholder(R.drawable.image_placeholder)
                error(R.drawable.image_placeholder)
                crossfade(true)
            }
            holder.name.text = item.name
            holder.address.text = item.formattedAddress
            view.setOnClickListener { listener.invoke(holder.itemView, item) }
        }
    }

    fun getItemById(position: Int): Place = getItem(position)

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.image
        val name: TextView = view.name
        val address: TextView = view.address
    }

    class PlaceDiffUtil : DiffUtil.ItemCallback<Place>() {

        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.city == newItem.city &&
                    oldItem.formattedAddress == newItem.formattedAddress
        }
    }
}
