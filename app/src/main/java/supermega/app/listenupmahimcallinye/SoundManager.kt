package supermega.app.listenupmahimcallinye

import android.app.Service
import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.provider.MediaStore

class SoundManager(context: Context?){
    private val _context: Context?
    private val _manager: AudioManager

    init{
        _context=context
        _manager=_context?.getSystemService(Service.AUDIO_SERVICE) as AudioManager
    }

    fun getRingerMode():Int{return _manager.ringerMode}
    fun setRingerMode(ringerMode:Int){_manager.ringerMode=ringerMode}
    fun isSilent():Boolean{return getRingerMode()==AudioManager.RINGER_MODE_SILENT}
    fun isLoud():Boolean{return getRingerMode()==AudioManager.RINGER_MODE_NORMAL}
    fun isVibrate():Boolean{return getRingerMode()==AudioManager.RINGER_MODE_VIBRATE}
    private fun _getPrefs():SharedPreferences?{ return _context?.getSharedPreferences(_context?.getString(R.string.preference_before_settings),Context.MODE_PRIVATE) }
    fun loud(){
        setRingerMode(AudioManager.RINGER_MODE_NORMAL)
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