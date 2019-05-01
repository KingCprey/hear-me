package supermega.app.listenupmahimcallinye

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class RingActivity : AppCompatActivity(){
    companion object{
        private val TAG="RingActivity"
    }
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var stopReceiver: BroadcastReceiver
    private val yaboah = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"Oncreate")
        setContentView(R.layout.activity_ring)
        moveTaskToBack(true)
        val volume=VolumeManager.getMediaVolume(this)
        mediaPlayer=MediaPlayer.create(this,SoundManager.getDefaultRingtone()).apply {
            setVolume(volume, volume)
            isLooping = true
            Log.d(TAG,"Triggering music")
            start()
        }
        stopReceiver = object: BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                release()
                yaboah.finish()
            }
        }
        registerReceiver(stopReceiver, IntentFilter(BROADCAST_STOP_MUSIC))
    }

    private fun release(){
        if(mediaPlayer!=null){
            if(mediaPlayer!!.isPlaying){ mediaPlayer!!.stop() }
            mediaPlayer!!.release()
            mediaPlayer=null
        }
    }

    override fun onDestroy() {
        release()
        unregisterReceiver(stopReceiver)
        super.onDestroy()
    }
}
