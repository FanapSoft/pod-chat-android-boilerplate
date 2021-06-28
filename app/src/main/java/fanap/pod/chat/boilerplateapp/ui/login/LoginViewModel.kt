@file:Suppress("SENSELESS_COMPARISON")

package fanap.pod.chat.boilerplateapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fanap.pod.chat.boilerplateapp.App
import fanap.pod.chat.boilerplateapp.R
import fanap.pod.chat.boilerplateapp.data.AppDataManager
import fanap.pod.chat.boilerplateapp.data.remote.models.SSoTokenResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel : ViewModel() {
    private val mTAG: String = "MainViewModel"
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private lateinit var number: String
    private lateinit var keyId: String
    private var mCompositeDisposable: CompositeDisposable? = null
    private var dataManager: AppDataManager = App.factory.getAppDataManager()

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    fun loginDataChanged(phoneNumber: String) {
        if (!phoneNumberValid(phoneNumber)) {
            _loginForm.value = LoginFormState(phoneNumberError = R.string.invalid_phone_number,type = 0)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true,type = 0)
        }
    }

    fun verifyDataChanged(code: String) {
        if (!isCodeValid(code)) {
            _loginForm.value = LoginFormState(verifyCodeError = R.string.invalid_verify,type = 1)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true,type = 1)
        }
    }

    // A placeholder username validation check
    private fun phoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.length == 11
    }

    // A placeholder password validation check
    private fun isCodeValid(password: String): Boolean {
        return password.length in 4..7
    }


    fun handshake(phoneNumber: String) {
            mCompositeDisposable?.add(dataManager.handShake(
                "fanap",
                "android",
                "10",
                "MOBILE_PHONE",
                App.getInstance()?.getDeviceId()!!
            )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ response ->
                    response!!.result!!.keyId?.let {
                        keyId = it
                        number = phoneNumber
                        login()
                    }
                    Log.e(mTAG, "handShake: ")
                }
                ) {
                    Log.e(mTAG, "throwable: ")
                })
    }

    private fun login() {
        mCompositeDisposable?.add(
            dataManager.login(
                number, keyId
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if(it?.getResult()?.userId != null) {
                        _loginResult.value =
                            LoginResult(success = true, message = R.string.verify_sent, state = 0)
                    }else{
                        _loginResult.value =
                            LoginResult(success = false,message =  R.string.verify_dontsent,state = 0)
                    }
                }
                ) {
                    _loginResult.value =
                        LoginResult(success = false,message =  R.string.verify_dontsent,state = 0)
                }
        )
    }

    fun verifyNumber(verifyCode: String) {
        mCompositeDisposable?.add(dataManager.verifyNumber(keyId, number, verifyCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it?.result?.accessToken != null) {
                    saveAuthData(it)
                } else {
                    _loginResult.value = LoginResult(success = false,message = R.string.login_failed)
                }
            }
            ) {
                Log.e(mTAG, "throwable: ")
                _loginResult.value = LoginResult(success = false,message = R.string.login_failed)
            })
    }

    private fun saveAuthData(ssoRes: SSoTokenResult) {
        ssoRes.result?.refreshToken?.let { dataManager.saveRefreshToken(it) }
        dataManager.changeLoginState(true)
        _loginResult.value =
            LoginResult(success = true,message =  R.string.login_success,state = 1)
    }
}
