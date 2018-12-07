package com.lzp.deeplinkso.exception

/**
 * Created by li.zhipeng on 2018/12/6.
 *
 *      DeepLinkSo跳转失败异常
 */
class DeepLinkSoFailedException(val code: Int, message: String) : Throwable(message) {

    companion object {

        /**
         * 参数为空
         * */
        const val PARAMS_NULL = -1

        /**
         * 被拦截
         * */
        const val INTERCEPT = -2

        /**
         * 注册的类没有被找到
         * */
        const val CLASS_NOT_FOUND = -3

    }

}