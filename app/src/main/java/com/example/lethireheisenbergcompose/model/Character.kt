package com.example.lethireheisenbergcompose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/*
*
*
id	integer	Unique Id per character
name	string	A character's full name
birthday	string	A character's birthday
occupation	array	List of character's known occupation
img	string	Character's image (as jpg)
status	string	Are they alive(or did Heisenberg get to them??)
nickname	string	A known nickname they are refered as
appearance	array	List of seasons that the character appeared in
*
*
* */


@Parcelize
@Serializable
data class Character (
    val char_id: Int,
    val name: String,
    val birthday: String,
    val occupation: List<String>,
    val img: String,
    val status: String,
    val nickname: String,
    val appearance: List<Int>,
    val portrayed: String,
    val category: String,
    val better_call_saul_appearance: List<Int>) : Parcelable {

//    companion object {
//        fun create(): Character {
//            return Character(0, "", "", emptyList(), "", "", "", emptyList(), "", "", emptyList())
//        }
//    }
}