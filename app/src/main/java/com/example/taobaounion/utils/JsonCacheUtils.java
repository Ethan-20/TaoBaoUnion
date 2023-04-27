package com.example.taobaounion.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.taobaounion.base.BaseApplication;
import com.example.taobaounion.model.domain.CacheWithDuration;
import com.google.gson.Gson;

public class JsonCacheUtils {
    public static final String JASON_CACHE_SP_NAME = "jason_cache_sp_name";
    private static  JsonCacheUtils sJsonCacheUtil = null;
    private final SharedPreferences mSharedPreferences;
    private final Gson mGson;

    public static JsonCacheUtils getInstance() {
        if (sJsonCacheUtil == null) {
            sJsonCacheUtil = new JsonCacheUtils() ;
        }
        return sJsonCacheUtil;
    }

    private JsonCacheUtils() {
        mSharedPreferences = BaseApplication.getAppContext().getSharedPreferences(JASON_CACHE_SP_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();

    }

    //对搜索记录进行保存
    public void saveCache(String key, Object value) {
        //这duration = -1 是真没看懂
        this.saveCache(key,value,100000000);
    }

    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        //1、把value转成json（这一步是为什么？） TODO:把这里删掉试试
        String valueStr = mGson.toJson(value);
        if (duration !=-1L) {
            //声明了时间，就把截止时间算出来捏
            duration += System.currentTimeMillis();
        }
        //2、把json格式的value放进CacheWithDuration
        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration, valueStr);
        //3、把CacheWithDuration格式的value转成json
        String cacheWithTime = mGson.toJson(cacheWithDuration);
        //4、保存起来
        editor.putString(key, cacheWithTime);
        editor.apply();
    }

    public void delCache(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public <T> T getValue(String key,Class<T> clazz) {
        //1、拿到json格式的CacheWithDuration
        String valueWithDuration = mSharedPreferences.getString(key, null);
        if (valueWithDuration == null) {
            return null;
        }
        //2、转回CacheWithDuration格式
        CacheWithDuration cacheWithDuration = mGson.fromJson(valueWithDuration, CacheWithDuration.class);
        //对时间判断
        long duration = cacheWithDuration.getDuration();
        if (duration ==-1|| duration - System.currentTimeMillis()<0) {
            //过期了
            return null;
        }else{
            //没过期
            //TODO 这里没看懂
            //3、从CacheWithDuration拿到json格式的缓存数据
            String cache = cacheWithDuration.getCache();
            LogUtils.d(this,"cache----->"+cache);
            //4、把缓存数据从json转回原来的格式
            T result = mGson.fromJson(cache, clazz);
            return result;
        }
    }

}
