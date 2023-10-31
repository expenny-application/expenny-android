package org.expenny.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import org.expenny.core.domain.usecase.profile.GetProfileSetUpUseCase
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val getProfileSetUp: GetProfileSetUpUseCase,
) : ViewModel() {

    val isProfileStored: StateFlow<Boolean?> = getProfileSetUp()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}