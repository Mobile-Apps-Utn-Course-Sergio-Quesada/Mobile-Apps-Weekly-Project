package Util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import cr.ac.utn.spots.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class Util {
    companion object {
        // Constantes para pasar datos entre Activities
        const val EXTRA_MESSAGE_POSTID = "cr.ac.utn.spots.PostId"
        const val EXTRA_MESSAGE_USERID = "cr.ac.utn.spots.UserId"
        const val EXTRA_MESSAGE_USERNAME = "cr.ac.utn.spots.Username"
        const val EXTRA_MESSAGE_FORUMID = "cr.ac.utn.spots.ForumId"
        const val EXTRA_MESSAGE_COMMENTID = "cr.ac.utn.spots.CommentId"

        fun openActivity(
            context: Context,
            objClass: Class<*>,
            extraName: String = "",
            value: String? = null
        ) {
            val intent = Intent(context, objClass).apply {
                if (extraName.isNotEmpty() && value != null) {
                    putExtra(extraName, value)
                }
            }
            context.startActivity(intent)
        }

        fun parseStringToDateModern(dateString: String, pattern: String): LocalDate? {
            return try {
                val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
                LocalDate.parse(dateString, formatter)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun parseStringToDateTimeModern(dateTimeString: String, pattern: String): LocalDateTime? {
            return try {
                val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
                LocalDateTime.parse(dateTimeString, formatter)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun parseStringToDateLegacy(dateString: String, pattern: String): Date? {
            return try {
                val formatter = SimpleDateFormat(pattern, Locale.getDefault())
                formatter.parse(dateString)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun showDialogCondition(context: Context, questionText: String, callback: () -> Unit) {
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage(questionText)
                .setCancelable(false)
                .setPositiveButton(
                    context.getString(R.string.TextYes),
                    DialogInterface.OnClickListener { dialog, id -> callback() })
                .setNegativeButton(
                    context.getString(R.string.TextNo),
                    DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
            val alert = dialogBuilder.create()
            alert.setTitle(context.getString(R.string.TextTitleDialogQuestion))
            alert.show()
        }

        fun showSimpleDialog(context: Context, title: String, message: String) {
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
            dialogBuilder.create().show()
        }

        fun isValidEmail(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isNotEmptyOrBlank(text: String): Boolean {
            return text.trim().isNotEmpty()
        }
    }
}