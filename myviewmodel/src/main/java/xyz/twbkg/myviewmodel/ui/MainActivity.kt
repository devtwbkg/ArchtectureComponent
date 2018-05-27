package xyz.twbkg.myviewmodel.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import xyz.twbkg.myviewmodel.R
import xyz.twbkg.myviewmodel.inject.Injection
import xyz.twbkg.myviewmodel.persistence.User
import xyz.twbkg.myviewmodel.ui.adapter.UserAdapter

class MainActivity : AppCompatActivity(), UserAdapter.UserAdapterCallback {

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: UserViewModel

    private lateinit var userAdapter: UserAdapter

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)

        initView()
    }

    private fun initView() {
        userAdapter = UserAdapter(this)
        users_ry?.apply {
            adapter = userAdapter
        }

        save_button?.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        getAllUsers()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    override fun onViewUser(user: User) {
        startActivity(Intent(this, UserActivity::class.java).apply {
            putExtra("id", user.id)
        })
    }

    override fun onUpdateUser(user: User) {
        startActivity(Intent(this, EditActivity::class.java).apply {
            putExtra("model", user)
        })
    }

    private fun callDialogViewUser(user: User) = MaterialDialog.Builder(this)
            .title("user data")
            .content(user.userInfo.firstName).show()

    private fun callDialog(user: User) = MaterialDialog.Builder(this)
            .title("user data")
            .inputType(InputType.TYPE_CLASS_TEXT)
            .input("input your content", user.userInfo.firstName, { dialog, input ->
                // Do somethingd
                dialog.dismiss()
                user.let { userModel ->
                    userModel.userInfo.firstName = input.toString()
                }.run {
                    updateUser(user)
                }
            }).show()

    private fun addItem(user: User) {
        disposable.add(
                viewModel.insertUser(user)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            Log.d(TAG, "insert user $user success")
                        }, { error -> Log.e(TAG, "insert fail ", error) })
        )
    }

    private fun updateUser(user: User) {
        disposable.add(
                viewModel.updateUser(user)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            Log.d(TAG, "update user success $user")
                        }, { error -> Log.e(TAG, "update user fail", error) })
        )
    }

    private fun getUserById(id: Long) {
        disposable.add(
                viewModel.getUserById(id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            Log.d(TAG, "get user by id $id :: $it")
                            it?.let { user ->
                                callDialogViewUser(user)
                            }
                        }, { error -> Log.e(TAG, "get user by id fail", error) })
        )
    }

    private fun getAllUsers() {
        disposable.add(
                viewModel.getAllUsers()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            userAdapter.addItems(it)
                        }, { error -> Log.e(TAG, "get user fail", error) })
        )
    }

    private fun delteAllUsers() {
        disposable.add(
                viewModel.deleteAllUser()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            Log.d(TAG, "delete user success")
                        }, { error -> Log.e(TAG, "delete user fail", error) })
        )
    }
}
