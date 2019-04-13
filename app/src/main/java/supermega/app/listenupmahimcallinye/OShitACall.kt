package supermega.app.listenupmahimcallinye

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

class OShitACall : BroadcastReceiver{
    private val TAG="OSHITACALL"
    constructor()
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG,"Received broadcast")
        val tmgr=context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val listener=PListener(context)
        tmgr.listen(listener,PhoneStateListener.LISTEN_CALL_STATE)

    }
}

class PListener(context: Context?) : PhoneStateListener() {
    private val TAG="OSHITACALL PLISTENER"
    private var pcontext: Context?
    init{
        pcontext=context
    }

    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        //Log.d(TAG,"$state incoming number: $phoneNumber")
        when(state){
            TelephonyManager.CALL_STATE_IDLE->{
                //Call has ended?
                val audio=SoundManager(pcontext)
                audio.revert()
            }
            TelephonyManager.CALL_STATE_RINGING->{
                if(phoneNumber!=null){
                    val whitelist=WhitelistManager(pcontext)
                    val audio=SoundManager(pcontext)
                    if(!audio.isLoud()){
                        if (whitelist.containsNumber(phoneNumber)) {
                            //save current ringer mode
                            audio.save()
                            //make loud
                            audio.loud()
                        }
                    }
                }
            }
        }
    }
}