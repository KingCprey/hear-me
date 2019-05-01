package supermega.app.listenupmahimcallinye

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.SharedPreferences
import android.media.*
import android.net.Uri
import android.net.rtp.AudioStream
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

class SoundManager(context: Context){
    private val context=context
    companion object{
        fun getDefaultRingtone(): Uri { return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE) }
        fun getRingerMode(context: Context):Int{
            val manager=context.getSystemService(Service.AUDIO_SERVICE)as AudioManager
            return manager.ringerMode
        }
    }
}
/*
class SoundManager(context: Context){
    constructor(context: Context,player: MediaPlayer):this(context){
        _player=player
        _player.isLooping=true

    }

    private val _context: Context
    private val _manager: AudioManager
    private var notificationManager: NotificationManager
    private val TAG="OSHITACALL SoundManager"
    private var _prefs:SharedPreferences

    private lateinit var _player: MediaPlayer

    private val STREAM=AudioManager.STREAM_RING

    init{
        _context=context
        _manager=_context?.getSystemService(Service.AUDIO_SERVICE) as AudioManager
        _prefs=_getPrefs()
        notificationManager=_context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //when Ringtone audio attributes were introduced
        prepare()

    }

    //gimme some static methods boah


    private fun _keyVolume():String{return _context!!.getString(R.string.pref_ring_volume)}
    private fun _getPrefs():SharedPreferences{ return _context!!.getSharedPreferences(_context?.getString(R.string.shared_preferences_sound),Context.MODE_PRIVATE) }

    fun start(){
        _loadVolume()
        when(_manager.ringerMode){
            AudioManager.RINGER_MODE_SILENT,AudioManager.RINGER_MODE_VIBRATE->{
                _manager.ringerMode=AudioManager.RINGER_MODE_NORMAL
                _manager.setStreamVolume(STREAM,_manager.getStreamMaxVolume(STREAM),0)
            }
        }
        RingPlayer.player.start()
    }
    fun prepare(){
        try{
            _loadVolume()
            RingPlayer.player.setDataSource(_context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
            if(android.os.Build.VERSION.SDK_INT>=21){
                RingPlayer.player.setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build())
            }else{
                RingPlayer.player.setAudioStreamType(AudioManager.STREAM_RING)
            }
            RingPlayer.player.isLooping = true
            RingPlayer.player.setVolume(volume/100.0f, volume/100.0f)
            RingPlayer.player.prepare()
        }catch(i: Exception){}
    }

    fun reset(){
        pause()
        RingPlayer.player.seekTo(0)
    }

    private fun _getVolume():Float{ return _prefs.getFloat(_keyVolume(),DEFAULT_VOLUME) }
    private fun _loadVolume(){ volume=_getVolume() }
    private fun _saveVolume(){
        with(_prefs.edit()){
            putFloat(_keyVolume(),volume)
            commit()
        }
    }

    fun setVolume(newVolume:Float){ setVolume(newVolume,true) }
    fun setVolume(newVolume:Float,save:Boolean){
        _player.setVolume(newVolume/100.0f,newVolume/100.0f)
        if(save){  _saveVolume() }
    }

    fun getVolume():Float{ return volume }

    fun saveVolume(){

    }

    /*
    fun getRingMax():Float{ return _manager.getStreamMaxVolume(AudioManager.STREAM_RING) as Float}
    fun loud(){
    /*if(hasNotificationPermission()){

            if(getRingerMode()!=AudioManager.RINGER_MODE_NORMAL){
                //if(getRingVolume()<=0){ setRingHalf() }
                setRingerMode(AudioManager.RINGER_MODE_NORMAL)

                /*if(!_ringtone.isPlaying) {
                    Log.d(TAG,"Playing Ringtone now")

                    _ringtone.play()
                }
                */
            }
        }*/
      }

    fun save(){
        val prefs=_getPrefs() ?: return
        with(prefs.edit()){
            clear()
            putInt(_context?.getString(R.string.saved_before_ringer_mode),getRingerMode())
            commit()
        }
    }
    fun clear(){
        val prefs=_getPrefs() ?:return
        with(prefs.edit()){
            clear()
            commit()
        }
    }
    fun revert(){
        stopRingtone()
        val before=getBeforeSetting()
        if(before>=0){
            setRingerMode(before)
            clear()
        }
    }
    fun getBeforeSetting(defaultValue:Int):Int{
        return _getPrefs()?.getInt(_context?.getString(R.string.saved_before_ringer_mode),defaultValue) ?: return defaultValue
    }
    fun getBeforeSetting():Int{return getBeforeSetting(-1)}
    fun hasBeforeSetting():Boolean{
        return getBeforeSetting()>=0
    }
    fun hasNotificationPermission():Boolean{ if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){return notificationManager.isNotificationPolicyAccessGranted}else{return true}}
    fun getRingerMode():Int{return _manager.ringerMode}
    fun setRingerMode(ringerMode:Int):Boolean{
        if(hasNotificationPermission()) {
            _manager.ringerMode = ringerMode
            return true
        }else{
            Toast.makeText(_context,"Sound mode changer has not been given Do Not Disturb permissions",Toast.LENGTH_SHORT).show()
        }
        return false
    }
    fun isSilent():Boolean{return getRingerMode()==AudioManager.RINGER_MODE_SILENT}
    fun isLoud():Boolean{return getRingerMode()==AudioManager.RINGER_MODE_NORMAL}
    fun isVibrate():Boolean{return getRingerMode()==AudioManager.RINGER_MODE_VIBRATE}
    fun getRingVolume():Int{
        return _manager.getStreamVolume(AudioManager.STREAM_RING)
    }
    fun getRingMax():Int{
        return _manager.getStreamMaxVolume(AudioManager.STREAM_RING)
    }
    fun setRingHalf(){
        _manager.setStreamVolume(AudioManager.STREAM_RING,getRingMax()/2,0)
    }
    private fun _getPrefs():SharedPreferences?{ return _context?.getSharedPreferences(_context?.getString(R.string.preference_before_settings),Context.MODE_PRIVATE) }
    fun stopRingtone(){
        //if(_ringtone.isPlaying){_ringtone.stop()}
        if(RingPlayer.player.isPlaying) { RingPlayer.player.stop() }
    }
    */
}
 */
