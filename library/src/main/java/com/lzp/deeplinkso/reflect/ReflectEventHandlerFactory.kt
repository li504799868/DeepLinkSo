package com.lzp.deeplinkso.reflect

import com.lzp.deeplinkso.handler.IDeepLinkSoEventHandler

/**
 * Created by li.zhipeng on 2018/12/11.
 *
 *      通过反射得到指定的EventHandler的工厂类
 */
object ReflectEventHandlerFactory {

    fun getEventHandlerByClass(clazz: Class<*>): IDeepLinkSoEventHandler{
        return clazz.newInstance() as IDeepLinkSoEventHandler
    }
}