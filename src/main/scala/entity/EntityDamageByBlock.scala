package com.wenkrang.nian_beast.entity

import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.entity.EntityDamageByBlockEvent

class EntityDamageByBlock extends Listener {
  @EventHandler def OnEntityDamageByBlockEvent(event: EntityDamageByBlockEvent): Unit = {
    if (event.getEntity.getScoreboardTags.contains("nian_beastone") || event.getEntity.getScoreboardTags.contains("nian_beasttwo")) event.setDamage(1)
  }
}