package com.wenkrang.nian_beast.entity

import com.wenkrang.nian_beast.NianBeast
import com.wenkrang.nian_beast.entity.PlayerJoin.findAirBlock
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

import java.util.Random

class SpawnerTwo extends Listener {
  @EventHandler def SpawnTwo(event: PlayerJoinEvent): Unit = {
    new BukkitRunnable() {
      /**
       * 重写的run方法
       */
      override def run(): Unit = {
        // 如果已经关闭，则取消任务
        if (NianBeast.isShutdown) cancel()
        new BukkitRunnable() {
          override def run(): Unit = {
            // 如果玩家在线
            if (event.getPlayer.isOnline) {
              // 如果玩家所在的维度是"end"
              if (event.getPlayer.getWorld.getName.equalsIgnoreCase("world_the_end")) {
                // 如果玩家眼睛所处的方块高度小于320
                if (event.getPlayer.getEyeLocation.getBlockY < 320) {
                  val random = new Random
                  // 随机数小于30时
                  if (random.nextInt(100) < 30) {
                    // 在玩家眼睛所处的方块周围寻找空气方块
                    for (i <- 0 until 3) {
                      val entitytwo = NianBeastEntity.getEntitytwo(findAirBlock(event.getPlayer.getEyeLocation))
                      entitytwo.setTarget(event.getPlayer)
                    }
                  }
                }
              }
            }
            else {
              // 如果玩家不在线，取消任务
              cancel()
            }
          }
        }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
      }
    }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(classOf[NianBeast]), 0, 12000)
  }
}