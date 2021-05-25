package fanap.pod.chat.boilerplateapp.ui.login

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import fanap.pod.chat.boilerplateapp.databinding.ActivityLoginBinding
import fanap.pod.chat.boilerplateapp.factory.ViewModelFactory
import fanap.pod.chat.boilerplateapp.utils.Utility
import fanap.pod.chat.boilerplateapp.utils.Utility.showProgressBar

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    private lateinit var login : Button
    private lateinit var verify  : Button
    private lateinit var editTextPhone :EditText
    private lateinit var editTextVerify : EditText
    private lateinit var textInputVerify : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        login = binding.cirLoginButton
        verify = binding.cirVerifyButton
        editTextPhone = binding.editTextPhone
        editTextVerify = binding.editTextVerify
        textInputVerify = binding.textInputVerify


        loginViewModel = ViewModelProvider(this, ViewModelFactory())
            .get(LoginViewModel::class.java)
        if (editTextPhone.text.isNullOrEmpty())
            login.isEnabled = false
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer
            when (loginState.type) {
                0 -> {
                    setLoginMode(loginState.isDataValid)
                    if (loginState.phoneNumberError != null) {
                        editTextPhone.error = getString(loginState.phoneNumberError)
                    }
                }
                1 -> {
                    setVerifyMode(loginState.isDataValid)
                    if (loginState.verifyCodeError != null) {
                        editTextVerify.error = getString(loginState.verifyCodeError)
                    }
                }
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            Utility.hideProgressBar()

            if (loginResult.success) {
                when (loginResult.state) {
                    0 -> {
                        loginResult.message?.let { it1 -> showMessage(it1) }
                        textInputVerify.visibility = View.VISIBLE
                        setVerifyMode(false)
                        editTextVerify.requestFocus();
                    }
                    1 -> {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            } else {
                loginResult.message?.let { it1 -> showMessage(it1) }
            }

        })

        editTextPhone.afterTextChanged {
            if (!login.isEnabled) {
                editTextVerify.text.clear()
                textInputVerify.visibility = View.GONE
                verify.visibility = View.GONE
                login.isEnabled = false
            }
            loginViewModel.loginDataChanged(
                editTextPhone.text.toString()
            )
        }



        editTextVerify.apply {
            afterTextChanged {
                loginViewModel.verifyDataChanged(
                    editTextVerify.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                      context.showProgressBar()
                        loginViewModel.verifyNumber(
                            editTextVerify.text.toString()
                        )
                    }
                }
                false
            }

            login.setOnClickListener {
                context.showProgressBar()
                loginViewModel.handshake(
                    editTextPhone.text.toString()
                )
            }

            verify.setOnClickListener {
                context.showProgressBar()
                loginViewModel.verifyNumber(
                    editTextVerify.text.toString()
                )
            }
        }
    }

    private fun setLoginMode(state: Boolean) {
        login.isEnabled = state
        login.visibility = View.VISIBLE
        verify.visibility = View.INVISIBLE
    }

    private fun setVerifyMode(state: Boolean) {
        verify.isEnabled = state
        verify.visibility = View.VISIBLE
        login.visibility = View.INVISIBLE
    }

    private fun showMessage(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}