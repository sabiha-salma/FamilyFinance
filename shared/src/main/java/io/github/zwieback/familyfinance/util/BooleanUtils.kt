package io.github.zwieback.familyfinance.util

import android.os.Parcel

object BooleanUtils {

    fun writeBooleanToParcel(out: Parcel, value: Boolean) {
        out.writeInt(if (value) 1 else 0)
    }

    fun readBooleanFromParcel(`in`: Parcel): Boolean {
        return `in`.readInt() == 1
    }
}
