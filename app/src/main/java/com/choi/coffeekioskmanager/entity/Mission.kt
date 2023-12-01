package com.choi.coffeekioskmanager.entity

import com.google.firebase.database.Exclude

data class Mission(
    val _id: Long = -1,
    val name: String = "",
    val missionDetail: String = "",
    val difficulty: String = "",
    val isSolved: Boolean=false,
) {
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "_id" to _id,
            "name" to name,
            "missionDetail" to missionDetail,
            "difficulty" to difficulty,
            "isSolved" to isSolved
        )
    }
}
