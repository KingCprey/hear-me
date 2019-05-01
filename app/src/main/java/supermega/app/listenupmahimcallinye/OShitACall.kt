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

const val BROADCAST_STOP_MUSIC="supermega.app.listenupmahimcallingye.broadcast.killmusic"

class OShitACall : BroadcastReceiver(){
    private val TAG="OSHITACALL"

    companion object{
        private var lastState = TelephonyManager.CALL_STATE_IDLE
        private var musicTriggered = false
        fun getStateFromExtra(callStateExtra:String):Int{
            when(callStateExtra){
                TelephonyManager.EXTRA_STATE_IDLE->return TelephonyManager.CALL_STATE_IDLE
                TelephonyManager.EXTRA_STATE_OFFHOOK->return TelephonyManager.CALL_STATE_OFFHOOK
                TelephonyManager.EXTRA_STATE_RINGING->return TelephonyManager.CALL_STATE_RINGING
            }
            return -1
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

    fun sendKillBroadcast(context: Context){
        val i=Intent(BROADCAST_STOP_MUSIC)
        context.sendBroadcast(i)
    }

    fun startMusic(context: Context){
        val i =Intent(context,RingActivity::class.java)
        context.startActivity(i)
    }

    fun onChange(context: Context, state: Int, number: String){
        Log.d(TAG,"Phone state changed, new state $state")
        if(state== lastState) return
        when(state){
            TelephonyManager.CALL_STATE_RINGING->{
                if(SoundManager.getRingerMode(context)!=AudioManager.RINGER_MODE_NORMAL){
                    val whitelist=WhitelistManager(context)
                    if(whitelist.containsNumber(number)){
                        Log.d(TAG,"TRIGGERING MUSIC")
                        startMusic(context)
                        musicTriggered=true
                    }
                }
            }
            TelephonyManager.CALL_STATE_OFFHOOK,TelephonyManager.CALL_STATE_IDLE->{
                if(musicTriggered){
                    sendKillBroadcast(context)
                    musicTriggered=false
                }
            }
        }
        lastState=state
    }

}
/*
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
       */