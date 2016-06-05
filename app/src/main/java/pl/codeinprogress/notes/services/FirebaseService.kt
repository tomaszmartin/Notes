package pl.codeinprogress.notes.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pl.codeinprogress.notes.data.NotesContract

/**
 * Created by tomaszmartin on 05.06.2016.
 */

class FirebaseService: FirebaseMessagingService() {

    val tag = FirebaseService::class.java.simpleName

    override fun onMessageReceived(message: RemoteMessage?) {
        log(message?.notification.toString())
        log(message?.data.toString())
    }

    fun log(message: String) {
        Log.d(tag, message)
    }

}