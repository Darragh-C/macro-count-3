package org.wit.macrocount.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class UserModel (var id: Long = 0,
                 var name: String = "",
                 var gender: String = "",
                 var weight: String = "",
                 var height: String = "",
                 var dob: String = "",) : Parcelable

