package com.reputationoverflow.session

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var dao: SessionDao
    private lateinit var db: SessionDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, SessionDatabase::class.java).build()
        dao = db.sessionDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /*@Test
    @Throws(Exception::class)
    fun Gi() {
        val user: User = TestUtil.createUser(3).apply {
            setName("george")
        }
        userDao.insert(user)
        val byName = userDao.findUsersByName("george")
        assertThat(byName.get(0), equalTo(user))
    }*/
}