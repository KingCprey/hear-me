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

class OShitACall : BroadcastReceiver(){
    private val TAG="OSHITACALL"

    companion object{
        private var lastState = TelephonyManager.CALL_STATE_IDLE
        fun getStateFromExtra(callStateExtra:String):Int{
            when(callStateExtra){
                TelephonyManager.EXTRA_STATE_IDLE->return TelephonyManager.CALL_STATE_IDLE
                TelephonyManager.EXTRA_STATE_OFFHOOK->return TelephonyManager.CALL_STATE_OFFHOOK
                TelephonyManager.EXTRA_STATE_RINGING->return TelephonyManager.CALL_STATE_RINGING
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent==null) return
        //checks if call being made is outgoing. If not then perform ringing checks
        if(intent.action!="android.intent.action.NEW_OUTGOING_CALL"){
            val extras=intent.extras
            val state= getStateFromExtra(extras.getString(TelephonyManager.EXTRA_STATE))
            val number=extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            onChange(context!!,state,number)
        }
    }

    fun onChange(context: Context, state: Int, number: String){
        if(state== lastState) return
        when(state){
            TelephonyManager.CALL_STATE_RINGING->{

            }
            TelephonyManager.CALL_STATE_OFFHOOK,TelephonyManager.CALL_STATE_IDLE->{

            }
        }
        lastState=state
    }

}

class PListener(context: Context?) : PhoneStateListener() {
    private val TAG="OSHITACALL PLISTENER"
    private var pcontext: Context?
    private var _sound:SoundManager
    private var _whitelist:WhitelistManager
    init{
        pcontext=context

        _sound=SoundManager(pcontext!!)
        _whitelist=WhitelistManager(pcontext)
    }

    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        Log.d(TAG,"$state incoming number: $phoneNumber")
        when(state){
            TelephonyManager.CALL_STATE_IDLE,TelephonyManager.CALL_STATE_OFFHOOK->{
                //Call has ended?
                //_sound.reset()
            }
            TelephonyManager.CALL_STATE_RINGING->{
                if(phoneNumber!=null){
                    val whitelist=WhitelistManager(pcontext)
                    if(whitelist.containsNumber(phoneNumber)){
                        //_sound.start()
                    }
                }
            }
        }
    }
}