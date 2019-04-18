package supermega.app.listenupmahimcallinye

import android.content.Context
import android.content.SharedPreferences
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import java.io.*

class WhitelistManager(context: Context?){
    private val _context: Context?
    private val _WHITELIST_FILE="whitelist"
    private var phoneUtil: PhoneNumberUtil
    init{
        _context=context
        phoneUtil= PhoneNumberUtil.createInstance(_context)
        load()
    }

    fun getSize():Int{
        val prefs=_getPrefs() ?: return -1
        return prefs.getInt(_context?.getString(R.string.pref_whitelist_count),-1)
    }
    private fun setSize(newSize:Int){
        val prefs=_getPrefs() ?: return
        with(prefs.edit()) {
            putInt(_context?.getString(R.string.pref_whitelist_count), newSize)
            commit()
        }
    }

    fun clearWhitelist(){
        val prefs=_getPrefs() ?: return
        with(prefs.edit()){
            clear()
            commit()
        }
    }
    fun append(number:String){
        val prefs=_getPrefs() ?: return
        var size=getSize()
        with(prefs.edit()){

        }
    }
    private fun parseNumber(num:String): Phonenumber.PhoneNumber {
        return phoneUtil.parse(num,"GB")
    }
    fun getWhitelist():Array<String>{
        val prefs=_getPrefs() ?: return arrayOf()
        val size=getSize()
        var whitelist=Array<String>(size){""}
        for(i in 0 until size){
            val key=_context?.getString(R.string.pref_whitelist_value)+i
            whitelist[i]=prefs.getString(key,"") as String
        }
        return whitelist
    }
    fun containsNumber(number:String):Boolean{
        val parsedNum=parseNumber(number)
        val whitelist=getWhitelist()
        for(num in whitelist){
            val whiteParsed=parseNumber(num)
            if(whiteParsed.countryCode==parsedNum.countryCode&&whiteParsed.nationalNumber==parsedNum.nationalNumber){ return true }
        }
        return false
    }

    private fun _getPrefs():SharedPreferences?{ return _context?.getSharedPreferences(_context?.getString(R.string.shared_preferences_whitelist),Context.MODE_PRIVATE) }
    fun load(){
        if(getSize()==-1){setSize(0)}
    }
    //very simple number comparison, no longer used since country codes exist lmao
    //fun extractDigits(s:String):String{ return s.replace("\\D+","") }
    /*
    private fun _load():ArrayList<String>{
        var list=ArrayList<String>()
        val input = _getPrefs() ?: return ArrayList()
        val whitelist_count=input.getInt(_context?.getString(R.string.saved_whitelist_count),0)
        for(i in 0 until whitelist_count ){
            val key=_context?.getString(R.string.saved_whitelist_key)+i
            if(input.contains(key)){
                list.add(input.getString(key,""))
            }
        }
        return list
    }
    */

    /*
    fun save(){
        val prefs=_getPrefs()
        if(prefs!=null){
            with(prefs.edit()){
                clear()
                putInt(_context?.getString(R.string.saved_whitelist_count),whitelist.size)
                for(i in 0 until whitelist.size){
                    putString(_context?.getString(R.string.saved_whitelist_key)+i,whitelist[i])
                }
                commit()
            }
        }
    }
    */

}