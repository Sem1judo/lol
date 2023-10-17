package com.oceanluc.kywheels.fragments.games.slots

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.oceanluc.kywheels.R

class SlotObjAdapter(private val context: Context, private val slotItems: List<SlotObj>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private companion object {
        private const val VIEW_TYPE_PORTRAIT = 1
        private const val VIEW_TYPE_LANDSCAPE = 2
    }

    private val isPortraitOrientation =
        context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    override fun getItemViewType(position: Int): Int {
        return if (isPortraitOrientation) VIEW_TYPE_PORTRAIT else VIEW_TYPE_LANDSCAPE
    }

    fun getItemByPosition(position: Int) = slotItems[position % slotItems.size]


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)

        val layoutId = if (viewType == VIEW_TYPE_PORTRAIT) {
            R.layout.slot_item
        } else {
            R.layout.slots_item_land
        }

        val view = inflater.inflate(layoutId, parent, false)

        return when (viewType) {
            VIEW_TYPE_PORTRAIT -> SlotPortraitViewHolder(view)
            VIEW_TYPE_LANDSCAPE -> SlotLandscapeViewHolder(view)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = slotItems[position % slotItems.size]
        when (holder) {
            is SlotPortraitViewHolder -> holder.bind(item.image)
            is SlotLandscapeViewHolder -> holder.bind(item.image)
        }
    }

    inner class SlotPortraitViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(image: Drawable?) {
            view.findViewById<ImageView>(R.id.iv_slots).setImageDrawable(image)
        }
    }

    inner class SlotLandscapeViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(image: Drawable?) {
            view.findViewById<ImageView>(R.id.iv_slots_land).setImageDrawable(image)
        }
    }
}
