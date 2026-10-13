package com.example.androidpractices.domain.usecase

import com.example.androidpractices.domain.model.Launch
import com.example.androidpractices.domain.repository.LaunchRepository

class GetLaunchByIdUseCase(
    private val repository: LaunchRepository
) {
    suspend operator fun invoke(id: String): Launch {
        return repository.getLaunchById(id)
    }
}
