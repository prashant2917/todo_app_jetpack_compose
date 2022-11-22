package com.pocket.contactappsjetpackcompose

import android.content.Context
import androidx.room.Room
import com.pocket.contactappsjetpackcompose.database.TodoDatabase
import com.pocket.contactappsjetpackcompose.database.TodoDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideChannelDao(todoDatabase: TodoDatabase): TodoDatabaseDao {
        return todoDatabase.todoDao()
    }

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext appContext: Context): TodoDatabase {
        return Room.databaseBuilder(
            appContext,
            TodoDatabase::class.java,
            "todo_list_database"
        ).build()
    }
}