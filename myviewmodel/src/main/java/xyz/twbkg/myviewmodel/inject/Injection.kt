package xyz.twbkg.myviewmodel.inject

import android.content.Context
import xyz.twbkg.myviewmodel.persistence.UserDao
import xyz.twbkg.myviewmodel.persistence.UsersDatabase
import xyz.twbkg.myviewmodel.ui.ViewModelFactory

object Injection {
    fun provideUserDataSource(context: Context): UserDao {
        val database = UsersDatabase.getInstance(context)
        return database.userDao()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideUserDataSource(context)
        return ViewModelFactory(dataSource)
    }
}