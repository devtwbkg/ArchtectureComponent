package xyz.twbkg.myviewmodel.ui

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit.*
import xyz.twbkg.myviewmodel.R
import xyz.twbkg.myviewmodel.inject.Injection
import xyz.twbkg.myviewmodel.persistence.Address
import xyz.twbkg.myviewmodel.persistence.User
import xyz.twbkg.myviewmodel.persistence.UserDetail

class EditActivity : AppCompatActivity() {
    private val TAG = EditActivity::class.java.simpleName

    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: UserViewModel

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        var dao = intent.getParcelableExtra<User>("model")
        viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)

        update_button?.setOnClickListener {
            getUserDetail()
        }

        dao?.let {
            input_username.setText(it.user_name)
            input_password.setText(it.password)
            input_first_name.setText(it.userInfo.firstName)
            input_last_name.setText(it.userInfo.lastName)
            input_city.setText(it.userInfo.address.city)
        }
    }

    private fun getUserDetail() {
        var userName = input_username.text.toString()
        var password = input_password.text.toString()
        var firstName = input_first_name.text.toString()
        var lastName = input_last_name.text.toString()
        var city = input_city.text.toString()

        var address = Address(city)
        var userDetail = UserDetail(firstName, lastName, address)
        var user = User(userName, password, userDetail).also {
            Log.i(TAG, "user info $it")
            addUser(it)
        }
    }


    private fun addUser(user: User) {
        disposable.add(
                viewModel.updateUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.d(TAG, "insert user success")
                            finish()
                        }, { error ->
                            Log.e(TAG, "insert user fail", error)
                            Toast.makeText(applicationContext, "insert user fail", Toast.LENGTH_SHORT).show()
                        })
        )
    }
}
