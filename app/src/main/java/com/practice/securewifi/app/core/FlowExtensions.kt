package com.practice.securewifi.app.core

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

/**
 * For collecting flows on UI (Main)
 */
fun <T> Flow<T>.launchOnStarted(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launchWhenStarted {
        this@launchOnStarted.flowOn(Dispatchers.Main).collect()
    }
}