package com.example.guessphraseapp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var title : TextView
    private lateinit var RVguess: RecyclerView
    lateinit var edPhrase: EditText
    lateinit var Check: Button
    lateinit var Guesses: ArrayList<String>
    private var numOfguess = 10
    private var secretPhrase = "Simplicity is half the beauty"
    private var phrase = charArrayOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = findViewById(R.id.tv1)
        edPhrase = findViewById(R.id.edt)
        Check = findViewById(R.id.btn)


        Guesses = arrayListOf()
        RVguess = findViewById(R.id.rvMain)
        RVguess.adapter = guessAdapter(Guesses)
        RVguess.layoutManager = LinearLayoutManager(this)

        phrase = "*".repeat(secretPhrase.length).toCharArray()


        val thePhrase = "The secret phrase is: ${String(phrase)}"
        title.text = thePhrase

        Check.setOnClickListener {
            if (numOfguess != 0)
            {
                if (edPhrase.hint == "Enter A Full Phrase")
                    checkPhrase()
                else
                    checkLetter()
            }
            else
                alertbox("Lost")
        }
    }

    fun checkPhrase() {
        if (edPhrase.text.length != 1) {
            if (edt.text.toString().equals(secretPhrase)) {
                disableInputs()
                alertbox("Won")
            } else {
                Guesses.add("Wrong Guess")
                edPhrase.hint = "Enter A Letter"

            }
            edt.text.clear()
            edt.clearFocus()
            RVguess.adapter?.notifyDataSetChanged()
        } else {
            Snackbar.make(clMain, "Error TextField is too short", Snackbar.LENGTH_SHORT).show()
        }
    }

    fun checkLetter() {
        val userLetter = edPhrase.text[0]
        val count = secretPhrase.fold(0) {
                sum: Int, c: Char ->
            if (edPhrase.text.toString().contains(c))
                sum + 1
            else
                sum
        }
        if (userLetter in secretPhrase) {
            Guesses.add("Found $count $userLetter")
            for (i in secretPhrase.indices) {
                if (secretPhrase[i] == userLetter)
                    phrase[i] = userLetter
            }
            val thePhrase = "Secret Phrase is: ${String(phrase)}\n Guessed Letter: $userLetter"
            title.text = thePhrase
            if(secretPhrase == String(phrase))
                alertbox("Won")
        } else {
            Guesses.add("wrong guess, No $userLetter")
            Guesses.add("Number of guess ${--numOfguess}")
        }
        edPhrase.text.clear()
        edt.clearFocus()
        RVguess.adapter?.notifyDataSetChanged()
        RVguess.scrollToPosition(Guesses.size-1)
        edPhrase.hint = "Enter A Full Phrase"
    }

    private fun disableInputs() {
        Check.isEnabled = false
        Check.isClickable = false
        edPhrase.isClickable = false
        edPhrase.isEnabled = false
    }

    private fun alertbox(title: String) {
        // first we create a variable to hold an AlertDialog builder
        val builder = AlertDialog.Builder(this)
        // here we set the message of our alert dialog
        builder.setMessage("You $title\n Phrase is: $secretPhrase \nNew Game?")
            // positive button text and action
            .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                this.recreate()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        // create dialog box
        val alert = builder.create()
        // set title for alert dialog box
        alert.setTitle("End of Game")
        // show alert dialog
        alert.show()
    }
}
