package com.lib

import android.app.Application
import androidx.multidex.MultiDex

/**
 *作者：create by 张金 on 2022/6/6 17:28
 *邮箱：564813746@qq.com
 */
class LibApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}