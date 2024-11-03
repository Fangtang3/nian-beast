package com.wenkrang.nian_beast.entity.raid

import com.wenkrang.nian_beast.entity.raid.RaidEffectShow.showEffect
import com.wenkrang.nian_beast.{NianBeast, lib}
import org.bukkit.{Bukkit, Location}
import org.bukkit.boss.{BarColor, BarStyle}
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.generator.structure.Structure
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.StructureSearchResult

import java.io.IOException
import java.util
import java.util.Objects
import scala.annotation.tailrec

object RaidEffectShow {
  /**
   * 移除玩家的特效
   *
   * @param player 玩家对象
   */
  def removeEffect(player: Player): Unit = {
    // 如果玩家的UUID在Nian_beast.RaidEffect中存在
    if (NianBeast.RaidEffect.contains(Objects.requireNonNull(player.getPlayerProfile.getUniqueId).toString)) {
      // 从Nian_beast.RaidEffect中移除玩家的UUID
      player.removeScoreboardTag("Raid")
      NianBeast.RaidEffect.remove(player.getPlayerProfile.getUniqueId.toString)
    }
  }

  /**
   * 给玩家添加效果
   *
   * @param player 玩家对象
   */
  @throws[IOException]
  @throws[InvalidConfigurationException]
  def AddEffect(player: Player): Unit = {
    // 如果玩家的唯一ID不在Nian_beast.RaidEffect集合中
    if (!NianBeast.RaidEffect.contains(Objects.requireNonNull(player.getPlayerProfile.getUniqueId).toString)) {
      // 将玩家的唯一ID添加到Nian_beast.RaidEffect集合中
      NianBeast.RaidEffect.add(player.getPlayerProfile.getUniqueId.toString)
      // 显示效果给玩家
      RaidEffectShow.showEffect(player)
      player.addScoreboardTag("Raid")
    }
  }

  /**
   * 显示效果
   *
   * @param player 玩家对象
   */
  @throws[IOException]
  @throws[InvalidConfigurationException]
  @tailrec
  def showEffect(player: Player): Unit = {
    //如果玩家有这个效果，就显示效果
    if (NianBeast.RaidEffect.contains(player.getPlayerProfile.getUniqueId.toString)) {
      //效果的显示
      val bossBar = Bukkit.createBossBar(lib.SpigotConsoleColors.DARK_YELLOW + lib.SpigotConsoleColors.BOLD + "年兽" + lib.SpigotConsoleColors.RESET + lib.SpigotConsoleColors.DARK_YELLOW + "袭击 - 年兽之兆", BarColor.BLUE, BarStyle.SOLID)
      bossBar.setVisible(true)
      bossBar.addPlayer(player)
      bossBar.setProgress(1)
      //持续检测玩家的位置，以便更新效果显示
      new BukkitRunnable() {
        override def run(): Unit = {
          if (player.isOnline && NianBeast.RaidEffect.contains(player.getPlayerProfile.getUniqueId.toString)) new BukkitRunnable() {
            override def run(): Unit = {
              val locations = new util.ArrayList[Location]
              val results = new util.ArrayList[StructureSearchResult]
              results.add(player.getWorld.locateNearestStructure(player.getLocation, Structure.VILLAGE_DESERT, 200, false))
              results.add(player.getWorld.locateNearestStructure(player.getLocation, Structure.VILLAGE_SNOWY, 200, false))
              results.add(player.getWorld.locateNearestStructure(player.getLocation, Structure.VILLAGE_PLAINS, 200, false))
              results.add(player.getWorld.locateNearestStructure(player.getLocation, Structure.VILLAGE_SAVANNA, 200, false))
              results.add(player.getWorld.locateNearestStructure(player.getLocation, Structure.VILLAGE_TAIGA, 200, false))
              val structures = new util.ArrayList[Structure]
              import scala.collection.JavaConversions._
              for (result <- results) {
                if (result != null) {
                  locations.add(result.getLocation)
                  structures.add(result.getStructure)
                }
              }
              if (!locations.isEmpty) {
                //从Location获取与玩家的距离
                for (i <- 0 until locations.size) {
                  val location = locations.get(i).clone
                  location.setY(player.getLocation.getBlockY)
                  locations.set(i, location)
                }
                var location: Location = null
                //从distances选出最小值
                var min = locations.get(0).distance(player.getLocation)
                for (i <- 1 until locations.size) {
                  if (locations.get(i).distance(player.getLocation) < min) {
                    min = locations.get(i).distance(player.getLocation)
                    location = locations.get(i)
                  }
                }
                min = min - 40
                min = 160 - min
                if (min > 0) {
                  if (min > 160) {
                    //触发事件
                    bossBar.setStyle(BarStyle.SOLID)
                    bossBar.setProgress(0)
                    bossBar.setColor(BarColor.YELLOW)
                    bossBar.setTitle(lib.SpigotConsoleColors.DARK_YELLOW + lib.SpigotConsoleColors.BOLD + "年兽" + lib.SpigotConsoleColors.RESET + lib.SpigotConsoleColors.DARK_YELLOW + "袭击 - 即将来临")
                    removeEffect(player)
                    RaidEvent.LoadRaid(bossBar, location, player)
                  } else {
                    bossBar.setStyle(BarStyle.SEGMENTED_12)
                    bossBar.setColor(BarColor.RED)
                    bossBar.setTitle(lib.SpigotConsoleColors.DARK_YELLOW + lib.SpigotConsoleColors.BOLD + "年兽" + lib.SpigotConsoleColors.RESET + lib.SpigotConsoleColors.DARK_YELLOW + "袭击")
                    val progress = min / 160
                    try bossBar.setProgress(progress)
                    catch {
                      case e: Exception =>
                    }
                  }
                } else {
                  bossBar.setStyle(BarStyle.SOLID)
                  bossBar.setProgress(1)
                }
              } else {
                //范围内找不到村庄
                bossBar.setProgress(1)
              }
            }
          }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
          else cancel()
        }
      }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(classOf[NianBeast]), 0, 2)
    } else if (player.getScoreboardTags.contains("Raid")) {
      AddEffect(player)
      showEffect(player)
    }
  }
}

class RaidEffectShow extends Listener {
  @EventHandler
  @throws[IOException]
  @throws[InvalidConfigurationException]
  def onRaidEffectShow(event: PlayerJoinEvent): Unit = {
    showEffect(event.getPlayer)
  }
}