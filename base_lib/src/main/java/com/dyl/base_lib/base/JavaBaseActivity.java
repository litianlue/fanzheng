package com.dyl.base_lib.base;

import android.content.Intent;

import com.dyl.base_lib.data.cache.Cache;
import com.dyl.base_lib.model.ReqModel;

/**
 * @Company: 杭州先手科技有限公司
 * @Created: 2018/5/8
 * 作者：wy
 * @Description:
 * @version: 1.0
 */

public abstract class JavaBaseActivity extends BaseActivity {
    public<T extends BaseActivity> void startActivity(Class<T> t){
        startActivity(t,null);
    }
    public<T extends BaseActivity,P> void startActivity(Class<T> t,P p){
        if(p!=null){
            Cache.Companion.putCache(this.getClass().getName()+"to"+t.getName()+"-data", p);
        }
        startActivity(new Intent(this, t.getClass()));
    }
    public < T extends BaseActivity, P> P getData(Class<T> t) {
       return Cache.Companion.getCache(t.getName()+"to"+this.getClass().getName()+"-data");
    }

    public < T extends BaseActivity, P> P popData(Class<T> t) {
        return Cache.Companion.popCache(t.getName()+"to"+this.getClass().getName()+"-data");
    }
}
