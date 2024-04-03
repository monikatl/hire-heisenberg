package com.example.lethireheisenbergcompose

import com.example.lethireheisenbergcompose.model.Character
import com.example.lethireheisenbergcompose.model.Service
import com.example.lethireheisenbergcompose.model.ServiceProvider
import com.example.lethireheisenbergcompose.utils.generateId

object CharacterTempRepo {
    val sP =  listOf (

        ServiceProvider(
            "Walter White",
            Character(1, "Walter White", "09-07-1958",
                listOf("High School Chemistry Teacher",
                    "Meth King Pin"),
                "https://images.amcnetworks.com/amc.com/wp-content/uploads/2015/04/cast_bb_700x1000_walter-white-lg.jpg",
                "Presumed dead",
                "Heisenberg",
                listOf(1, 2, 3, 4, 5), "Bryan Cranston",
                "Breaking Bad",
                listOf()), 56.25, true, listOf(Service.SECURITY) ),

        ServiceProvider(
            "Jessee Pinkman",
            Character(2, "Jesse Pinkman", "09-24-1984",
                listOf( "Meth Dealer"),
                "https://upload.wikimedia.org/wikipedia/en/c/c6/Jesse_Pinkman_S5B.png",
                "Alive",
                "Cap n' Cook",
                listOf(1, 2, 3, 4, 5), "Aaron Paul",
                "Breaking Bad",
                listOf()), 70.25, true, listOf(Service.COOK) )
    )
}