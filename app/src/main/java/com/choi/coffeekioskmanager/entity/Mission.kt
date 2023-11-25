package com.choi.coffeekioskmanager.entity

import com.google.firebase.database.Exclude

data class Mission(
    val _id: Long=-1,
    val missionDetail: String="",
    val difficulty: String="",
) {
    @Exclude
    fun toMap() : Map<String,Any> {
        return mapOf(
            "_id" to _id,
            "missionDetail" to missionDetail,
            "difficulty" to difficulty
        )
    }
}
