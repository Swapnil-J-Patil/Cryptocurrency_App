package com.example.cleanarchitectureproject.domain.use_case.app_theme

import com.example.cleanarchitectureproject.domain.repository.ThemeRepository
import javax.inject.Inject


class GetThemeUseCase @Inject constructor(private val repository: ThemeRepository) {
    operator fun invoke() = repository.getTheme()
}
