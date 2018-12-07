package com.lzp.deeplinkso.utils

import android.content.Context
import android.content.Intent
import com.lzp.deeplinkso.DeepLinkSoClient
import com.lzp.deeplinkso.activity.DeepLinkSoActivity
import com.lzp.deeplinkso.bean.DeepLinkSoOption
import com.lzp.deeplinkso.constants.DeepLinkSoConstant

/**
 * Created by li.zhipeng on 2018/12/6.
 *
 *      DeepLink跳转器
 */
object DeepLinkSoNavigator {

    fun startActivity(context: Context, option: DeepLinkSoOption, params: HashMap<String, Any>) {
        val intent = Intent(context, option.className)
        if (option.params?.size ?: 0 > 0) {
            for (param in option.params!!) {
                if (param.type == DeepLinkSoConstant.INT) {
                    intent.putExtra(param.key, params[param.key] as Int)
                } else if (param.type == DeepLinkSoConstant.LONG) {
                    intent.putExtra(param.key, params[param.key] as Long)
                } else if (param.type == DeepLinkSoConstant.FLOAT) {
                    intent.putExtra(param.key, params[param.key] as Float)
                } else if (param.type == DeepLinkSoConstant.DOUBLE) {
                    intent.putExtra(param.key, params[param.key] as Double)
                } else {
                    intent.putExtra(param.key, params[param.key] as String)
                }
            }
        }
        context.startActivity(intent)
        DeepLinkSoClient.config.listener?.onDeepLinkSuccess(context, option, params)
    }

    fun launchApp(context: Context) {
        // 如果没有找到任务栈，启动app
        if (!moveMainTaskToFront(context)) {
            context.startActivity(context.packageManager.getLaunchIntentForPackage(context.packageName))
            DeepLinkSoClient.config.listener?.onLaunch(context)
        }
    }

    fun moveMainTaskToFront(context: Context): Boolean {
        // 是否已经启动了其他任务栈，如果有，把任务栈移动到前台
        val manager = context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val taskInfo = manager
                .getRunningTasks(20)
        for (i in taskInfo.indices) {
            // 判断包名是否相同
            if (taskInfo[i].baseActivity.packageName == context.packageName
                    // 判断是否DeepLinkSo以外的页面
                    && !taskInfo[i].baseActivity.shortClassName.contains(DeepLinkSoActivity::class.java.simpleName)) {

                manager.moveTaskToFront(taskInfo[i].id, android.app.ActivityManager.MOVE_TASK_WITH_HOME)//关键
                return true
            }
        }
        return false
    }
}