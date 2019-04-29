package supermega.app.listenupmahimcallinye

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

private const val ACTION_PLAY = "supermega.app.action.PLAY"
private const val ACTION_STOP="supermega.app.action.STOP"
private const val ACTION_CHANGE_VOLUME="supermega.app.action.VOLUME"
class MusicPlayerService : Service() , MediaPlayer.OnErrorListener{
    inner class MusicBinder: Binder(){
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }
    override fun onBind(intent: Intent?): IBinder? { return binder }
    private val binder=MusicBinder()

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        _init()
    }

    private fun _init(start_on_init:Boolean){
        mediaPlayer=MediaPlayer.create(this,SoundManager.getDefaultRingtone())
        val con=this
        mediaPlayer?.apply{
            val vol=VolumeManager.getMediaVolume(con)
            //setOnPreparedListener(con)
            isLooping=true
            setOnErrorListener(con)
            setVolume(vol,vol)
            if(start_on_init){start()}
        }

    }
    private fun _init(){_init(false)}

    fun start(){
        if(mediaPlayer!=null){
            mediaPlayer?.start()
        }else{
            _init(true)
        }
    }

    fun setVolume(newVolume:Float){
        mediaPlayer?.setVolume(newVolume/100.0f,newVolume/100.0f)
    }

    fun reset(){
        mediaPlayer?.pause()
        mediaPlayer?.seekTo(0)
    }

    override fun onDestroy() {
        try{mediaPlayer?.release()}catch(exc:Exception){}
        super.onDestroy()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        try{mediaPlayer?.release()}catch (exc: Exception){}
        _init()
        return false
    }

}