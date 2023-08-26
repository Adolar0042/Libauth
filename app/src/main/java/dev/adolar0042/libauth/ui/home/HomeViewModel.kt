package dev.adolar0042.libauth.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dev.adolar0042.libauth.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _response = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Empty)
    val response: StateFlow<HomeScreenUiState> = _response.asStateFlow()

    init {
        Log.i(this::class.simpleName, "${this::class.simpleName} initialized")

        _response.value = HomeScreenUiState.Loading
        val msg = homeRepository.loadData()
        _response.value = HomeScreenUiState.Success(data = msg)
    }

    var menuState by mutableStateOf(false)

    fun toggleMenu() {
        menuState = !menuState
    }
}
