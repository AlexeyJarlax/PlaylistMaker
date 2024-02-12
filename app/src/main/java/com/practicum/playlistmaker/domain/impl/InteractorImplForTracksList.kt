package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.InteractorForTracksList
import com.practicum.playlistmaker.domain.api.RepositoryForTracksList
import java.util.concurrent.Executors

class InteractorImplForTracksList(private val repository: RepositoryForTracksList) : InteractorForTracksList {

    private val executor = Executors.newCachedThreadPool()

    override fun searchStep3useAPI(query: String, consumer: InteractorForTracksList.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracksList(query))
        }
    }
}