package com.example.lethireheisenbergcompose

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.lethireheisenbergcompose.data.AuthRepository
import com.example.lethireheisenbergcompose.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
open class HeisenbergViewModel @Inject constructor(
    private val application: Application,
    open val myDatabase: FirebaseFirestore,
    private val authRepository: AuthRepository
) : AndroidViewModel(application = application) {


}