package supermega.app.listenupmahimcallinye

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

private const val ACTION_PLAY = "supermega.app.action.PLAY"
private const val ACTION_STOP="supermega.app.action.STOP"
private const val ACTION_CHANGE_VOLUME="supermega.app.action.VOLUME"
class MusicPlayerService : Service(), MediaPlayer.OnPreparedListener , MediaPlayer.OnErrorListener{
    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate() {
        super.onCreate()
    }
    override fun onPrepared(mediaPlayer: MediaPlayer){
        mediaPlayer.start()
    }

    private fun _init(){
        mediaPlayer=MediaPlayer.create(this,SoundManager.getDefaultRingtone())
        mediaPlayer?.apply{
            setVolume()
        }
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        try{mediaPlayer?.release()}catch (exc: Exception){}
        mediaPlayer=
    }
    override fun onBind(intent: Intent?): IBinder? {

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent!=null){
            when(intent.action){
                ACTION_PLAY->{

                }
                ACTION_STOP->{

                }
                ACTION_CHANGE_VOLUME->{

                }

            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

}