package com.wenkrang.nian_beast.entity

import com.wenkrang.nian_beast.NianBeast
import com.wenkrang.nian_beast.NianBeast.isShutdown
import com.wenkrang.nian_beast.entity.PlayerJoin.AutoSetTarget
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class BossDamage extends Listener {
  @EventHandler def OnBossDamage(event: PlayerJoinEvent): Unit = {
    new BukkitRunnable() {
      /**
       * 运行方法
       */
      override def run(): Unit = {
        // 如果已经关闭，则取消任务
        if (isShutdown) cancel()
        // 如果玩家在线
        if (event.getPlayer.isOnline) {
          // 创建一个BukkitRunnable任务
          new BukkitRunnable() {
            override def run(): Unit = {
              AutoSetTarget(40, "nian_beastthree", event.getPlayer)
            }
          }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
        }
        else {
          // 如果玩家不在线，则取消任务
          cancel()
        }
      }
    }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(classOf[NianBeast]), 0, 40)
  }
}