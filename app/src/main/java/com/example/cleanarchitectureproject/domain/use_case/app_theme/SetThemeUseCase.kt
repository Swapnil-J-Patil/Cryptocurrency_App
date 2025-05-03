package com.example.cleanarchitectureproject.domain.use_case.app_theme

import com.example.cleanarchitectureproject.domain.repository.ThemeRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(private val repository: ThemeRepository) {
    suspend operator fun invoke(isDark: Boolean) = repository.setTheme(isDark)
}