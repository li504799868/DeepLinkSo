package com.lzp.deeplinkso.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.lzp.deeplinkso.DeepLinkSoClient
import com.lzp.deeplinkso.activity.DeepLinkSoActivity
import com.lzp.deeplinkso.bean.DeepLinkSoActivityOption
import com.lzp.deeplinkso.bean.DeepLinkSoEventOption
import com.lzp.deeplinkso.bean.DeepLinkSoOption
import com.lzp.deeplinkso.bean.DeepLinkSoRequest
import com.lzp.deeplinkso.constants.DeepLinkSoConstant
import com.lzp.deeplinkso.exception.DeepLinkSoFailedException
import com.lzp.deeplinkso.interceptor.IDeepLinkSoInterceptor
import com.lzp.deeplinkso.reflect.ReflectEventHandlerFactory

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

        // 创建跳转请求
        val request = createDeepLinkSoRequest(uri, page)
        // 检查配置是否正确
        if (!checkOptionAndParams(activity, request)) {
            deepLinkFailed(activity)
            return
        }

        // 判断是否被拦截器拦截
        if (needIntercept(activity, request)) {
            deepLinkFailed(activity)
            return
        }

        // 跳转页面
        if (request.option!! is DeepLinkSoActivityOption) {
            startActivity(activity, request)
        }
        // 调用指定的EventHandler处理此次跳转请求
        else if (request.option is DeepLinkSoEventOption) {
            callEventHandler(activity, request)
        }

        activity.finish()
    }

    /**
     * 创建深度链接请求
     * */
    private fun createDeepLinkSoRequest(uri: Uri, page: String?): DeepLinkSoRequest {
        // 得到指定的配置信息
        val option = DeepLinkSoClient.config.getOption(page!!)
        // 查询此次跳转在uri中的参数
        val params = option?.let { queryParams(it, uri) }
        return DeepLinkSoRequest(uri, option, params)
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
     * 检查配置是否正确
     * */
    private fun checkOptionAndParams(activity: Activity, request: DeepLinkSoRequest): Boolean {
        // 检查是否得到了option配置
        if (request.option == null) {
            DeepLinkSoClient.config.listener?.onDeepLinkFailed(activity, request,
                    DeepLinkSoFailedException(DeepLinkSoFailedException.PAGE_NOT_REGISTER,
                            "The option of the page is not found, have you register it in DeepLinkSo.xml?"))
            return false
        }

        // 检查了uri链接中是否配置了正确的参数
        if (request.params == null) {
            DeepLinkSoClient.config.listener?.onDeepLinkFailed(activity, request,
                    DeepLinkSoFailedException(DeepLinkSoFailedException.PARAMS_NULL, "param can not be null"))
            return false
        }
        return true
    }

    /**
     * 是否要拦截此次跳转
     * */
    private fun needIntercept(activity: Activity, request: DeepLinkSoRequest): Boolean {
        // 先判断公共拦截器
        if (!request.shouldSkipCommonInterceptors() && needInterceptInner(activity, request, DeepLinkSoClient.config.interceptors)) {
            DeepLinkSoClient.config.listener?.onDeepLinkFailed(activity, request,
                    DeepLinkSoFailedException(DeepLinkSoFailedException.INTERCEPT, "failed by common interceptor"))
            return true
        }

        // 再判断私有拦截器
        if (needInterceptInner(activity, request, request.option!!.interceptors)) {
            DeepLinkSoClient.config.listener?.onDeepLinkFailed(activity, request,
                    DeepLinkSoFailedException(DeepLinkSoFailedException.INTERCEPT, "failed by private interceptor"))
            return true
        }

        return false
    }

    /**
     * 调用拦截器
     * */
    private fun needInterceptInner(activity: Activity, request: DeepLinkSoRequest, interceptors: ArrayList<IDeepLinkSoInterceptor>?): Boolean {
        if (interceptors != null && interceptors.size > 0) {
            for (interceptor in interceptors) {
                // 如果某个拦截器需要拦截，之后的拦截器也不会得到执行，页面直接销毁
                if (interceptor.intercept(activity, request)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 启动深度链接对应的页面
     * @param context 跳转的上下文
     * @param request 跳转请求
     * */
    fun startActivity(context: Context, request: DeepLinkSoRequest) {
        val option = request.option!!
        val params = request.params!!
        val intent = Intent(context, option.className)
        // 把参数放入intent
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
            DeepLinkSoClient.config.listener?.onDeepLinkSuccess(context, request)
        } catch (e: Exception) {
            DeepLinkSoClient.config.listener?.onDeepLinkFailed(context, request,
                    DeepLinkSoFailedException(
                            DeepLinkSoFailedException.UNKNOWN,
                            "startActivity failed by unknown reason"
                    ))
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
     * 启动EventHandler
     * */
    private fun callEventHandler(activity: Activity, request: DeepLinkSoRequest) {
        // 启动app
        DeepLinkSoNavigator.launchApp(activity)
        val eventHandler = ReflectEventHandlerFactory.getEventHandlerByClass(request.option!!.className!!)
        eventHandler.handleDeepLinkSoRequest(activity, request)
    }

    /**
     * 启动页面失败
     * */
    private fun deepLinkFailed(activity: Activity) {
        // 把app回到前台
        DeepLinkSoNavigator.moveMainTaskToFront(activity)
    }
}