/*
 * Copyright 2020-2022 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package com.github.dr.rwserver.net.http

import com.github.dr.rwserver.data.json.Json

/**
 * @author Dr
 */
abstract class WebPost {
    abstract fun get(getUrl: String, urlData: String,data: String, send: SendWeb)

    protected fun stringResolveToJson(data: String) : Json {
        return if (data.contains("&")) {
            val paramArray: Array<String> = data.split("&".toRegex()).toTypedArray()
            val listMap = LinkedHashMap<String, String>()
            for (pam in paramArray) {
                val keyValue = pam.split("=".toRegex()).toTypedArray()
                listMap[keyValue[0]] = keyValue[1]
            }
            Json(listMap)
        } else {
            Json(data)
        }
    }
}