package com.lzp.deeplinkso.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.lzp.deeplinkso.DeepLinkSoClient
import com.lzp.deeplinkso.activity.DeepLinkSoActivity
import com.lzp.deeplinkso.bean.DeepLinkSoOption
import com.lzp.deeplinkso.constants.DeepLinkSoConstant
import com.lzp.deeplinkso.exception.DeepLinkSoFailedException

/**
 * Created by li.zhipeng on 2018/12/6.
 *
 *      DeepLink跳转器
 */
object DeepLinkSoNavigator {

    /**
     * 通过中转的页面，跳转到指定的页面
     * */
    fun startActivity(activity: Activity) {
        // 首先判断是否是从深度链接跳转过来的
        val uri = activity.intent.data ?: return
        val page = uri.getQueryParameter("page")
        // 启动app
        if (Utils.isEmpty(page)) {
            DeepLinkSoNavigator.launchApp(activity)
            return
        }

        // 得到指定的配置信息
        val option = DeepLinkSoClient.config.getOption(page!!)
        // 如果没有找到相关配置
        if (option == null) {
            DeepLinkSoClient.config.listener?.onDeepLinkFailed(activity,
                    DeepLinkSoFailedException(DeepLinkSoFailedException.PAGE_NOT_REGISTER,
                            "The option of the page is not found, have you register it in DeepLinkSo.xml?"),
                    option)
            deepLinkFailed(activity)
            return
        }

        val params = queryParams(option, uri)

        if (params == null) {
            DeepLinkSoClient.config.listener?.onDeepLinkFailed(activity,
                    DeepLinkSoFailedException(DeepLinkSoFailedException.PARAMS_NULL, "param can not be null"),
                    option)
            deepLinkFailed(activity)
            return
        }

        // 判断是否拦截此次跳转
        if (needIntercept(activity, option, params)) {
            DeepLinkSoClient.config.listener?.onDeepLinkFailed(activity,
                    DeepLinkSoFailedException(DeepLinkSoFailedException.INTERCEPT, "failed by intercept"),
                    option)
            deepLinkFailed(activity)
            return
        }

        DeepLinkSoNavigator.startActivity(activity, option, params)
        activity.finish()

    }

    /**
     * 启动深度链接对应的页面
     * @param context 跳转的上下文
     * @param option 深度链接的配置信息
     * @param params 此次跳转的参数集合
     * */
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
        try {
            context.startActivity(intent)
            DeepLinkSoClient.config.listener?.onDeepLinkSuccess(context, option, params)
        } catch (e: Exception) {
            DeepLinkSoClient.config.listener?.onDeepLinkFailed(
                    context,
                    DeepLinkSoFailedException(
                            DeepLinkSoFailedException.UNKNOWN,
                            "startActivity failed by unknown reason"
                    ),
                    option)
        }
    }

    /**
     * 启动app
     * */
    private fun launchApp(context: Context) {
        // 如果没有找到任务栈，启动app
        if (!moveMainTaskToFront(context)) {
            context.startActivity(context.packageManager.getLaunchIntentForPackage(context.packageName))
            DeepLinkSoClient.config.listener?.onLaunch(context)
        }
    }

    /**
     * 把app移动到前台
     * */
    @Suppress("DEPRECATION")
    private fun moveMainTaskToFront(context: Context): Boolean {
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

    /**
     * 查询此次跳转的参数
     * */
    private fun queryParams(option: DeepLinkSoOption, uri: Uri): HashMap<String, Any>? {
        val params = HashMap<String, Any>()
        if (option.params != null && option.params!!.size > 0) {
            for (param in option.params!!) {
                val value = uri.getQueryParameter(param.key)
                // 目前不允许参数为空
                if (Utils.isEmpty(value)) {
                    // 返回null
                    return null
                }
                // 把参数加入到集合中
                if (param.type == DeepLinkSoConstant.INT) {
                    params[param.key] = value!!.toInt()
                } else if (param.type == DeepLinkSoConstant.LONG) {
                    params[param.key] = value!!.toLong()
                } else if (param.type == DeepLinkSoConstant.FLOAT) {
                    params[param.key] = value!!.toFloat()
                } else if (param.type == DeepLinkSoConstant.DOUBLE) {
                    params[param.key] = value!!.toDouble()
                }
                // 默认是字符串
                else {
                    params[param.key] = value!!
                }
            }
        }
        return params
    }

    /**
     * 是否要拦截此次跳转
     * */
    private fun needIntercept(activity: Activity, option: DeepLinkSoOption, params: HashMap<String, Any>): Boolean {
        // 开始调用拦截器
        val interceptors = DeepLinkSoClient.config.interceptors
        if (interceptors != null && interceptors.size > 0) {
            for (interceptor in interceptors) {
                // 如果某个拦截器需要拦截，之后的拦截器也不会得到执行，页面直接销毁
                if (interceptor.intercept(activity, option, params)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 启动页面失败
     * */
    private fun deepLinkFailed(activity: Activity) {
        // 把app回到前台
        DeepLinkSoNavigator.moveMainTaskToFront(activity)
    }
}