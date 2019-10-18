package io.github.zwieback.familyfinance.util

import android.os.Parcel

object BooleanUtils {

    @JvmStatic
    fun writeBooleanToParcel(out: Parcel, value: Boolean) {
        out.writeInt(if (value) 1 else 0)
    }

    @JvmStatic
    fun readBooleanFromParcel(`in`: Parcel): Boolean {
        return `in`.readInt() == 1
    }
}
