package com.practice.securewifi.core.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * For collecting flows on UI (Main)
 */
fun <T> Flow<T>.collectOnStarted(
    lifecycleScope: LifecycleCoroutineScope,
    lifecycle: Lifecycle,
    doOnEach: (T) -> Unit
) {
    lifecycleScope.launch {
        this@collectOnStarted.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .flowOn(Dispatchers.Main)
            .collect(doOnEach)
    }
}