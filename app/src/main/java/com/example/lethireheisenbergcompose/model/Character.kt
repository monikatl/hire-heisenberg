package com.example.lethireheisenbergcompose.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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


/*
{
        "char_id": 1,
        "name": "Walter White",
        "birthday": "09-07-1958",
        "occupation": [
            "High School Chemistry Teacher",
            "Meth King Pin"
        ],
        "img": "https://images.amcnetworks.com/amc.com/wp-content/uploads/2015/04/cast_bb_700x1000_walter-white-lg.jpg",
        "status": "Presumed dead",
        "nickname": "Heisenberg",
        "appearance": [
            1,
            2,
            3,
            4,
            5
        ],
        "portrayed": "Bryan Cranston",
        "category": "Breaking Bad",
        "better_call_saul_appearance": []
    }
 */

@Parcelize
class Character (
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

    companion object {
        fun create(): Character {
            return Character(0, "", "", emptyList(), "", "", "", emptyList(), "", "", emptyList())
        }
    }
}