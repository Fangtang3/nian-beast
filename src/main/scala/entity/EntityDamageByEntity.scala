package com.wenkrang.nian_beast.entity

import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

import java.util.Random
import com.wenkrang.nian_beast.entity.PlayerJoin.findAirBlock
import com.wenkrang.nian_beast.lib
import com.wenkrang.nian_beast.lib.SpigotConsoleColors

class EntityDamageByEntity extends Listener {
  /**
   * 当实体受到实体伤害时触发的事件
   *
   * @param event 实体受到实体伤害的事件
   */
  @EventHandler def onEntityDamageByEntityEvent(event: EntityDamageByEntityEvent): Unit = {
    if (event.getEntity.getScoreboardTags.contains("nian_beastone") || event.getEntity.getScoreboardTags.contains("nian_beasttwo") || event.getEntity.getScoreboardTags.contains("nian_beastthree")) {
      if (event.getDamager.isInstanceOf[Player]) {
        val random = new Random
        val player = event.getDamager.asInstanceOf[Player]
        if (event.getEntity.getScoreboardTags.contains("nian_beastone")) if (random.nextInt(100) < 10) player.sendMessage(lib.SpigotConsoleColors.DARK_RED + "Nian_Beast " + lib.SpigotConsoleColors.RESET + lib.SpigotConsoleColors.WHITE + ">> " + lib.SpigotConsoleColors.RESET + lib.SpigotConsoleColors.DARK_YELLOW + "普通年兽 : 纳米上皮组织，小子! ( 你只能使用烟花来攻击 )")
      }
      if (event.getDamager.getType == EntityType.FIREWORK_ROCKET) {
        val firework = event.getDamager.asInstanceOf[Firework]
        val meta = firework.getFireworkMeta
        val effectCount = meta.getPower
        if (effectCount >= 0) event.setDamage(15)
        if (effectCount >= 1) event.setDamage(20)
        if (effectCount >= 2) event.setDamage(40)
      }
      else event.setDamage(1)
    }
    if (event.getEntity.isInstanceOf[Player]) {
      if (event.getDamager.getScoreboardTags.contains("nian_beastone") || event.getDamager.getScoreboardTags.contains("nian_beasttwo")) {
        val player = event.getEntity.asInstanceOf[Player]
        val random = new Random
        if (random.nextInt(100) < 30) {
          val potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 1)
          event.getEntity.asInstanceOf[Player].addPotionEffect(potionEffect)
        }
        if (random.nextInt(100) < 30) {
          val potionEffect = new PotionEffect(PotionEffectType.HUNGER, 10 * 20, 1)
          event.getEntity.asInstanceOf[Player].addPotionEffect(potionEffect)
        }
        if (random.nextInt(100) < 30) {
          val potionEffect = new PotionEffect(PotionEffectType.WEAKNESS, 10 * 20, 1)
          event.getEntity.asInstanceOf[Player].addPotionEffect(potionEffect)
        }
        if (random.nextInt(100) < 30) {
          val potionEffect = new PotionEffect(PotionEffectType.POISON, 10 * 20, 1)
          event.getEntity.asInstanceOf[Player].addPotionEffect(potionEffect)
        }
      }
      if (event.getDamager.getScoreboardTags.contains("nian_beasttwo")) {
        event.getEntity.setFireTicks(160)
        event.getEntity.setVelocity(event.getDamager.getLocation.getDirection.multiply(3))
      }
      if (event.getDamager.getScoreboardTags.contains("nian_beastthree")) {
        val player = event.getEntity.asInstanceOf[Player]
        player.setFireTicks(160)
        val random = new Random
        if (random.nextInt(100) < 30) {
          val potionEffect = new PotionEffect(PotionEffectType.POISON, 30 * 20, 1)
          event.getEntity.asInstanceOf[Player].addPotionEffect(potionEffect)
        }
        if (random.nextInt(100) < 60) {
          val potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 1)
          event.getEntity.asInstanceOf[Player].addPotionEffect(potionEffect)
        }
        if (random.nextInt(100) < 60) {
          val potionEffect = new PotionEffect(PotionEffectType.HUNGER, 10 * 20, 1)
          event.getEntity.asInstanceOf[Player].addPotionEffect(potionEffect)
        }
        if (random.nextInt(100) < 60) {
          val potionEffect = new PotionEffect(PotionEffectType.WEAKNESS, 10 * 20, 1)
          event.getEntity.asInstanceOf[Player].addPotionEffect(potionEffect)
        }
        if (random.nextInt(100) < 60) {
          val potionEffect = new PotionEffect(PotionEffectType.SLOWNESS, 10 * 20, 1)
          event.getEntity.asInstanceOf[Player].addPotionEffect(potionEffect)
        }
        if (random.nextInt(100) <= 10) {
          val randomnubmer = random.nextInt(100)
          if (randomnubmer < 60) NianBeastEntity.getEntityone(player.getLocation)
          else NianBeastEntity.getEntitytwo(findAirBlock(player.getEyeLocation))
        }
      }
    }
  }
}
