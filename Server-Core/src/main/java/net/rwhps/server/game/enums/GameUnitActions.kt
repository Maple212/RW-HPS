/*
 * Copyright 2020-2024 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package net.rwhps.server.game.enums

import net.rwhps.server.util.EnumUtils

/**
 *
 *
 * @date 2024/1/28 10:54
 * @author Dr (dr@der.kim)
 */
enum class GameUnitActions {
    outOfRange,
    onlyInRange,
    returnFire,
    holdFire,
    guardArea,
    aggressive,
    mixed,
    UNKNOWN;

    companion object {
        fun from(name: String): GameUnitActions? = EnumUtils.from(entries, name)

        fun from(index: Int): GameUnitActions = EnumUtils.from(entries, index, UNKNOWN)
    }
}