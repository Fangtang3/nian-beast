package com.wenkrang.nian_beast.lib.bookapi

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

import java.util
import java.util.List


object BookList {
  var thelist: util.List[AnyRef] = null

  def showBookList(player: Player, list: util.List[AnyRef]): Unit = {
    thelist = list
    val inventory = Bukkit.createInventory(null, 54, thelist.get(1).asInstanceOf[String])
    val Background = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
    val itemMeta = Background.getItemMeta
    itemMeta.setDisplayName(" ")
    Background.setItemMeta(itemMeta)
    for (i <- 0 until 8) {
      inventory.setItem(i, Background)
    }
    for (i <- 45 until 53) {
      inventory.setItem(i, Background)
    }
  }
}