package com.reputationoverflow.session

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Dao
interface SessionDao {
    @Query("SELECT * FROM session_db LIMIT 1")
    fun getSession(): LiveData<SessionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(session: SessionEntity)

    @Query("DELETE FROM session_db")
    fun deleteAll(): Int

    @Transaction
    fun replaceSession(session: SessionEntity) {
        deleteAll()
        insert(session)
    }
}

@Database(entities = [SessionEntity::class], version = 2)
abstract class SessionDatabase: RoomDatabase() {
    abstract val sessionDao: SessionDao
}

@Module
@InstallIn(ApplicationComponent::class)
object SessionDatabaseModule {

    @Singleton
    @Provides
    fun provideSessionDao(sessionDatabase: SessionDatabase): SessionDao {
        return sessionDatabase.sessionDao
    }

    @Singleton
    @Provides
    fun provideSessionDatabase(@ApplicationContext context: Context): SessionDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SessionDatabase::class.java,
            "session.sqlite3"
        )
            .build()
    }
}
