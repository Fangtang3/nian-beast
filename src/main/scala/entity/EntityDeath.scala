package com.wenkrang.nian_beast
package entity

import com.wenkrang.nian_beast.NianBeast
import com.wenkrang.nian_beast.entity.raid.RaidEffectShow
import org.bukkit.Material
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

import java.io.IOException
import java.util.{Objects, Random}

class EntityDeath extends Listener {
  /**
   * 当实体死亡事件发生时的事件处理器。
   *
   * @param event 实体死亡事件对象
   */
  @EventHandler def OnEntityDeathEvent(event: EntityDeathEvent): Unit = {
    new BukkitRunnable() {
      override def run(): Unit = {
        // 检查实体的得分板标签是否包含"nian_beastone"
        if (event.getEntity.getScoreboardTags.contains("nian_beastone")) {
          event.getDrops.clear()
          val random = new Random
          // 随机生成一个0到100的整数，小于5时，掉落数量为1的钻石
          if (random.nextInt(100) < 5) {
            val itemStack = new ItemStack(Material.DIAMOND)
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack)
          }
          //                    if (true) {
          //                        MapView map = Bukkit.createMap(event.getEntity().getWorld());
          //                        MapRenderer mapRenderer = new MapRenderer() {
          //                            private BufferedImage image;
          //
          //                            {
          //                                try {
          //                                    // 从URL加载图片资源
          //                                    URL imageUrl = new URL("https://tse3-mm.cn.bing.net/th/id/OIP-C.gIpzqPY-96xvftc4L40Z9gHaLJ?w=115&h=180&c=7&r=0&o=5&pid=1.7");
          //                                    image = ImageIO.read(imageUrl);
          //                                } catch (IOException e) {
          //                                    Bukkit.getLogger().severe("Failed to load image: " + e.getMessage());
          //                                }
          //                            }
          //                            @Override
          //                            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
          //                                if (image != null) {
          //                                    // 计算图片在地图上的位置
          //                                    int centerX = 64; // 地图中心点x坐标
          //                                    int centerY = 64; // 地图中心点y坐标
          //
          //                                    // 图片在地图上的左上角坐标
          //                                    int x = centerX - image.getWidth() / 2;
          //                                    int y = centerY - image.getHeight() / 2;
          //
          //                                    // 绘制图片到地图上
          //                                    mapCanvas.drawImage(x, y, image);
          //                                }
          //                            }
          //                        };
          //                        map.addRenderer(mapRenderer);
          //                        ItemStack itemStack = new ItemStack(Material.FILLED_MAP);
          //                        MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
          //                        mapMeta.setColor(Color.RED);
          //                        map.setLocked(true);
          //                        mapMeta.setMapView(map);
          //                        itemStack.setItemMeta(mapMeta);
          //                        event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), itemStack);
          //                    }
          // 随机生成一个0到100的整数，小于5时，掉落数量为1的金块
          if (random.nextInt(100) < 5) {
            val itemStack = new ItemStack(Material.GOLD_BLOCK)
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack)
          }
          // 始终执行，掉落数量为6到10的火药
          val itemStack = new ItemStack(Material.GUNPOWDER, 6 + random.nextInt(4))
          event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack)
          // 随机生成一个0到100的整数，小于70时，掉落数量为8的曲奇
          if (random.nextInt(100) < 70) {
            val itemStack = new ItemStack(Material.COOKIE, 8)
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack)
          }
          // 随机生成一个0到100的整数，小于50时，掉落数量为1到2的绿宝石
          if (random.nextInt(100) < 50) {
            val itemStack = new ItemStack(Material.EMERALD, 1 + random.nextInt(2))
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack)
          }
          if (!event.getEntity.getScoreboardTags.contains("NORaid")) {
            //年兽袭击效果
            if (event.getEntity.getKiller != null) {
              if (!NianBeast.RaidEffect.contains(event.getEntity.getKiller.getPlayerProfile.getUniqueId)) {
                try {
                  RaidEffectShow.AddEffect(event.getEntity.getKiller)
                } catch {
                  case e@(_: IOException | _: InvalidConfigurationException) =>
                    throw new RuntimeException(e)
                }
              }
            }
          }
        }
        // 检查实体的得分板标签是否包含"nian_beasttwo"
        if (event.getEntity.getScoreboardTags.contains("nian_beasttwo")) {
          event.getDrops.clear()
          val random = new Random
          if (random.nextInt(100) < 5) {
            val itemStack = new ItemStack(Material.DIAMOND)
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack)
          }
          if (random.nextInt(100) < 5) {
            val itemStack = new ItemStack(Material.GOLD_BLOCK)
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack)
          }
          if (random.nextInt(100) < 50) {
            val itemStack = new ItemStack(Material.DIAMOND, 10 + random.nextInt(8))
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack)
          }
          if (random.nextInt(100) < 1) {
            val itemStack = new ItemStack(Material.ELYTRA)
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack)
          }
          if (random.nextInt(100) < 10) {
            val itemStack = new ItemStack(Material.DRAGON_BREATH)
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack)
          }
        }
        if (event.getEntity.getScoreboardTags.contains("nian_beastthree")) {
          event.getDrops.clear()
          val itemStack1 = new ItemStack(Material.DIAMOND_BLOCK, 10)
          event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack1)
          val itemStack2 = new ItemStack(Material.GOLD_BLOCK, 20)
          event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack2)
          val itemStack3 = new ItemStack(Material.GUNPOWDER, 128)
          event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack3)
          val random = new Random
          if (random.nextInt(100) < 10) {
            val itemStack = new ItemStack(Material.SPAWNER)
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack)
          }
          if (random.nextInt(100) < 30) {
            val itemStack = new ItemStack(Material.NETHER_STAR, 1 + random.nextInt(2))
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack)
          }
          if (random.nextInt(100) < 30) {
            val itemStack = new ItemStack(Material.ANCIENT_DEBRIS, 10 + random.nextInt(10))
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, itemStack)
          }
          if (random.nextInt(100) < 10) {
            // 创建一个空的附魔书物品实例
            val enchantedBook = new ItemStack(Material.ENCHANTED_BOOK)
            // 获取该物品的元数据以便添加附魔
            val bookMeta = enchantedBook.getItemMeta.asInstanceOf[EnchantmentStorageMeta]
            // 设置抢夺V（最大可达到的等级）
            bookMeta.addStoredEnchant(Enchantment.LOOTING, 5, true) // 第三个参数表示是否允许超出常规等级限制

            // 应用元数据到物品上
            enchantedBook.setItemMeta(bookMeta)
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, enchantedBook)
          }
          if (random.nextInt(100) < 10) {
            // 创建一个空的附魔书物品实例
            val enchantedBook = new ItemStack(Material.ENCHANTED_BOOK)
            // 获取该物品的元数据以便添加附魔
            val bookMeta = enchantedBook.getItemMeta.asInstanceOf[EnchantmentStorageMeta]
            // 设置锋利V（最大可达到的等级）
            bookMeta.addStoredEnchant(Enchantment.SHARPNESS, 5, true) // 修改：1.20.5锋利附魔在API中表示为SHARPNESS

            // 应用元数据到物品上
            enchantedBook.setItemMeta(bookMeta)
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, enchantedBook)
          }
          if (random.nextInt(100) < 10) {
            // 创建一个空的附魔书物品实例
            val enchantedBook = new ItemStack(Material.ENCHANTED_BOOK)
            // 获取该物品的元数据以便添加附魔
            val bookMeta = enchantedBook.getItemMeta.asInstanceOf[EnchantmentStorageMeta]
            // 如果要设置力量这个特定附魔（在1.13+版本中）
            // 使用以下代码替换上一行：
            bookMeta.addStoredEnchant(Enchantment.POWER, 5, true)
            // 应用元数据到物品上
            enchantedBook.setItemMeta(bookMeta)
            event.getEntity.getWorld.dropItem(event.getEntity.getLocation, enchantedBook)
          }
        }
      }
    }.runTaskLater(JavaPlugin.getPlugin(classOf[NianBeast]), 0)
  }
}