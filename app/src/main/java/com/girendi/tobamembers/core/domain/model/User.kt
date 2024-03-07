package com.girendi.tobamembers.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class User (
    val id: Int = 0,
    var username: String,
    var email: String,
    var password: String,
    var role: String,
): Parcelable