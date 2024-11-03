package com.wenkrang.nian_beast.entity

import org.bukkit.entity.EntityType
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.entity.CreatureSpawnEvent

import java.util.Random

class Spawner extends Listener {
  /**
   * 当生物生成时触发的事件
   */
  @EventHandler def onSpawn(event: CreatureSpawnEvent): Unit = {
    if (event.getEntity.getWorld.getName.equalsIgnoreCase("world")) if (event.getSpawnReason eq CreatureSpawnEvent.SpawnReason.NATURAL) { //这里是检测生成的方法，NA
      // TURAL也就是自然生成
      if (event.getEntity.getType == EntityType.SPIDER) {
        val random = new Random
        if (random.nextInt(100) <= 10) {
          NianBeastEntity.getEntityone(event.getLocation)
          event.setCancelled(true)
        }
      }
      if (event.getEntity.getType == EntityType.SKELETON) {
        val random = new Random
        if (random.nextInt(100) <= 5) {
          NianBeastEntity.getEntityone(event.getLocation)
          event.setCancelled(true)
        }
      }
      if (event.getEntity.getType == EntityType.PHANTOM) {
        val random = new Random
        NianBeastEntity.getEntitytwo(event.getLocation)
        event.setCancelled(true)
      }
    }
  }
}