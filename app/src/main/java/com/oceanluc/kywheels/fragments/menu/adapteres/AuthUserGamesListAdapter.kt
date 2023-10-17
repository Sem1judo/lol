package com.oceanluc.kywheels.fragments.menu.adapteres

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oceanluc.kywheels.databinding.ItemListBinding
import com.oceanluc.kywheels.models.AuthUserGamesModel
import com.oceanluc.kywheels.tools.DiffUtilImpl
import com.oceanluc.kywheels.tools.OnItemClickListener
class AuthUserGamesListAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val appContext: Context
) : ListAdapter<AuthUserGamesModel, AuthUserGamesListAdapter.AuthUserGamesListViewHolder>(DiffUtilImpl()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthUserGamesListViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AuthUserGamesListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuthUserGamesListViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }

    inner class AuthUserGamesListViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: AuthUserGamesModel) {
            // Use Glide to load the image into the ImageView
            Glide.with(binding.ivSelectGame)
                .load(model.image)
                .into(binding.ivSelectGame)

            itemView.setOnClickListener {
                if (model.isOpen) {
                    onItemClickListener.onItemClick(model)
                } else {
                    showToast("This game is not available to you yet!")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
    }
}
