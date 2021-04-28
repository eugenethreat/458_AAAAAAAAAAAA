package com.example.firsttest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.firsttest.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var notifId: Int = 0

    //for notification ids == the array of notifications is ordered by ids, for some reason
    lateinit var changeTextButton: Button
    lateinit var bottomButton: Button
    lateinit var titleButton: EditText
    lateinit var notifButton: EditText //sends notifications
    lateinit var notifListener: Button

    var listeningForNotifs: Boolean = false
    lateinit var notificationManager: NotificationManager
    var notificationCount: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        super.onCreate(savedInstanceState)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setting binding, setup, etc. don't touch!

        changeTextButton = binding.changeText
        bottomButton = binding.button
        titleButton = binding.titleText
        notifButton = binding.bodyText
        notifListener = binding.notifListener


        //notification shit ----------------------------------------------
        //notification channel

        // Create the NotificationChannel
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val CHANNEL_ID = "1"
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        notificationManager.createNotificationChannel(mChannel)

        changeTextButton.setOnClickListener {

            //get notif text from text fields
            var newTitle = titleButton.text.toString()
            var newBody = notifButton.text.toString()

            //notification builder
            var builder = NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(newTitle)
                .setContentText(newBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            //sending the notification
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notifId = notifId + 1
                //can't be random -- affects order of the array of notifications, they are ordered by id.
                notify(notifId, builder.build())
            }

            //CODE THAT WAS PREVIOUSLY LISTENING FOR NOTIFICATIONS AND UPDATING THE BIG BUTTON

//            var title: String? =
//                notificationManager.activeNotifications.lastOrNull()?.notification?.extras?.getString(
//                    Notification.EXTRA_TITLE
//                ).toString()
//            var contents: String? =
//                notificationManager.activeNotifications.lastOrNull()?.notification?.extras?.getString(
//                    Notification.EXTRA_TEXT
//                ).toString()
//            //http://gmariotti.blogspot.com/2013/11/notificationlistenerservice-and-kitkat.html
//            //https://developer.android.com/reference/android/app/Notification.html#EXTRA_TEXT
//            //needs to be Notification.EXTRA_TITLE static val
//
//            //this is the code that will be listening for notifications -- notificationManager and
//            //getSystemService(NOTIFICATION_SERVICE) as NotificationManager are the most important parts!
//
//            if (title == "" || title === null) {
//                //if there is no notification title...
//                bottomButton.setText("nothing sent! ")
//            } else {
//                bottomButton.setText(title + "\n" + contents)
//                bottomButton.setText(tryToPing())
//                //tryToPing()
//                Log.v("CatalogClient", "test")
//            }
        } //end of listener

        //here it is
        //button for whether you are listening to notifications or not
        notifListener.setOnClickListener {
            if (listeningForNotifs == false) {
                listeningForNotifs = true
                //notifListener.setBackgroundColor(101010)
                notifListener.setText("now listening!")
                //bottomButton.setText(listeningForNotifs.toString())

                //https://stackoverflow.com/questions/7478941/implementing-a-while-loop-in-android
                //runnable to create its own thread and not lock up UI
                var notifListener: Runnable = Runnable {
                    while (listeningForNotifs) {
                        var prevTitle: String? = ""
                        var prevContents: String? = ""

                        var prevText: String? = bottomButton.text.toString()
                        //Log.v("null1", prevText.toString())

                        if (prevText == "this should change when a notification comes!") {
                            //Log.v("null2", "blackmidi")
                            //should do nothing -- makes sure app doesn't crash on first notif
                        } else {
                            if (prevText != null) {
                                prevTitle = prevText.split("\n")[0]
                            }
                            if (prevText != null) {
                                prevContents = prevText.split("\n")[1]
                            }

                        }

                        //Log.v("listening-notifs-test", "TESTING IF WHILE LOOPS WORK")
                        var title: String? =
                            notificationManager.activeNotifications.lastOrNull()?.notification?.extras?.getString(
                                Notification.EXTRA_TITLE
                            ).toString()
                        var contents: String? =
                            notificationManager.activeNotifications.lastOrNull()?.notification?.extras?.getString(
                                Notification.EXTRA_TEXT
                            ).toString()

                        if (title == "" || title === null) {
                            //if there is no notification title...
                            bottomButton.setText("nothing sent! ")
                        } else if (prevTitle != title && prevContents != contents) {
                            //check if the notification is different from the previous to prevent it from sending 1 billion pings (ow)
                            bottomButton.setText(title + "\n" + contents)
                            notificationCount++
                            Log.v("notificationAmt", notificationCount.toString())
                            //bottomButton.setText(tryToPing())
                            pingServer()

                        } else {
                            //should do nothing
                        }
                    }
                }

                var myThread: Thread = Thread(notifListener)
                myThread.start()


            } else {
                listeningForNotifs = false;
                notifListener.setText("not listening!")
                // bottomButton.setText(listeningForNotifs.toString())

            }

        }


    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onResume() {
//        super.onResume()
////
////        var counter = 1;
////        while (counter < 10) {
////            Log.v("loop-test", "TESTING IF WHILE LOOPS WORK")
////            counter++
////        }
//
////        //for some reason you have to leave and reenter to get this to work
////        var notifListener: Runnable = Runnable {
////            while (listeningForNotifs == true) {
////                Log.v("listening-notifs-test", "TESTING IF WHILE LOOPS WORK")
////            }
////        }
////
////        notifListener.run()
//
//
//        //now, wait for notifications
//        while (listeningForNotifs == true) {
//
//            var title: String? =
//                notificationManager.activeNotifications.lastOrNull()?.notification?.extras?.getString(
//                    Notification.EXTRA_TITLE
//                ).toString()
//            var contents: String? =
//                notificationManager.activeNotifications.lastOrNull()?.notification?.extras?.getString(
//                    Notification.EXTRA_TEXT
//                ).toString()
//
//            if (title == "" || title === null) {
//                //if there is no notification title...
//                bottomButton.setText("nothing sent! ")
//            } else {
//                bottomButton.setText(title + "\n" + contents)
//                //bottomButton.setText(tryToPing())
//                //tryToPing()
//                Log.v("CatalogClient", "test")
//            }
//        }
//
//
//    }

    var toReturn: String? = null

    //for connecting to the server
    fun pingServer(): String {
        //var link = "http://www.google.com"
        var link = "http://10.0.2.2:8080"

        var url = URL(link);
        val conn = url.openConnection() as HttpURLConnection

        //writing request body or something
//        conn.doOutput = true
//        var toWrite: OutputStream = conn.outputStream
//        val b = byteArrayOf('h'.toByte(), 'e'.toByte(), 'l'.toByte(), 'l'.toByte(), 'o'.toByte())
//        toWrite.write(b, 0 ,3)


        conn.connect()

        var inStream: InputStream = conn.inputStream
        var reader: BufferedReader = BufferedReader(InputStreamReader(inStream, "UTF-8"))
        var webPage: String = ""
        var data: String = ""

        //if there is only one line in the notif...
        var firstly = reader.readLine()

        if (reader.readLine() != null) {
            while ((reader.readLine() != null)) {
                data = reader.readLine()
                webPage += data + "\n"
            }
            conn.disconnect();
            return webPage;
        } else {
            return firstly
        }

//        return toReturn.toString()

    }

}

