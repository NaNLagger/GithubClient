package com.nanlagger.githubclient.presentation.repository

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.nanlagger.githubclient.tools.addTo
import com.nanlagger.githubclient.di.RepositoryFullName
import com.nanlagger.githubclient.domain.entity.Repository
import com.nanlagger.githubclient.domain.repository.GithubRepository
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class RepositoryInfoPresenter @Inject constructor(
    private val githubRepository: GithubRepository,
    private val router: Router,
    @RepositoryFullName private val repositoryFullName: String
) : MvpPresenter<RepositoryInfoView>() {

    private val compositeDisposable = CompositeDisposable()

    private var repository: Repository? = null

    override fun onFirstViewAttach() {
        githubRepository.getRepository(repositoryFullName)
            .doOnSubscribe { viewState.setLoading(true) }
            .subscribe({
                repository = it
                viewState.showRepository(it)
                viewState.setLoading(false)
            }, { error ->
                viewState.setLoading(false)
                Timber.e(error)
            })
            .addTo(compositeDisposable)
    }

    fun back() {
        router.exit()
    }

    fun changeFavoriteState() {
        repository?.let { repo ->
            val action = if (repo.isFavorite)
                githubRepository.deleteFromFavorite(repo)
            else
                githubRepository.addToFavorite(repo)
            action
                .subscribe({
                    repository = repo.copy(isFavorite = !repo.isFavorite)
                        .also { viewState.showRepository(it) }
                }, { error ->
                    Timber.e(error)
                })
                .addTo(compositeDisposable)
        }
    }
}