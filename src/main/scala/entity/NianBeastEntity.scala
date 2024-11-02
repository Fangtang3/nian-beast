package com.wenkrang.nian_beast
package entity

import com.wenkrang.nian_beast.lib
import com.wenkrang.nian_beast.lib.SpigotConsoleColors
import org.bukkit.{Location, Particle}
import org.bukkit.attribute.{Attribute, AttributeInstance, AttributeModifier}
import org.bukkit.entity.{EntityType, Phantom, PolarBear, Ravager}
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.{PotionEffect, PotionEffectType}
import org.bukkit.scheduler.BukkitRunnable

import java.util.UUID

object NianBeastEntity {
  /**
   * 我们这里新建一个方法
   *
   * @param location 一个Location对象，表示要生成年兽的位置
   */
  def getEntityone(location: Location): Unit = {
    //特效
    location.getWorld.spawnParticle(Particle.SONIC_BOOM, location, 1)
    new BukkitRunnable() {
      override def run(): Unit = {
        location.getWorld.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, 20)
        // 在指定位置生成一个年兽
        val polarBear = location.getWorld.spawnEntity(location, EntityType.POLAR_BEAR).asInstanceOf[PolarBear]
        // 设置年兽的自定义名称
        polarBear.setCustomName(lib.SpigotConsoleColors.DARK_YELLOW + lib.SpigotConsoleColors.BOLD + "年兽" + lib.SpigotConsoleColors.RESET + lib.SpigotConsoleColors.DARK_YELLOW + "-普通")
        polarBear.setCustomNameVisible(true)
        // 将年兽添加到"nian_beastone"的分数板上
        polarBear.addScoreboardTag("nian_beastone")
        // 设置年兽的最大生命值为60滴
        val newMaxHealth = 60.0
        // 生成一个随机的UUID
        val uuid = UUID.randomUUID
        // 获取年兽的"最大生命值"属性实例
        var maxHealthAttribute = polarBear.getAttribute(Attribute.MAX_HEALTH)
        // 在原有最大生命值的基础上增加30滴
        var modifier = new AttributeModifier("new MaxHealth", 30, AttributeModifier.Operation.ADD_NUMBER)
        maxHealthAttribute.addModifier(modifier)
        polarBear.setHealth(60)
        // 获取年兽的"攻击伤害"属性实例
        maxHealthAttribute = polarBear.getAttribute(Attribute.ATTACK_DAMAGE)
        // 在原有攻击伤害的基础上增加11点
        modifier = new AttributeModifier("new DAMAGE", 30, AttributeModifier.Operation.ADD_NUMBER)
        maxHealthAttribute.addModifier(modifier)
        // 速度I
        val speedEffect = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, true, false)
        speedEffect.apply(polarBear)
        // 闪烁效果
        val GlowingEffect = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, true, false)
        GlowingEffect.apply(polarBear)
      }
    }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 30)
  }

  def getEntitytwo(location: Location): Phantom = {
    val phantom = location.getWorld.spawnEntity(location, EntityType.PHANTOM).asInstanceOf[Phantom]
    phantom.setCustomName(lib.SpigotConsoleColors.DARK_YELLOW + lib.SpigotConsoleColors.BOLD + "年兽" + lib.SpigotConsoleColors.RESET + lib.SpigotConsoleColors.DARK_YELLOW + "-飞行")
    phantom.setCustomNameVisible(true)
    phantom.addScoreboardTag("nian_beasttwo")
    val uuid = UUID.randomUUID
    var maxHealthAttribute = phantom.getAttribute(Attribute.MAX_HEALTH)
    //这是在北极熊原有的30滴血上增加30滴
    var modifier = new AttributeModifier("new MaxHealth", 30, AttributeModifier.Operation.ADD_NUMBER)
    maxHealthAttribute.addModifier(modifier)
    phantom.setHealth(50)
    maxHealthAttribute = phantom.getAttribute(Attribute.ATTACK_DAMAGE)
    modifier = new AttributeModifier("new DAMAGE", 20, AttributeModifier.Operation.ADD_NUMBER)
    maxHealthAttribute.addModifier(modifier)
    // 速度II
    val speedEffect = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, true, false)
    speedEffect.apply(phantom)
    val GlowingEffect = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, true, false)
    GlowingEffect.apply(phantom)
    phantom
  }

  def getEntitythree(location: Location): Ravager = {
    val ravager = location.getWorld.spawnEntity(location, EntityType.RAVAGER).asInstanceOf[Ravager]
    ravager.setCustomName(lib.SpigotConsoleColors.DARK_YELLOW + lib.SpigotConsoleColors.BOLD + "年兽" + lib.SpigotConsoleColors.RESET + lib.SpigotConsoleColors.DARK_YELLOW + "王")
    ravager.setCustomNameVisible(true)
    ravager.addScoreboardTag("nian_beastthree")
    val uuid = UUID.randomUUID
    var maxHealthAttribute = ravager.getAttribute(Attribute.MAX_HEALTH)
    var modifier = new AttributeModifier("new MaxHealth", 500, AttributeModifier.Operation.ADD_NUMBER)
    maxHealthAttribute.addModifier(modifier)
    ravager.setHealth(600)
    maxHealthAttribute = ravager.getAttribute(Attribute.ATTACK_DAMAGE)
    modifier = new AttributeModifier("new DAMAGE", 12, AttributeModifier.Operation.ADD_NUMBER)
    maxHealthAttribute.addModifier(modifier)
    val GlowingEffect = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, true, false)
    GlowingEffect.apply(ravager)
    ravager
  }
}

