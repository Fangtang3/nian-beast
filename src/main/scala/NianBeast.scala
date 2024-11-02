package com.wenkrang.nian_beast

import command.NianBeastCommand
import entity.raid.RaidEffectShow
import entity._

import com.wenkrang.nian_beast.lib.SpigotConsoleColors
import org.bukkit.plugin.java.JavaPlugin

import java.util

object NianBeast {
  var isShutdown = false
  var Keys = new util.ArrayList[String]()
  var RaidEffect = new util.ArrayList[String]()
}
final class NianBeast extends JavaPlugin {
  override def onEnable(): Unit = {
    // Plugin startup logic
    val consoleSender = getServer.getConsoleSender
    consoleSender.sendMessage(lib.SpigotConsoleColors.BLUE + "[*] " + lib.SpigotConsoleColors.RESET + "正在加载Nian_beast, 版本 -=> 24a01a")
    consoleSender.sendMessage("    _   ___                  __                    __ ")
    consoleSender.sendMessage("   / | / (_)___ _____       / /_  ___  ____ ______/ /_")
    consoleSender.sendMessage("  /  |/ / / __ `/ __ \\     / __ \\/ _ \\/ __ `/ ___/ __/")
    consoleSender.sendMessage(" / /|  / / /_/ / / / /    / /_/ /  __/ /_/ (__  ) /_  ")
    consoleSender.sendMessage("/_/ |_/_/\\__,_/_/ /_/____/_.___/\\___/\\__,_/____/\\__/  ")
    consoleSender.sendMessage("                   /_____/                            ")
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
    getServer.getPluginManager.registerEvents(new safdasfs, this) //没啥事别开

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
