package com.example.lethireheisenbergcompose.ui.profile

import androidx.annotation.DrawableRes
import javax.annotation.concurrent.Immutable

@Immutable
data class ProfileScreenState(
    val userId: String,
    @DrawableRes val photo: Int?,
    val name: String,
    val status: String,
    val displayName: String,
    val position: String,
    val twitter: String = "",
    val timeZone: String?, // Null if me
    val commonChannels: String? // Null if me
) {
    //fun isMe() = userId == meProfile.userId
}