package com.maulana.netweezen.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val name: String,
    val photo: Int
): Parcelable