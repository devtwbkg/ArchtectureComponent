package xyz.twbkg.myviewmodel.ui

import android.arch.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import xyz.twbkg.myviewmodel.persistence.User
import xyz.twbkg.myviewmodel.persistence.UserDao

class UserViewModel(private val dataSource: UserDao) : ViewModel() {

    fun getAllUsers(): Flowable<List<User>> {
        return dataSource.getAllUsers()
    }

    fun getUserById(id: Long): Flowable<User> {
        return dataSource.getUserById(id)
    }

    fun insertUser(user: User): Completable {
        return Completable.fromAction {
            dataSource.insertUser(user)
        }
    }

    fun updateUser(user: User): Completable {
        return Completable.fromAction {
            dataSource.updateUser(user)
        }
    }

    fun deleteAllUser(): Completable {
        return Completable.fromAction {
            dataSource.deleteAllUsers()
        }
    }
}