package com.wenkrang.nian_beast
package entity

import com.wenkrang.nian_beast.NianBeast
import com.wenkrang.nian_beast.NianBeast.isShutdown
import com.wenkrang.nian_beast.entity.PlayerJoin.AutoSetTarget
import org.bukkit._
import org.bukkit.entity._
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.NotNull

import java.util._
import java.util
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.util.control.Breaks.{break, breakable}

object PlayerJoin {
  /**
   * 自动设置实体的目标
   *
   * @param range    仇视范围
   * @param mobtaget 要设置目标的实体的标签
   * @param player   检查并运行方法的对象
   * @return 是否锁定目标
   */
  def AutoSetTarget(range: Int, mobtaget: String, player: Player): Boolean = {
    var IsFind = false
    // 获取玩家附近的实体列表
    @NotNull val nearbyEntities = player.getNearbyEntities(range, range, range)
    // 遍历附近的实体列表
    breakable {
      for (i <- 0 until nearbyEntities.size) {
        // 如果附近的实体的Scoreboard标签中包含mobtaget标签
        if (nearbyEntities.get(i).getScoreboardTags.contains(mobtaget)) {
          // 获取附近的实体的mob对象
          val mob = nearbyEntities.get(i).asInstanceOf[Mob]
          // 获取mob对象附近的实体列表
          @NotNull val nearbyEntities1 = mob.getNearbyEntities(range, range, range)
          // 遍历mob对象附近的实体列表
          for (j <- 0 until nearbyEntities1.size) {
            val entity = nearbyEntities1.get(j)
            // 如果实体的类型是Player
            if (entity.getType == EntityType.PLAYER) {
              // 如果实体的游戏模式是SURVIVAL
              if (entity.asInstanceOf[Player].getGameMode == GameMode.SURVIVAL) {
                // 设置PolarBear对象的目标为实体
                mob.setTarget(entity.asInstanceOf[LivingEntity])
                IsFind = true
                // 跳出循环
                break //todo: break is not supported
              }
            }
          }
        }
      }
    }
    IsFind
  }

  /**
   * 查找上方有空气方块的位置
   *
   * @param location1 起始位置
   * @return 上方有空气方块的位置，如果没有则返回null
   */
  def findAirBlock(location1: Location): Location = {
    var result: Location = null
    val location = location1.clone
    if (location.getBlockY < 0) location.setY(Objects.requireNonNull(location.getWorld).getSeaLevel)
    val list = new util.ArrayList[Integer]
    for (i <- location.getBlockY + 1 until location.getBlockY + 45) {
      val x = location.clone
      x.setY(i)
      if (x.getBlock.getBlockData.getMaterial == Material.AIR) list.add(x.getBlockY)
    }
    if (!list.isEmpty) {
      var maxValue = list.get(0)
      for (num <- list.asScala) {
        if (num > maxValue) maxValue = num
      }
      result = location1.clone
      result.setY(maxValue.toDouble)
    }
    result
  }

}

class PlayerJoin extends Listener {
  @EventHandler def OnJoin(event: PlayerJoinEvent): Unit = {
    new BukkitRunnable() {
      /**
       * 运行方法
       */
      override def run(): Unit = {
        // 如果已经关闭，则取消任务
        if (isShutdown) cancel()
        // 如果玩家在线
        if (event.getPlayer.isOnline) {
          // 如果玩家所在的服务器是"world"世界
          if (event.getPlayer.getWorld.getName.equalsIgnoreCase("world") || event.getPlayer.getWorld.getName.equalsIgnoreCase("world_the_end")) {
            // 创建一个BukkitRunnable任务
            new BukkitRunnable() {
              override def run(): Unit = {
                AutoSetTarget(25, "nian_beastone", event.getPlayer)
                AutoSetTarget(50, "nian_beasttwo", event.getPlayer)
              }
            }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
          }
        }
        else {
          // 如果玩家不在线，则取消任务
          cancel()
        }
      }
    }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(classOf[NianBeast]), 0, 40)
  }
}