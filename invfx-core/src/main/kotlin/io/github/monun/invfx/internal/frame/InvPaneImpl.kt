/*
 * InvFX
 * Copyright (C) 2021 Monun
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.monun.invfx.internal.frame

import io.github.monun.invfx.frame.InvFrame
import io.github.monun.invfx.frame.InvPane
import io.github.monun.invfx.util.getValue
import io.github.monun.invfx.util.lazyVal
import io.github.monun.invfx.util.setValue
import io.github.monun.invfx.util.weak
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class InvPaneImpl(
    frame: InvFrame,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : AbstractInvRegion(x, y, width, height), InvPane {
    private val frame by weak(frame)

    private var onClick: ((Int, Int, InventoryClickEvent) -> Unit)? by lazyVal()

    override fun onClick(onClick: (x: Int, y: Int, event: InventoryClickEvent) -> Unit) {
        this.onClick = onClick
    }

    private fun checkSlot(x: Int, y: Int) {
        require(x in 0 until width)
        require(y in 0 until height)
    }

    override fun item(x: Int, y: Int): ItemStack? {
        checkSlot(x, y)

        return frame.item(this.x + x, this.y + y)
    }

    override fun item(x: Int, y: Int, item: ItemStack?) {
        checkSlot(x, y)

        frame.item(this.x + x, this.y + y, item)
    }

    override fun onClick(x: Int, y: Int, event: InventoryClickEvent) {
        onClick?.runCatching { invoke(x, y, event) }
    }
}