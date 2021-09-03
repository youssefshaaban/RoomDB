package com.example.roomdb.presntation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdb.R
import com.example.roomdb.WordsApplication
import com.example.roomdb.data.Word
import com.example.roomdb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val wordViewModel: WordViewModel by viewModels {
    WordViewModelFactory((application as WordsApplication).repository)
  }
  lateinit var adapter:WordListAdapter
  private val newWordActivityRequestCode = 1


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setSupportActionBar(binding.toolbar)
    adapter= WordListAdapter()
    binding.contentRv.rv.adapter = adapter
    binding.contentRv.rv.layoutManager = LinearLayoutManager(this)
    binding.fab.setOnClickListener { view ->
      val intent = Intent(this@MainActivity, NewWordActivity::class.java)
      startActivityForResult(intent, newWordActivityRequestCode)
    }

    observeChangeData()
  }

  private fun observeChangeData() {
    wordViewModel.allWords.observe(this,  { words ->
      // Update the cached copy of the words in the adapter.
      words?.let { adapter.submitList(it) }
    })
  }


  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
      data?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let {
        val word = Word(it)
        wordViewModel.insert(word)
      }
    } else {
      Toast.makeText(
        applicationContext,
        R.string.empty_not_saved,
        Toast.LENGTH_LONG).show()
    }
  }
}