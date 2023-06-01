package com.ayushsinghal.notesapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenDuration {
    private val _isloading = MutableStateFlow(true)
    val isloading = _isloading.asStateFlow()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1730)
            _isloading.value = false
        }
    }
}