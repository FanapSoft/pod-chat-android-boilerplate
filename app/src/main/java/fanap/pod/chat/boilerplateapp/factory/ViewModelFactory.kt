package fanap.pod.chat.boilerplateapp.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fanap.pod.chat.boilerplateapp.ui.login.LoginViewModel

import fanap.pod.chat.boilerplateapp.ui.main.MainViewModel

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class ViewModelFactory (): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when(modelClass){
            LoginViewModel::class.java->{
                return LoginViewModel() as T
            }
            MainViewModel::class.java->{
                return MainViewModel() as T
            }
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}