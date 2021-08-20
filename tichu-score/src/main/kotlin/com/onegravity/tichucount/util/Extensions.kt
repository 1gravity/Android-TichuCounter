package com.onegravity.tichucount.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun <T> ViewModel.launch(block: suspend CoroutineScope.() -> T) =
    viewModelScope.launch { block() }

fun <T> ViewModel.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> T
) = viewModelScope.launch(context) { block() }
