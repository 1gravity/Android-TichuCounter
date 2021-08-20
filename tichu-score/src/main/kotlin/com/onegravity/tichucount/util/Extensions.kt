package com.onegravity.tichucount.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun <T> ViewModel.launch(block: suspend CoroutineScope.() -> T) =
    viewModelScope.launch { block() }
