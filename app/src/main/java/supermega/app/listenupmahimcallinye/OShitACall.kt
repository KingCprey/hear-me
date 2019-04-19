package supermega.app.listenupmahimcallinye

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.telephony.PhoneNumberUtils
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

class OShitACall : BroadcastReceiver{
    private val TAG="OSHITACALL"

    constructor()
    override fun onReceive(context: Context?, intent: Intent?) {
        //Log.d(TAG,"Received broadcast")
        val tmgr=context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val listener=PListener(context)
        tmgr.listen(listener,PhoneStateListener.LISTEN_CALL_STATE)
    }
}

class PListener(context: Context?) : PhoneStateListener() {
    private val TAG="OSHITACALL PLISTENER"
    private var pcontext: Context?
    private var _sound:SoundManager
    private var _whitelist:WhitelistManager
    init{
        pcontext=context
        _sound=SoundManager(pcontext)
        _whitelist=WhitelistManager(pcontext)
    }

    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        Log.d(TAG,"$state incoming number: $phoneNumber")
        when(state){
            TelephonyManager.CALL_STATE_IDLE,TelephonyManager.CALL_STATE_OFFHOOK->{
                //Call has ended?
                _sound.reset()
            }
            TelephonyManager.CALL_STATE_RINGING->{
                if(phoneNumber!=null){
                    val whitelist=WhitelistManager(pcontext)
                    if(whitelist.containsNumber(phoneNumber)){
                        _sound.start()
                    }
                }
            }
        }
    }
}