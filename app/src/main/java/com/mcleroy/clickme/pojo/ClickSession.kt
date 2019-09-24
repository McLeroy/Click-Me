package com.mcleroy.clickme.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ClickSession(
    @PrimaryKey val email: String,
    @ColumnInfo(name = "click_count") var clickCount: Int,
    @ColumnInfo(name = "last_click_time") val lastClickTime: Long)