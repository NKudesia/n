package com.example.healthgates.ui.viewmodel

import androidx.lifecycle.ViewModel;
import com.example.healthgates.data.models.User
import com.example.healthgates.data.repositories.LoginRepository
import com.example.healthgates.ui.interfaces.ApiListener

class LoginViewModel : ViewModel() {

    private val repository: LoginRepository = LoginRepository()
    private var user: User? = null

    fun login(user: User, listener: ApiListener) = repository.login(user, listener)

    fun getSignUpData(): User? = user

    fun setSignUpData(updated: User) {
        user = updated
    }

    fun signUp(user: User, listener: ApiListener) = repository.signUp(user, listener)

    fun resetPassword(email: String, listener: ApiListener) = repository.resetPassword(email, listener)



}