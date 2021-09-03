package com.example.roomdb

import android.app.Application
import com.example.roomdb.data.db.WordRoomDatabase
import com.example.roomdb.repositery.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WordsApplication: Application()  {
  // Using by lazy so the database and the repository are only created when they're needed
  // rather than when the application starts
  // No need to cancel this scope as it'll be torn down with the process
  val applicationScope = CoroutineScope(SupervisorJob())

  val database by lazy { WordRoomDatabase.getDatabase(this,scope = applicationScope) }
  val repository by lazy { WordRepository(database.wordDao()) }
}