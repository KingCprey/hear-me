package supermega.app.listenupmahimcallinye

import android.content.Context
import android.content.SharedPreferences

class VolumeManager(context: Context){
    companion object{
        private const val DEFAULT_VOLUME=50.0f
        private fun getVolumePrefs(context: Context):SharedPreferences{ return context.getSharedPreferences(context.getString(R.string.shared_preferences_sound),Context.MODE_PRIVATE) }
        //returns volume 0-100
        fun getVolume(context: Context):Float{
            val prefs= getVolumePrefs(context)
            return prefs.getFloat(context.getString(R.string.pref_ring_volume),DEFAULT_VOLUME)
        }
        //returns volume 0-1
        fun getMediaVolume(context:Context):Float{ return getVolume(context)/100.0f }
        fun setVolume(context:Context,volume:Float){
            val prefs= getVolumePrefs(context)
            with(prefs.edit()){
                putFloat(context.getString(R.string.pref_ring_volume),
                    if(volume>100.0f){100.0f}
                    else{if(volume<0.0f){0.0f}else{volume} }
                )
                commit()
            }
        }
    }
    val context=context
    fun getVolume():Float{ return getVolume(context) }
    fun getMediaVolume():Float{return getMediaVolume(context)}
    fun setVolume(volume:Float){return setVolume(context,volume)}
}