package com.choi.coffeekioskmanager

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.choi.coffeekioskmanager.databinding.ActivityMainBinding
import com.choi.coffeekioskmanager.entity.Mission
import com.choi.coffeekioskmanager.util.EASY_COLLECTIONS
import com.choi.coffeekioskmanager.util.HARD_COLLECTIONS
import com.choi.coffeekioskmanager.util.NORMAL_COLLECTIONS
import com.choi.coffeekioskmanager.util.afterTextChangesInFlow
import com.choi.coffeekioskmanager.util.focusChangesInFlow
import com.choi.coffeekioskmanager.util.initInFlow
import com.choi.coffeekioskmanager.util.setOnAvoidDuplicateClickFlow
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import reactivecircus.flowbinding.android.widget.AfterTextChangeEvent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val fireStoreDB = Firebase.firestore
    private var fireStoreCollectionName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        initView()
        checkValidate()


        with(binding) {
            makeButton.setOnAvoidDuplicateClickFlow {
                if (canMakeMission()) {
                    val checkedChipId = chipGroup.checkedChipId
                    val checkedChip = findViewById<Chip>(checkedChipId)

                    val missionInfo = Mission(
                        missionNumberInputEditText.text.toString().trim().toLong(),
                        missionNameInputEditText.text.toString().toString(),
                        missionDetailInputEditText.text.toString().trim(),
                        checkedChip.text.toString().trim(),
                        false
                    )

                    insertFireStore(missionInfo)

                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.error_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initView() {
        with(binding) {
            missionDetailInputLayout.initInFlow(
                getString(R.string.edt_hint),
                getString(R.string.helper)
            )

            missionNameInputLayout.initInFlow(
                getString(R.string.edt_name),
                getString(R.string.name_helper)
            )

            missionNumberInputLayout.initInFlow(
                getString(R.string.edt_number),
                getString(R.string.number_helper)
            )
        }

    }

    private fun checkValidate() {
        with(binding) {
            missionDetailInputLayout.apply {
                focusChangesInFlow(hasFocus)
                afterTextChangesInFlow(inputMission)
            }

            missionNameInputLayout.apply {
                focusChangesInFlow(hasFocus)
                afterTextChangesInFlow(inputName)
            }

            missionNumberInputLayout.apply {
                focusChangesInFlow(hasFocus)
                afterTextChangesInFlow(inputNumber)
            }

        }
    }

    private val inputNumber = checkInputError()
    private val inputName = checkInputError()
    private val inputMission = checkInputError()

    private val hasFocus =
        { layout: TextInputLayout, hasFocus: Boolean -> if (hasFocus) layout.error = null }

    private fun canMakeMission(): Boolean {
        with(binding) {
            val isEditTextNotEmpty =
                missionDetailInputEditText.text.toString().trim().isNotEmpty() &&
                        missionNumberInputEditText.text.toString().trim().isNotEmpty()
            chipGroup.isSingleSelection = true
            val isChipChecked = chipGroup.checkedChipId != View.NO_ID
            return isEditTextNotEmpty && isChipChecked
        }
    }

    private fun checkInputError() = { layout: TextInputLayout, event: AfterTextChangeEvent ->
        val errorMessage = when (event.editable?.length) {
            0 -> getString(R.string.error_message)
            else -> null
        }
        if (layout.hasFocus()) {
            layout.error = errorMessage
        } else {
            layout.error = null
        }
    }

    private fun insertFireStore(mission: Mission) {
        val checkedChipId=binding.chipGroup.checkedChipId
        val chip=findViewById<Chip>(checkedChipId)
        when(chip.text) {
            getString(R.string.easy) -> fireStoreCollectionName= EASY_COLLECTIONS
            getString(R.string.normal) -> fireStoreCollectionName= NORMAL_COLLECTIONS
            getString(R.string.hard) -> fireStoreCollectionName= HARD_COLLECTIONS
        }
        val documentRef = fireStoreDB.collection(fireStoreCollectionName)
        documentRef.add(mission.toMap()).addOnSuccessListener {
            Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
            clearTextField()
        }.addOnFailureListener {
            Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
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

    private fun clearTextField() {
        with(binding) {
            missionDetailInputEditText.setText("")
            missionNameInputEditText.setText("")
            missionNumberInputEditText.setText("")
            val checkedChipId=binding.chipGroup.checkedChipId
            val chip=findViewById<Chip>(checkedChipId)
            chip.isChecked=false
            initView()
        }
    }
}