package supermega.app.listenupmahimcallinye

import android.content.Context
import android.content.SharedPreferences
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import java.io.*

class WhitelistManager(context: Context?){
    private val _context: Context?
    private val _WHITELIST_FILE="whitelist"
    var whitelist:ArrayList<String>
    private var phoneUtil: PhoneNumberUtil

    fun getSize():Int{return whitelist.size}

    init{
        _context=context
        phoneUtil= PhoneNumberUtil.createInstance(_context)
        whitelist= ArrayList()
        load()
    }
    fun clearWhitelist(){
        whitelist.clear()
        save()
    }
    fun append(number:String){
        whitelist.add(number)
        save()
    }
    private fun parseNumber(num:String): Phonenumber.PhoneNumber {
        return phoneUtil.parse(num,"GB")
    }
    fun containsNumber(number:String):Boolean{
        val parsedNum=parseNumber(number)
        for(num in whitelist){
            val whiteParsed=parseNumber(num)
            if(whiteParsed.countryCode==parsedNum.countryCode&&whiteParsed.nationalNumber==parsedNum.nationalNumber){ return true }
        }
        return false
    }
    //fun extractDigits(s:String):String{ return s.replace("\\D+","") }
    private fun _getPrefs():SharedPreferences?{
        return _context?.getSharedPreferences(_context?.getString(R.string.preference_whitelist),Context.MODE_PRIVATE)
    }
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
    fun load(){
        whitelist=_load()
    }
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

}