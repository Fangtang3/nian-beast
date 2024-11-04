package com.wenkrang.nian_beast

import command.NianBeastCommand
import entity._
import entity.raid.RaidEffectShow

import org.bukkit.plugin.java.JavaPlugin

import java.util
import java.util.function.Consumer

object NianBeast {
  var isShutdown = false
  val Keys = new util.ArrayList[String]()
  val RaidEffect = new util.ArrayList[String]()
  private val asciiArt =
    """"    _   ___                  __                    __
      |   / | / (_)___ _____       / /_  ___  ____ ______/ /_
      |  /  |/ / / __ `/ __ \\     / __ \\/ _ \\/ __ `/ ___/ __/
      | / /|  / / /_/ / / / /    / /_/ /  __/ /_/ (__  ) /_
      |/_/ |_/_/\\__,_/_/ /_/____/_.___/\\___/\\__,_/____/\\__/
      |                   /_____/                            """.stripMargin.lines()
}
final class NianBeast extends JavaPlugin {
  override def onEnable(): Unit = {
    // Plugin startup logic
    val consoleSender = getServer.getConsoleSender
    consoleSender.sendMessage(lib.SpigotConsoleColors.BLUE + "[*] " + lib.SpigotConsoleColors.RESET + "正在加载Nian_beast, 版本 -=> 24a01a")
    NianBeast.asciiArt.forEach(new Consumer[String] {
      override def accept(i: String): Unit = {
        consoleSender.sendMessage(i)
      }
    })
    consoleSender.sendMessage(lib.SpigotConsoleColors.BLUE + "[*] " + lib.SpigotConsoleColors.RESET + "正在注册命令")
    //Load Command
    getServer.getPluginCommand("nb").setExecutor(new NianBeastCommand)
    consoleSender.sendMessage(lib.SpigotConsoleColors.BLUE + "[*] " + lib.SpigotConsoleColors.RESET + "正在注册监听器")
    getServer.getPluginManager.registerEvents(new Spawner, this)
    getServer.getPluginManager.registerEvents(new EntityDamageByBlock, this)
    getServer.getPluginManager.registerEvents(new EntityDamageByEntity, this)
    getServer.getPluginManager.registerEvents(new EntityDeath, this)
    getServer.getPluginManager.registerEvents(new PlayerJoin, this)
    getServer.getPluginManager.registerEvents(new SpawnerTwo, this)
    //getServer.getPluginManager.registerEvents(new safdasfs, this) //没啥事别开

    getServer.getPluginManager.registerEvents(new EntityTarget, this)
    getServer.getPluginManager.registerEvents(new RaidEffectShow, this)
    getServer.getPluginManager.registerEvents(new DamageEvent, this)
  }

  override def onDisable(): Unit = {
    // Plugin shutdown logic
    val consoleSender = getServer.getConsoleSender
    consoleSender.sendMessage(lib.SpigotConsoleColors.BLUE + "[*] " + lib.SpigotConsoleColors.RESET + "NianBeast 正在关闭")
    NianBeast.isShutdown = true
  }
}
