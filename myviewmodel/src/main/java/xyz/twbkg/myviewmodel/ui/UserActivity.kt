package xyz.twbkg.myviewmodel.ui

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user.*
import xyz.twbkg.myviewmodel.R
import xyz.twbkg.myviewmodel.inject.Injection

class UserActivity : AppCompatActivity() {
    companion object {
        val TAG = UserActivity::class.java.simpleName
    }

    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: UserViewModel

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val id = intent.getLongExtra("id", 0L)
        viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)

        if (id > 0L) {
            getUserById(id)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun getUserById(id: Long) {
        disposable.addAll(
                viewModel.getUserById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.d(TAG, "user $it")
                            result_tv.text = "$it"
                        }, { error -> Log.e(TAG, "get user by id fail", error) })
        )
    }
}
