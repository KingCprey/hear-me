package supermega.app.listenupmahimcallinye

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

class SoundManager(context: Context?){
    private val _context: Context?
    private val _manager: AudioManager
    private var notificationManager: NotificationManager
    private val TAG="OSHITACALL SoundManager"

    private lateinit var _ringtone: Ringtone

    init{
        _context=context
        _manager=_context?.getSystemService(Service.AUDIO_SERVICE) as AudioManager
        notificationManager=_context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        _ringtone=RingtoneManager.getRingtone(_context,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
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
        if(_ringtone.isPlaying){_ringtone.stop()}
    }
    fun loud(){
        if(hasNotificationPermission()){
            if(getRingerMode()!=AudioManager.RINGER_MODE_NORMAL){
                if(getRingVolume()<=0){ setRingHalf() }
                setRingerMode(AudioManager.RINGER_MODE_NORMAL)
                if(!_ringtone.isPlaying) { _ringtone.play() }
            }
        }

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

}