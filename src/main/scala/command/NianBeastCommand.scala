package com.wenkrang.nian_beast.command

import com.wenkrang.nian_beast.entity.NianBeastEntity
import com.wenkrang.nian_beast.entity.raid.RaidEffectShow
import com.wenkrang.nian_beast.{NianBeast, lib}
import org.bukkit._
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.entity._
import org.bukkit.util.Vector

import java.io.IOException
import java.util.Random
import com.wenkrang.nian_beast.entity.PlayerJoin.findAirBlock
import com.wenkrang.nian_beast.lib.SpigotConsoleColors


object NianBeastCommand {
  /**
   * 向传入的CommandSender对象发送帮助信息
   *
   * @param sender CommandSender对象，用于发送消息
   */
  def gethelp(sender: CommandSender): Unit = {
    sender.sendMessage("§7[!]  §4年兽 - nian_beast §7正在运行")
    sender.sendMessage(" §4| §7help  帮助列表")
    sender.sendMessage(" §4| §7spawnLO 生成普通年兽")
    sender.sendMessage(" §4| §7spawnLT 生成飞行年兽")
    sender.sendMessage(" §4| §7testwo  测试生成第二只年兽的机制")
    sender.sendMessage(" §4| §7spawnLThree 生成年兽王")
  }

  def getLocation(player: Location, targetLocation: Location): Location = {
    val playerLocation = player.clone
    // 获取从from到to的位置向量
    val direction = targetLocation.toVector.subtract(playerLocation.toVector).normalize
    // 计算yaw (水平旋转角度)
    var yaw = Math.toDegrees(Math.atan2(direction.getZ, direction.getX)).toFloat - 90F
    // Minecraft中的yaw范围是-180到180，所以需要将其调整到这个范围内
    if (yaw < -180.0F) yaw += 360.0F
    else if (yaw > 180.0F) yaw -= 360.0F
    // 计算pitch (垂直旋转角度)
    val pitch = -Math.toDegrees(Math.atan2(direction.getY, Math.sqrt(direction.getX * direction.getX + direction.getZ * direction.getZ))).toFloat
    val location = player
    location.setYaw(yaw)
    location.setPitch(pitch)
    location
  }
}

class NianBeastCommand extends CommandExecutor {
  override def onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array[String]): Boolean = {
    if (strings.length == 0) NianBeastCommand.gethelp(commandSender)
    else if (commandSender.isOp) {
      if (strings(0).equalsIgnoreCase("help")) NianBeastCommand.gethelp(commandSender)
      if (strings(0).equalsIgnoreCase("spawnLO")) if (commandSender.isOp) if (commandSender.isInstanceOf[Player]) {
        val player = commandSender.asInstanceOf[Player]
        NianBeastEntity.getEntityone(player.getLocation)
        commandSender.sendMessage(lib.SpigotConsoleColors.BLUE + "[-] " + lib.SpigotConsoleColors.RESET + "普通年兽已生成")
      }
      else commandSender.sendMessage(lib.SpigotConsoleColors.DARK_RED + "[-] " + lib.SpigotConsoleColors.RESET + "你必须在游戏内才可以使用此命令")
      else commandSender.sendMessage(lib.SpigotConsoleColors.DARK_RED + "[-] " + lib.SpigotConsoleColors.RESET + "阿巴阿巴")
      if (strings(0).equalsIgnoreCase("spawnLT")) if (commandSender.isOp) if (commandSender.isInstanceOf[Player]) {
        val player = commandSender.asInstanceOf[Player]
        NianBeastEntity.getEntitytwo(findAirBlock(player.getEyeLocation))
        commandSender.sendMessage(lib.SpigotConsoleColors.BLUE + "[-] " + lib.SpigotConsoleColors.RESET + "飞行年兽已生成")
      }
      else commandSender.sendMessage(lib.SpigotConsoleColors.DARK_RED + "[-] " + lib.SpigotConsoleColors.RESET + "你必须在游戏内才可以使用此命令")
      else commandSender.sendMessage(lib.SpigotConsoleColors.DARK_RED + "[-] " + lib.SpigotConsoleColors.RESET + "阿巴阿巴")
      if (strings(0).equalsIgnoreCase("spawnLThree")) if (commandSender.isOp) if (commandSender.isInstanceOf[Player]) {
        val player = commandSender.asInstanceOf[Player]
        NianBeastEntity.getEntitythree(player.getLocation)
        commandSender.sendMessage(lib.SpigotConsoleColors.BLUE + "[-] " + lib.SpigotConsoleColors.RESET + "年兽王已生成")
      }
      else commandSender.sendMessage(lib.SpigotConsoleColors.DARK_RED + "[-] " + lib.SpigotConsoleColors.RESET + "你必须在游戏内才可以使用此命令")
      else commandSender.sendMessage(lib.SpigotConsoleColors.DARK_RED + "[-] " + lib.SpigotConsoleColors.RESET + "阿巴阿巴")
      if (strings(0).equalsIgnoreCase("testwo")) if (commandSender.isOp) if (commandSender.isInstanceOf[Player]) {
        val player = commandSender.asInstanceOf[Player]
        if (player.getPlayer.getWorld.getName.equalsIgnoreCase("world_the_end")) {
          // 如果玩家眼睛所处的方块高度小于320
          if (player.getPlayer.getEyeLocation.getBlockY < 320) {
            val random = new Random
            // 随机数小于30时
            if (random.nextInt(100) < 30) {
              // 在玩家眼睛所处的方块周围寻找空气方块
              for (i <- 0 until 3) {
                NianBeastEntity.getEntitytwo(findAirBlock(player.getPlayer.getEyeLocation))
              }
            }
          }
        }
        commandSender.sendMessage(lib.SpigotConsoleColors.BLUE + "[-] " + lib.SpigotConsoleColors.RESET + "飞行年兽测试")
      }
      else commandSender.sendMessage(lib.SpigotConsoleColors.DARK_RED + "[-] " + lib.SpigotConsoleColors.RESET + "你必须在游戏内才可以使用此命令")
      else commandSender.sendMessage(lib.SpigotConsoleColors.DARK_RED + "[-] " + lib.SpigotConsoleColors.RESET + "阿巴阿巴")
      if (strings(0).equalsIgnoreCase("test")) {
        val player = commandSender.asInstanceOf[Player]
        try RaidEffectShow.AddEffect(player)
        catch {
          case e@(_: IOException | _: InvalidConfigurationException) =>
            throw new RuntimeException(e)
        }
      }
      if (strings(0).equalsIgnoreCase("test2")) {
        val player = commandSender.asInstanceOf[Player]
        player.sendMessage(NianBeast.Keys.toString)
        player.sendMessage(NianBeast.RaidEffect.toString)
        player.sendMessage(player.getScoreboardTags.toString)
      }
    }
    else commandSender.sendMessage(lib.SpigotConsoleColors.DARK_RED + "[-] " + lib.SpigotConsoleColors.RESET + "阿巴阿巴")
    true
  }
}
