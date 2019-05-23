package com.nanlagger.githubclient.ui.repositories

import androidx.recyclerview.widget.DiffUtil
import com.nanlagger.githubclient.domain.entity.Repository

class RepostoriesDiffCallback : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem == newItem
    }

}