package com.danchoo.sample.gallery.presentation.base

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface BaseIntent
interface BaseViewState
interface BaseSideEffect

abstract class BaseViewModel<Event : BaseIntent, UIState : BaseViewState, SideEffect : BaseSideEffect> :
    ViewModel() {

    private val initialState: UIState by lazy { setInitialState() }

    private val _viewState: MutableState<UIState> = mutableStateOf(initialState)

    private val _sideEffect: Channel<SideEffect> = Channel()
    val sideEffect: Flow<SideEffect> = _sideEffect.receiveAsFlow()

    val viewState: State<UIState> = _viewState

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    abstract fun setInitialState(): UIState
    abstract fun handleEvents(event: Event)

    init {
        subscribeToEvents()
    }

    fun setEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    protected fun setState(reducer: UIState.() -> UIState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect {
                handleEvents(it)
            }
        }
    }

    protected fun setEffect(builder: () -> SideEffect) {
        val effectValue = builder()
        viewModelScope.launch { _sideEffect.send(effectValue) }
    }
}