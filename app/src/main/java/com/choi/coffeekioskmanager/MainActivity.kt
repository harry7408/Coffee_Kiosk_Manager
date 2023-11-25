package com.choi.coffeekioskmanager

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.choi.coffeekioskmanager.databinding.ActivityMainBinding
import com.choi.coffeekioskmanager.util.afterTextChangesInFlow
import com.choi.coffeekioskmanager.util.focusChangesInFlow
import com.choi.coffeekioskmanager.util.initInFlow
import com.choi.coffeekioskmanager.util.setOnAvoidDuplicateClickFlow
import com.google.android.material.textfield.TextInputLayout
import reactivecircus.flowbinding.android.widget.AfterTextChangeEvent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        initView()
        checkValidate()

        with(binding) {
            makeButton.setOnAvoidDuplicateClickFlow {
                //todo Firebase FireStore와 연결
            }
        }
    }

    private fun initView() {
        binding.missionInputLayout.initInFlow(
            getString(R.string.edt_hint),
            getString(R.string.helper)
        )
    }

    private fun checkValidate() {
        with(binding) {
            missionInputLayout.focusChangesInFlow(hasFocus)
            missionInputLayout.afterTextChangesInFlow(inputMission)
        }
    }

    private val inputMission = { layout: TextInputLayout, event: AfterTextChangeEvent ->
        val errorMessage = when (event.view.text.length) {
            0 -> getString(R.string.error_messsage)
            else -> null
        }
        if (layout.hasFocus()) {
            layout.error = errorMessage
        } else {
            layout.error = null
        }
    }


    /**
     * Dispatch touch event
     * Soft Keyboard 닫는 방법
     * @param ev
     * @return
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView: View? = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private val hasFocus =
        { layout: TextInputLayout, hasFocus: Boolean -> if (hasFocus) layout.error = null }
}