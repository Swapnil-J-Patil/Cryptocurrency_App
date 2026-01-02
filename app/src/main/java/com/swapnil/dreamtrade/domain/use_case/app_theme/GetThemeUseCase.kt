package com.swapnil.dreamtrade.domain.use_case.app_theme

import com.swapnil.dreamtrade.domain.repository.ThemeRepository
import javax.inject.Inject


class GetThemeUseCase @Inject constructor(private val repository: ThemeRepository) {
    operator fun invoke() = repository.getTheme()
}
