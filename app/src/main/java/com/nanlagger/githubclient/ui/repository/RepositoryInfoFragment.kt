package com.nanlagger.githubclient.ui.repository

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.nanlagger.githubclient.R
import com.nanlagger.githubclient.di.RepositoryFullName
import com.nanlagger.githubclient.di.Scopes
import com.nanlagger.githubclient.domain.entity.Repository
import com.nanlagger.githubclient.presentation.repository.RepositoryInfoPresenter
import com.nanlagger.githubclient.presentation.repository.RepositoryInfoView
import com.nanlagger.githubclient.tools.argument
import com.nanlagger.githubclient.tools.visible
import com.nanlagger.githubclient.ui.common.BaseFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_repository_info.*
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module

class RepositoryInfoFragment : BaseFragment(), RepositoryInfoView {

    override val layoutId: Int = R.layout.fragment_repository_info

    private val repositoryName by argument(KEY_REPOSITORY_NAME, "")

    @InjectPresenter
    lateinit var presenter: RepositoryInfoPresenter

    @ProvidePresenter
    fun providePresenter(): RepositoryInfoPresenter =
            scope.getInstance(RepositoryInfoPresenter::class.java)

    private lateinit var scope: Scope

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener { presenter.back() }
            buttonFavorite.setOnClickListener { presenter.changeFavoriteState() }
        }
    }

    override fun onOpenScope() {
        scope = Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.REPOSITORY_INFO_SCOPE + fragmentId)
                .apply {
                    installModules(
                            object : Module() {
                                init {
                                    bind(String::class.java)
                                            .withName(RepositoryFullName::class.java)
                                            .toInstance(repositoryName)
                                }
                            }
                    )
                }
    }

    override fun onCloseScope() {
        Toothpick.closeScope(Scopes.REPOSITORY_INFO_SCOPE + fragmentId)
    }

    override fun setLoading(loading: Boolean) {
        progress.visible(loading)
        containerContent.visible(!loading)
    }

    override fun showRepository(repository: Repository) {
        textRepoName.text = repository.name
        textRepoDescription.text = repository.description
        textOwnerName.text = repository.user.login
        textDateCreated.text = repository.createdAt.toString()
        textCountForks.text = "Forks: ${repository.forksCount}"
        textCountStars.text = "Stars: ${repository.starsCount}"
        buttonFavorite.text = if (!repository.isFavorite) getString(R.string.add_to_favorite) else getString(R.string.remove_from_favorite)
        Picasso.get()
                .load(repository.user.avatarUrl)
                .placeholder(R.mipmap.avatar_placeholder)
                .error(R.mipmap.avatar_placeholder)
                .into(imageOwnerAvatar)
    }

    override fun showError(error: String) {
        showSnackMessage(error)
    }

    companion object {
        const val KEY_REPOSITORY_NAME = "key.repository.name"
        fun newInstance(repositoryName: String): RepositoryInfoFragment {
            return RepositoryInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_REPOSITORY_NAME, repositoryName)
                }
            }
        }
    }
}