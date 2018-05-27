package xyz.twbkg.myviewmodel.ui

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add.*
import xyz.twbkg.myviewmodel.R
import xyz.twbkg.myviewmodel.inject.Injection
import xyz.twbkg.myviewmodel.persistence.Address
import xyz.twbkg.myviewmodel.persistence.User
import xyz.twbkg.myviewmodel.persistence.UserDetail

class AddActivity : AppCompatActivity() {
    companion object {
        val TAG = AddActivity::class.java.simpleName
    }

    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: UserViewModel

    private val disposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)

        add_button?.setOnClickListener {
            getUserDetail()
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
                viewModel.insertUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.d(TAG, "insert user success")
                        }, { error -> Log.e(TAG, "insert user success", error) })
        )
    }
}
