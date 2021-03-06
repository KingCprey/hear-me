package supermega.app.listenupmahimcallinye

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.content.*
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import android.os.Build
import android.os.IBinder
import android.provider.ContactsContract
import android.text.InputType
import android.util.Log
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private val REQUEST_PHONE = 1
    private val REQUEST_CONTACTS = 2
    private val REQUEST_PICK_CONTACT = 3
    private val REQUEST_NOTIFICATION_SETTINGS = 5
    private lateinit var layout_has_permission: ConstraintLayout
    private lateinit var layout_no_permission: ConstraintLayout
    private lateinit var scroll_whitelisted_numbers: ScrollView
    private lateinit var button_add_contact: Button
    private lateinit var button_add_number: Button
    private lateinit var button_request_phone: Button
    private lateinit var button_clear_whitelist: ImageButton
    private lateinit var whitelist: WhitelistManager
    private lateinit var seek_volume: SeekBar
    private var number_string = "";

    private val TAG = "OSHITACALL MainActivity"
    private lateinit var notificationManager: NotificationManager
    private lateinit var volumeManager: VolumeManager
    private lateinit var mediaPlayer: MediaPlayer
    private var mediaPrepared=false
    private var playOnPrepared=false

    /*
    private lateinit var musicService: MusicPlayerService
    private var musicBound: Boolean = false

    private val musicConnection = object: ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder=service as MusicPlayerService.MusicBinder
            musicService=binder.getService()
            musicBound=true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicBound=false
        }
    }
    */

    private fun initViews() {
        layout_has_permission = findViewById(R.id.has_permission_layout)
        layout_no_permission = findViewById(R.id.no_permission_layout)
        scroll_whitelisted_numbers = findViewById(R.id.scroll_whitelisted_numbers)
        button_add_contact = findViewById(R.id.button_add_contact)
        button_add_number = findViewById(R.id.button_add_number)
        button_request_phone = findViewById(R.id.button_request_phone)
        button_clear_whitelist = findViewById(R.id.button_clear_whitelist)
        seek_volume = findViewById(R.id.seekVolume)
        seek_volume.progress=volumeManager.getVolume().toInt()
        seek_volume.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                volumeManager.setVolume(progress.toFloat())
                mediaPlayer.setVolume(volumeManager.getMediaVolume(),volumeManager.getMediaVolume())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                if(!mediaPrepared){ playOnPrepared=true }
                else{
                    mediaPlayer.start()
                }
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) { mediaPlayer.pause(); mediaPlayer.seekTo(0)}
        })
    }

    private fun hasPhonePermission(): Boolean { return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED }
    private fun requestNotificationPermission() {
        startActivityForResult(Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS), REQUEST_NOTIFICATION_SETTINGS)
    }

    private fun hasContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPhonePermission() { ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), REQUEST_PHONE) }
    private fun requestContactsPermission() { ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CONTACTS) }

    private fun chooseContact() {
        val yeet = Intent(Intent.ACTION_GET_CONTENT)
        yeet.type = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        startActivityForResult(yeet, REQUEST_PICK_CONTACT)
    }

    private fun updateUI() {
        if (!hasPhonePermission()) {
            show_no_permission()
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                Toast.makeText(this, "Requires Phone permission to check incoming numbers", Toast.LENGTH_LONG)
            } else { requestPhonePermission() }
        } else {
            /*if(!hasNotificationPermission()){
                val appName=getString(R.string.app_name)
                Toast.makeText(this,"Give the app \"$appName\" Do Not Disturb permissions to allow sound changes",Toast.LENGTH_LONG).show()
                requestNotificationPermission()
            }else {
                show_has_permission()
            }*/
            show_has_permission()
        }
    }

    private fun hasNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return notificationManager.isNotificationPolicyAccessGranted
        } else {
            return true
        }
    }

    private fun initMediaPlayer(){
        mediaPlayer=MediaPlayer()
        val volume=VolumeManager.getMediaVolume(this)
        val cont=this
        mediaPlayer.apply {
            setVolume(volume,volume)
            setOnPreparedListener {
                mediaPrepared=true
                if(playOnPrepared){ start() }
            }
            isLooping=true
            setDataSource(cont,SoundManager.getDefaultRingtone())
            prepareAsync()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "Received activity result")
        when (requestCode) {
            REQUEST_NOTIFICATION_SETTINGS -> {
                updateUI()
            }
            REQUEST_PICK_CONTACT -> {
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "Contacts result OK")
                    if (data != null) {
                        Log.d(TAG, "Contacts data not null")
                        val contactData = data.data
                        Log.d(TAG, contactData.toString())
                        val cursor = contentResolver.query(contactData, null, null, null, null)
                        if (cursor.moveToFirst()) {
                            Log.d(TAG, "Queried first contact")
                            val phoneColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            if (phoneColumn > -1) {
                                var number = cursor.getString(phoneColumn)
                                if (whitelist.containsNumber(number)) {
                                    Toast.makeText(this, "Whitelist already contains number", Toast.LENGTH_SHORT).show()
                                } else {
                                    whitelist.append(number)
                                    update_whitelisted_numbers()
                                }
                            } else { Toast.makeText(this, "Chosen contact does not have a phone number", Toast.LENGTH_SHORT).show() }
                        } else { Toast.makeText(this, "Failed to read contacts data", Toast.LENGTH_SHORT).show() }
                    }
                } else { Toast.makeText(this, "Contact not chosen, cancelling add", Toast.LENGTH_SHORT).show() }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //soundManager= SoundManager(this)
        volumeManager=VolumeManager(this)
        whitelist = WhitelistManager(this)
        initMediaPlayer()
        initViews()
        //requestNotificationPermission()
        button_add_contact.setOnClickListener {
            if (hasContactsPermission()) {
                chooseContact()
            } else {
                requestContactsPermission()
            }
        }
        button_add_number.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("")
            val input = EditText(this)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.inputType = InputType.TYPE_CLASS_PHONE
            builder.setView(input)

// Set up the buttons
            builder.setPositiveButton("OK") { _, _ ->
                number_string = input.text.toString()
                if (whitelist.containsNumber(number_string)) {
                    Toast.makeText(this, "Number already added", Toast.LENGTH_SHORT).show()
                } else {
                    whitelist.append(number_string)
                    //whitelist.save()
                    update_whitelisted_numbers()
                }
            }
            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            builder.show()
        }
        button_request_phone.setOnClickListener {
            requestPhonePermission()
        }
        button_clear_whitelist.setOnClickListener {
            whitelist.clearWhitelist()
            update_whitelisted_numbers()
        }
    }


    fun show_no_permission() {
        layout_has_permission.visibility = View.GONE
        layout_no_permission.visibility = View.VISIBLE
    }

    fun show_has_permission() {
        layout_has_permission.visibility = View.VISIBLE
        layout_no_permission.visibility = View.GONE
        update_whitelisted_numbers()
    }

    private fun update_whitelisted_numbers() {
        //whitelist.getWhitelist()
        whitelist.load()
        button_clear_whitelist.visibility = if (whitelist.getSize() > 0) View.VISIBLE else View.INVISIBLE
        scroll_whitelisted_numbers.removeAllViews()
        val linlayout = LinearLayout(this)
        linlayout.orientation = LinearLayout.VERTICAL
        for (num in whitelist.getWhitelist()) {
            val tview = TextView(this)
            tview.text = num
            linlayout.addView(tview)
        }
        scroll_whitelisted_numbers.addView(linlayout)
    }

    override fun onStart(){
        //Intent(this,MusicPlayerService::class.java).also { intent->bindService(intent,musicConnection,Context.BIND_AUTO_CREATE) }
        super.onStart()
    }

    override fun onDestroy() {
        //unbindService(musicConnection)
        mediaPlayer.release()
        super.onDestroy()
    }


    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PHONE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    show_has_permission()
                } else {
                    show_no_permission()
                    Toast.makeText(this, "Permission denied: Gib me the goddamb permission boi", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            REQUEST_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseContact()
                } else {
                    Toast.makeText(this, "Contacts permission denied, cannot choose contact", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
