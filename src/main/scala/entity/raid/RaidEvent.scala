package com.wenkrang.nian_beast.entity.raid

import com.wenkrang.nian_beast.{NianBeast, lib}
import com.wenkrang.nian_beast.lib.SpigotConsoleColors
import org.bukkit._
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.attribute.AttributeModifier
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity._
import org.bukkit.generator.structure.Structure
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.StructureSearchResult

import java.util._
import java.util

object RaidEvent {
  def getnearvillage(player: Player): Structure = {
    val locations = new util.ArrayList[Location]
    val results = new util.ArrayList[StructureSearchResult]
    var structure: Structure = null
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
          structure = results.get(i).getStructure
        }
      }
    }
    structure
  }

  def RaidThree(bossBar: BossBar, location: Location, player: Player, Realocation: Location): Unit = {
    bossBar.setStyle(BarStyle.SOLID)
    bossBar.setProgress(0)
    new BukkitRunnable() {
      override def run(): Unit = {
        if (NianBeast.isShutdown) cancel()
        if (bossBar.getProgress + 0.01 > 1) {
          new BukkitRunnable() {
            override def run(): Unit = {
              // 袭击号角
              val nearbyEntities = Objects.requireNonNull(location.getWorld).getNearbyEntities(location, 150, 150, 150)
              import scala.collection.JavaConversions._
              for (entity <- nearbyEntities) {
                if (entity.isInstanceOf[Player]) {
                  val player1 = entity.asInstanceOf[Player]
                  player1.playSound(player1.getLocation, Sound.EVENT_RAID_HORN, 100, 1)
                }
              }
              location.getWorld.playSound(location, Sound.EVENT_RAID_HORN, 100, 1)
            }
          }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
          // 袭击开始
          new BukkitRunnable() {
            override def run(): Unit = {
              val entities = new util.ArrayList[Entity]
              for (i <- 0 until 20) {
                val location1 = location.clone
                location1.add(0, 20, 0)
                val random = new Random
                if (random.nextInt(100) > 50) location1.add(random.nextInt(30), 0, 0)
                else location1.add(-random.nextInt(30), 0, 0)
                if (random.nextInt(100) > 50) location1.add(0, 0, random.nextInt(30))
                else location1.add(0, 0, -random.nextInt(30))
                player.sendMessage(location1.toString)
                location1.getWorld.spawnParticle(Particle.SONIC_BOOM, location1, 1)
                new BukkitRunnable() {
                  override def run(): Unit = {
                    location1.getWorld.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location1, 50)
                    // 在指定位置生成一个年兽
                    val polarBear = location1.getWorld.spawnEntity(location1, EntityType.POLAR_BEAR).asInstanceOf[PolarBear]
                    // 设置年兽的自定义名称
                    polarBear.setCustomName(lib.SpigotConsoleColors.DARK_YELLOW + lib.SpigotConsoleColors.BOLD + "年兽" + lib.SpigotConsoleColors.RESET + lib.SpigotConsoleColors.DARK_YELLOW + "-普通")
                    polarBear.setCustomNameVisible(true)
                    // 将年兽添加到"nian_beastone"的分数板上
                    polarBear.addScoreboardTag("nian_beastone")
                    polarBear.addScoreboardTag("NORaid")
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
                    modifier = new AttributeModifier("new DAMAGE", 11, AttributeModifier.Operation.ADD_NUMBER)
                    maxHealthAttribute.addModifier(modifier)
                    // 速度I
                    val speedEffect = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, true, false)
                    speedEffect.apply(polarBear)
                    // 闪烁效果
                    val GlowingEffect = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, true, false)
                    GlowingEffect.apply(polarBear)
                    entities.add(polarBear)
                    val SlowEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 1, true, false)
                    SlowEffect.apply(polarBear)
                  }
                }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 30)
              }
              for (i <- 0 until 15) {
                val location1 = location.clone
                location1.add(0, 20, 0)
                val random = new Random
                if (random.nextInt(100) > 50) location1.add(random.nextInt(30), 0, 0)
                else location1.add(-random.nextInt(30), 0, 0)
                if (random.nextInt(100) > 50) location1.add(0, 0, random.nextInt(30))
                else location1.add(0, 0, -random.nextInt(30))
                location1.getWorld.spawnParticle(Particle.SONIC_BOOM, location1, 1)
                player.sendMessage(location1.toString)
                new BukkitRunnable() {
                  override def run(): Unit = {
                    location1.getWorld.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location1, 50)
                    val phantom = location1.getWorld.spawnEntity(location1, EntityType.PHANTOM).asInstanceOf[Phantom]
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
                    modifier = new AttributeModifier("new DAMAGE", 11, AttributeModifier.Operation.ADD_NUMBER)
                    maxHealthAttribute.addModifier(modifier)
                    // 速度II
                    val speedEffect = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, true, false)
                    speedEffect.apply(phantom)
                    val GlowingEffect = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, true, false)
                    GlowingEffect.apply(phantom)
                    entities.add(phantom)
                  }
                }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 30)
              }
              if (true) {
                val location1 = location.clone
                location1.add(0, 20, 0)
                val random = new Random
                if (random.nextInt(100) > 50) location1.add(random.nextInt(30), 0, 0)
                else location1.add(-random.nextInt(30), 0, 0)
                if (random.nextInt(100) > 50) location1.add(0, 0, random.nextInt(30))
                else location1.add(0, 0, -random.nextInt(30))
                location1.getWorld.spawnParticle(Particle.SONIC_BOOM, location1, 1)
                player.sendMessage(location1.toString)
                new BukkitRunnable() {
                  override def run(): Unit = {
                    val ravager = location1.getWorld.spawnEntity(location1, EntityType.RAVAGER).asInstanceOf[Ravager]
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
                    entities.add(ravager)
                  }
                }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 30)
              }
              bossBar.setStyle(BarStyle.SEGMENTED_12)
              new BukkitRunnable() {
                override def run(): Unit = {
                  if (NianBeast.isShutdown) cancel()
                  var LIFE = 0
                  import scala.collection.JavaConversions._
                  for (entity <- entities) {
                    if (!entity.isDead) LIFE += 1
                  }
                  val finalLIFE = LIFE
                  new BukkitRunnable() {
                    override def run(): Unit = {
                      player.sendMessage(entities.toString)
                      bossBar.setProgress(finalLIFE.toDouble / 36)
                    }
                  }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
                  if (bossBar.getProgress == 0) {
                    new BukkitRunnable() {
                      override def run(): Unit = {
                        bossBar.setProgress(1)
                        bossBar.setTitle(lib.SpigotConsoleColors.DARK_YELLOW + lib.SpigotConsoleColors.BOLD + "年兽" + lib.SpigotConsoleColors.RESET + lib.SpigotConsoleColors.DARK_YELLOW + "袭击 - 胜利")
                        val nearbyEntities = Objects.requireNonNull(location.getWorld).getNearbyEntities(location, 150, 150, 150)
                        import scala.collection.JavaConversions._
                        for (entity <- nearbyEntities) {
                          if (entity.isInstanceOf[Player]) {
                            val player1 = entity.asInstanceOf[Player]
                            val potionEffect = new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 114514 * 20, 1, true, false)
                            potionEffect.apply(player1)
                            player1.playSound(player1.getLocation, Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1)
                          }
                        }
                      }
                    }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
                    new BukkitRunnable() {
                      override def run(): Unit = {
                        bossBar.setVisible(false)
                        bossBar.removeAll()
                        NianBeast.Keys.remove(String.valueOf(location.getBlockX) + String.valueOf(location.getBlockY) + String.valueOf(location.getBlockZ))
                      }
                    }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 300)
                    cancel()
                  }
                }
              }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(classOf[NianBeast]), 40, 20)
            }
          }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 60)
          cancel()
        }
        else new BukkitRunnable() {
          override def run(): Unit = {
            bossBar.setProgress(bossBar.getProgress + 0.01)
          }
        }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
      }
    }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(classOf[NianBeast]), 0, 5)
  }

  def RaidTwo(bossBar: BossBar, location: Location, player: Player, Realocation: Location): Unit = {
    bossBar.setStyle(BarStyle.SOLID)
    bossBar.setProgress(0)
    new BukkitRunnable() {
      override def run(): Unit = {
        if (NianBeast.isShutdown) cancel()
        if (bossBar.getProgress + 0.01 > 1) {
          // 袭击号角
          new BukkitRunnable() {
            override def run(): Unit = {
              val nearbyEntities = Objects.requireNonNull(location.getWorld).getNearbyEntities(location, 150, 150, 150)
              import scala.collection.JavaConversions._
              for (entity <- nearbyEntities) {
                if (entity.isInstanceOf[Player]) {
                  val player1 = entity.asInstanceOf[Player]
                  player1.playSound(player1.getLocation, Sound.EVENT_RAID_HORN, 100, 1)
                }
              }
              location.getWorld.playSound(location, Sound.EVENT_RAID_HORN, 100, 1)
            }
          }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
          // 袭击开始
          new BukkitRunnable() {
            override def run(): Unit = {
              val entities = new util.ArrayList[Entity]
              for (i <- 0 until 15) {
                val location1 = location.clone
                location1.add(0, 20, 0)
                val random = new Random
                if (random.nextInt(100) > 50) location1.add(random.nextInt(30), 0, 0)
                else location1.add(-random.nextInt(30), 0, 0)
                if (random.nextInt(100) > 50) location1.add(0, 0, random.nextInt(30))
                else location1.add(0, 0, -random.nextInt(30))
                location1.getWorld.spawnParticle(Particle.SONIC_BOOM, location1, 1)
                new BukkitRunnable() {
                  override def run(): Unit = {
                    location1.getWorld.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location1, 50)
                    // 在指定位置生成一个年兽
                    val polarBear = location1.getWorld.spawnEntity(location1, EntityType.POLAR_BEAR).asInstanceOf[PolarBear]
                    // 设置年兽的自定义名称
                    polarBear.setCustomName(lib.SpigotConsoleColors.DARK_YELLOW + lib.SpigotConsoleColors.BOLD + "年兽" + lib.SpigotConsoleColors.RESET + lib.SpigotConsoleColors.DARK_YELLOW + "-普通")
                    polarBear.setCustomNameVisible(true)
                    // 将年兽添加到"nian_beastone"的分数板上
                    polarBear.addScoreboardTag("nian_beastone")
                    polarBear.addScoreboardTag("NORaid")
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
                    modifier = new AttributeModifier("new DAMAGE", 11, AttributeModifier.Operation.ADD_NUMBER)
                    maxHealthAttribute.addModifier(modifier)
                    // 速度I
                    val speedEffect = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, true, false)
                    speedEffect.apply(polarBear)
                    // 闪烁效果
                    val GlowingEffect = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, true, false)
                    GlowingEffect.apply(polarBear)
                    entities.add(polarBear)
                    val SlowEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 1, true, false)
                    SlowEffect.apply(polarBear)
                  }
                }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 30)
              }
              for (i <- 0 until 10) {
                val location1 = location.clone
                location1.add(0, 10, 0)
                val random = new Random
                if (random.nextInt(100) > 50) location1.add(random.nextInt(30), 0, 0)
                else location1.add(-random.nextInt(30), 0, 0)
                if (random.nextInt(100) > 50) location1.add(0, 0, random.nextInt(30))
                else location1.add(0, 0, -random.nextInt(30))
                location1.getWorld.spawnParticle(Particle.SONIC_BOOM, location1, 1)
                new BukkitRunnable() {
                  override def run(): Unit = {
                    location1.getWorld.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location1, 50)
                    val phantom = location1.getWorld.spawnEntity(location1, EntityType.PHANTOM).asInstanceOf[Phantom]
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
                    modifier = new AttributeModifier("new DAMAGE", 11, AttributeModifier.Operation.ADD_NUMBER)
                    maxHealthAttribute.addModifier(modifier)
                    // 速度II
                    val speedEffect = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, true, false)
                    speedEffect.apply(phantom)
                    val GlowingEffect = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, true, false)
                    GlowingEffect.apply(phantom)
                    entities.add(phantom)
                  }
                }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 30)
              }
              bossBar.setStyle(BarStyle.SEGMENTED_12)
              new BukkitRunnable() {
                override def run(): Unit = {
                  if (NianBeast.isShutdown) cancel()
                  var LIFE = 0
                  import scala.collection.JavaConversions._
                  for (entity <- entities) {
                    if (!entity.isDead) LIFE += 1
                  }
                  val finalLIFE = LIFE
                  new BukkitRunnable() {
                    override def run(): Unit = {
                      bossBar.setProgress(finalLIFE.toDouble / 25)
                    }
                  }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
                  if (bossBar.getProgress == 0) {
                    new BukkitRunnable() {
                      override def run(): Unit = {
                        RaidThree(bossBar, location, player, location)
                      }
                    }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
                    cancel()
                  }
                }
              }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(classOf[NianBeast]), 40, 20)
            }
          }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 60)
          cancel()
        }
        else new BukkitRunnable() {
          override def run(): Unit = {
            if (!(bossBar.getProgress + 0.01 > 1)) bossBar.setProgress(bossBar.getProgress + 0.01)
          }
        }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
      }
    }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(classOf[NianBeast]), 0, 5)
  }

  /**
   * 袭击开始
   *
   * @param bossBar     袭击进度条
   * @param location    袭击位置
   * @param player      玩家
   * @param Realocation 重新定位
   */
  def RaidMain(bossBar: BossBar, location: Location, player: Player, Realocation: Location): Unit = {
    // 袭击号角
    val nearbyEntities = location.getWorld.getNearbyEntities(location, 150, 150, 150)
    import scala.collection.JavaConversions._
    for (entity <- nearbyEntities) {
      if (entity.isInstanceOf[Player]) {
        val player1 = entity.asInstanceOf[Player]
        player1.playSound(player1.getLocation, Sound.EVENT_RAID_HORN, 100, 1)
      }
    }
    location.getWorld.playSound(location, Sound.EVENT_RAID_HORN, 100, 1)
    // 袭击开始
    new BukkitRunnable() {
      override def run(): Unit = {
        val entities = new util.ArrayList[Entity]
        for (i <- 0 until 10) {
          val location1 = location.clone
          location1.add(0, 20, 0)
          val random = new Random
          if (random.nextInt(100) > 50) location1.add(random.nextInt(30), 0, 0)
          else location1.add(-random.nextInt(30), 0, 0)
          if (random.nextInt(100) > 50) location1.add(0, 0, random.nextInt(30))
          else location1.add(0, 0, -random.nextInt(30))
          location1.getWorld.spawnParticle(Particle.SONIC_BOOM, location1, 1)
          new BukkitRunnable() {
            override def run(): Unit = {
              location1.getWorld.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location1, 50)
              // 在指定位置生成一个年兽
              val polarBear = location1.getWorld.spawnEntity(location1, EntityType.POLAR_BEAR).asInstanceOf[PolarBear]
              // 设置年兽的自定义名称
              polarBear.setCustomName(lib.SpigotConsoleColors.DARK_YELLOW + lib.SpigotConsoleColors.BOLD + "年兽" + lib.SpigotConsoleColors.RESET + lib.SpigotConsoleColors.DARK_YELLOW + "-普通")
              polarBear.setCustomNameVisible(true)
              // 将年兽添加到"nian_beastone"的分数板上
              polarBear.addScoreboardTag("nian_beastone")
              polarBear.addScoreboardTag("NORaid")
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
              modifier = new AttributeModifier("new DAMAGE", 11, AttributeModifier.Operation.ADD_NUMBER)
              maxHealthAttribute.addModifier(modifier)
              // 速度I
              val speedEffect = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, true, false)
              speedEffect.apply(polarBear)
              // 闪烁效果
              val GlowingEffect = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, true, false)
              GlowingEffect.apply(polarBear)
              entities.add(polarBear)
              val SlowEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 1, true, false)
              SlowEffect.apply(polarBear)
            }
          }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 30)
        }
        bossBar.setStyle(BarStyle.SEGMENTED_12)
        new BukkitRunnable() {
          override def run(): Unit = {
            if (NianBeast.isShutdown) cancel()
            var LIFE = 0
            import scala.collection.JavaConversions._
            for (entity <- entities) {
              if (!entity.isDead) LIFE += 1
            }
            val finalLIFE = LIFE
            new BukkitRunnable() {
              override def run(): Unit = {
                bossBar.setProgress(finalLIFE.toDouble / 10)
              }
            }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
            val tmp = finalLIFE.toDouble / 10
            if (tmp == 0) {
              new BukkitRunnable() {
                override def run(): Unit = {
                  new BukkitRunnable() {
                    override def run(): Unit = {
                      RaidTwo(bossBar, location, player, location)
                    }
                  }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
                }
              }.runTaskLaterAsynchronously(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
              cancel()
            }
          }
        }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(classOf[NianBeast]), 40, 20)
      }
    }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 60)
  }

  /**
   * 加载年兽BossBar并开始加载进度
   *
   * @param bossBar  年兽BossBar对象
   * @param location 事件位置
   * @param player   玩家对象
   */
  def LoadRaid(bossBar: BossBar, location: Location, player: Player): Unit = {
    var b = false
    import scala.collection.JavaConversions._
    for (string <- NianBeast.Keys) {
      if (string.equalsIgnoreCase(String.valueOf(location.getBlockX) + String.valueOf(location.getBlockY) + String.valueOf(location.getBlockZ))) b = true
    }
    if (!b) {
      NianBeast.Keys.add(String.valueOf(location.getBlockX) + String.valueOf(location.getBlockY) + String.valueOf(location.getBlockZ))
      new BukkitRunnable() {
        override def run(): Unit = {
          //跑条子
          if (bossBar.getProgress + 0.01 >= 1) {
            new BukkitRunnable() {
              override def run(): Unit = {
                bossBar.setTitle(lib.SpigotConsoleColors.DARK_YELLOW + lib.SpigotConsoleColors.BOLD + "年兽" + lib.SpigotConsoleColors.RESET + lib.SpigotConsoleColors.DARK_YELLOW + "袭击")
                RaidMain(bossBar, location, player, location)
              }
            }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
            cancel()
          }
          else new BukkitRunnable() {
            override def run(): Unit = {
              bossBar.setProgress(bossBar.getProgress + 0.01)
              //寻找袭击位置
              val random = new Random
              if (random.nextInt(100) < 5) Objects.requireNonNull(location.getWorld).playSound(location, Sound.BLOCK_BELL_USE, 7F, 1)
            }
          }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
        }
      }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(classOf[NianBeast]), 0, 5)
      new BukkitRunnable() {
        override def run(): Unit = {
          if (NianBeast.isShutdown) cancel()
          val nearbyEntities = Objects.requireNonNull(location.getWorld).getNearbyEntities(location, 150, 150, 150)
          import scala.collection.JavaConversions._
          for (entity <- nearbyEntities) {
            if (entity.isInstanceOf[Player]) {
              val player1 = entity.asInstanceOf[Player]
              bossBar.addPlayer(player1)
            }
          }
        }
      }.runTaskTimer(JavaPlugin.getPlugin(classOf[NianBeast]), 0, 20)
    }
    else {
      bossBar.removeAll()
      bossBar.setVisible(false)
    }
  }
}