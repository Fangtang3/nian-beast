package com.wenkrang.nian_beast.entity

import com.wenkrang.nian_beast.NianBeast
import org.bukkit.Location
import org.bukkit.entity._
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.NotNull

import java.util._

object EntityTarget {
  def EvokerFangsDamage(mob: Mob): Unit = {
    val mobEyeLocation = mob.getLocation
    val location2 = mobEyeLocation.clone
    location2.add(location2.getDirection.clone.multiply(2.5))
    for (i <- 0 until 30) {
      location2.add(location2.getDirection.clone.multiply(0.8))
      location2.setPitch(0)
      mob.getWorld.spawn(location2, classOf[EvokerFangs])
      val world = mob.getWorld
      @NotNull val nearbyEntities = world.getNearbyEntities(location2, 1, 1, 1)
      import scala.collection.JavaConversions._
      for (entity <- nearbyEntities) {
        if (entity.isInstanceOf[Damageable]) {
          val damageable = entity.asInstanceOf[Damageable]
          damageable.damage(19)
        }
      }
    }
  }

  def getLookDirection(from: Location, to: Location): Array[Double] = {
    val direction = to.toVector.subtract(from.toVector).normalize
    val yaw = Math.atan2(direction.getY, direction.getX) * 180 / Math.PI - 90
    val pitch = -Math.atan2(direction.getZ, Math.sqrt(direction.getX * direction.getX + direction.getY * direction.getY)) * 180 / Math.PI
    Array[Double](yaw, pitch)
  }

  def Damage(player: Player, mob: Mob): Unit = {
    val distance = mob.getLocation.distance(player.getLocation)
    if (distance < 30) EvokerFangsDamage(mob)
  }

  @EventHandler def onEntityTarget(event: EntityTargetLivingEntityEvent): Unit = {
    if (event.getTarget != null) if (event.getTarget.isInstanceOf[Player]) {
      val player = event.getTarget.asInstanceOf[Player]
      if (event.getEntity.getScoreboardTags.contains("nian_beastthree")) {
        val mob = event.getEntity.asInstanceOf[Mob]
        new BukkitRunnable() {
          override def run(): Unit = {
            if (!NianBeast.isShutdown && mob.getTarget != null && !mob.isDead) {
              val random = new Random
              new BukkitRunnable() {
                override def run(): Unit = {
                  Damage(player, mob)
                }
              }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
            }
            else cancel()
          }
        }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(classOf[NianBeast]), 0, 60)
        new BukkitRunnable() {
          override def run(): Unit = {
            if (!NianBeast.isShutdown && mob.getTarget != null && !mob.isDead) {
              val random = new Random
              new BukkitRunnable() {
                override def run(): Unit = {
                  mob.teleport(player.getLocation)
                }
              }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
            }
            else cancel()
          }
        }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(classOf[NianBeast]), 0, 300)
      }
    }
  }
}

class EntityTarget extends Listener {}