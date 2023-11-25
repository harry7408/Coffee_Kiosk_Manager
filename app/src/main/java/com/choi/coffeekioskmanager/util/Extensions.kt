package com.choi.coffeekioskmanager.util

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import reactivecircus.flowbinding.android.view.focusChanges
import reactivecircus.flowbinding.android.widget.AfterTextChangeEvent
import reactivecircus.flowbinding.android.widget.afterTextChanges

fun TextInputLayout.afterTextChangesInFlow(actionInMainThread: (TextInputLayout, AfterTextChangeEvent) -> Unit) {
    if (this.editText != null) {
        this.editText!!.afterTextChanges()
            .onEach { event ->
                actionInMainThread(this, event)
            }
            .launchIn(CoroutineScope(Dispatchers.Main))
    }
}

fun TextInputLayout.focusChangesInFlow(actionInMainThread: (TextInputLayout, Boolean) -> Unit) {
    if (this.editText != null) {
        this.editText!!.focusChanges()
            .onEach {
                actionInMainThread(this, it)
            }.launchIn(CoroutineScope(Dispatchers.Main))
    }
}


fun TextInputLayout.initInFlow(hint: String, helperText: String) {
    this.hint = hint
    this.helperText = helperText
}

fun View.setOnAvoidDuplicateClickFlow(actionInMainThread: () -> Unit) {
    this.clicks()
        .flowOn(Dispatchers.Main) // 이후 chain의 메서드들은 쓰레드 io 영역에서 처리
        .throttleFirst(INTERVAL_TIME)
        .flowOn(Dispatchers.IO) // 이후 chain의 메서드들은 쓰레드 main 영역에서 처리
        .onEach {// onEach{}를 main에서 실행
            actionInMainThread()
        }.launchIn(CoroutineScope(Dispatchers.Main))
}

// throttleFirst()는 Flow에 없기 때문에 직접 구현해줘야 한다. debounce()는 있다.
private fun <T> Flow<T>.throttleFirst(intervalTime: Long): Flow<T> = flow {
    var throttleTime = 0L
    collect { upStream ->
        val currentTime = System.currentTimeMillis()
        if ((currentTime - throttleTime) > intervalTime) {
            throttleTime = currentTime
            emit(upStream)
        }
    }
}