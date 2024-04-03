package com.example.lethireheisenbergcompose

import com.example.lethireheisenbergcompose.ui.login.LoginViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
@HiltAndroidTest
class LoginTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Inject
    lateinit var viewModel: LoginViewModel // Przykładowy ViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testLoginSuccess() {
        val auth = mock(FirebaseAuth::class.java)
        val user = mock(FirebaseUser::class.java)
        `when`(auth.currentUser).thenReturn(user)

        viewModel.loginUser("moniczka123@gmail.com", "aaaaaa")

        verify(auth, times(1)).signInWithEmailAndPassword("moniczka123@gmail.com", "aaaaaa")
        assert(viewModel.signInState.asLiveData().value?.isSuccess == "Sign In Success ")
    }

    @Test
    fun testLoginFailure() {
        val auth = mock(FirebaseAuth::class.java)
        `when`(auth.currentUser).thenReturn(null)

        viewModel.loginUser("wrong@email.com", "wrongpassword")

        verify(auth, times(1)).signInWithEmailAndPassword("wrong@email.com", "wrongpassword")
        assert(viewModel.signInState.asLiveData().value?.isSuccess == "")
    }
}
