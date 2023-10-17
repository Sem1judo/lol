package com.oceanluc.kywheels.tools

import androidx.recyclerview.widget.DiffUtil

class DiffUtilImpl<T : IDiffModel<S>, S> : DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.image == newItem.image
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}