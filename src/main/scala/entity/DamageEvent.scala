package com.wenkrang.nian_beast.entity

import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.entity.EntityDamageEvent

class DamageEvent extends Listener {
  @EventHandler def OnDamageEvent(event: EntityDamageEvent): Unit = {
    if (event.getCause == EntityDamageEvent.DamageCause.FIRE_TICK) if (event.getEntity.getScoreboardTags.contains("nian_beasttwo")) {
      event.getEntity.setFireTicks(0)
      event.setCancelled(true)
    }
  }
}