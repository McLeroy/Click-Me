package com.mcleroy.clickme.database

import androidx.lifecycle.LiveData
import androidx.room.*

import com.mcleroy.clickme.App
import com.mcleroy.clickme.pojo.ClickSession
import com.mcleroy.clickme.utils.Converters

@TypeConverters(Converters::class)
@Database(version = 1, entities = arrayOf(ClickSession::class), exportSchema = false)
abstract class ClickSessionDatabase : RoomDatabase() {
    companion object {

        @Volatile private var instance: ClickSessionDatabase? = null

        fun get(): ClickSessionDatabase {
            synchronized(ClickSessionDatabase::class.java.simpleName) {
                if (instance == null)
                    instance = Room.databaseBuilder(
                        App.getContext(),
                        ClickSessionDatabase::class.java,
                        "click_db"
                    ).build()
            }
            return instance!!
        }
    }

    abstract val clickSessionDao: ClickSessionDao;

    @Dao
    interface ClickSessionDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(vararg clickSession: ClickSession)

        @Query("SELECT * FROM ClickSession WHERE email= :email")
        fun getClickSession(email: String): LiveData<ClickSession>

        @Query("SELECT * FROM ClickSession WHERE email= :email")
        fun getClickSessionSync(email: String): ClickSession?

        @Query("DELETE FROM ClickSession WHERE email= :email")
        fun deleteClickSession(email: String)
    }
}

