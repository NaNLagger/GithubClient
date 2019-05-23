package com.nanlagger.githubclient.ui.repositories

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nanlagger.githubclient.R
import com.nanlagger.githubclient.domain.entity.Repository
import com.nanlagger.githubclient.tools.format
import com.nanlagger.githubclient.tools.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_repository.*

class RepositoryAdapter(
    private val onClickListener: (Repository) -> Unit,
    diffCallback: RepostoriesDiffCallback
) : PagedListAdapter<Repository, RepositoryAdapter.RepositoryViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        return RepositoryViewHolder(parent.inflate(R.layout.item_repository))
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        } else {
            holder.bindEmpty()
        }
    }

    inner class RepositoryViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(repository: Repository) {
            containerView.setOnClickListener { onClickListener(repository) }
            textRepoName.text = repository.name
            textDateCreated.text = repository.createdAt.format()
            textRepoDescription.text = repository.description
        }

        fun bindEmpty() {
            textRepoName.text = ""
            textRepoDescription.text = ""
        }
    }
}