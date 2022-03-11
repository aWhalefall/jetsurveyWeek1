package com.example.compose.jetsurvey.signinsignup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.compose.jetsurvey.Screen
import com.example.compose.jetsurvey.util.Event
import java.lang.IllegalArgumentException

class WelcomeViewModel(val userRepository: UserRepository) : ViewModel() {

    val _navigateTo = MutableLiveData<Event<Screen>>()
    val navigateTo: LiveData<Event<Screen>> = _navigateTo

    fun handeContinue(email: String) {
        if (userRepository.isKnownUserEmail(email = email)) {
            _navigateTo.value = Event(Screen.SignIn)
        } else {
            _navigateTo.value = Event(Screen.SignUp)
        }
    }

    fun signInAsGuest() {
        userRepository.signInAsGuest()
        _navigateTo.value = Event(Screen.Survey)
    }

    class WelcomeViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
                return WelcomeViewModel(UserRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}
