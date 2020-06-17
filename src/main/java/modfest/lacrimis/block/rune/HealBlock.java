package modfest.lacrimis.block.rune;

import modfest.lacrimis.Lacrimis;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HealBlock extends CenterRuneBlock {
    public HealBlock() {
        super(50, 1);
    }

    @Override
    protected boolean activate(World world, BlockPos pos, BlockPos pipe, Entity entity, PlayerEntity player) {
        if(entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if(livingEntity.getHealth() < livingEntity.getMaxHealth()) {
                livingEntity.heal(2);
                if(!world.isClient)
                    Lacrimis.LOGGER.info("Entity Healed");
                return true;
            } else
                error(player, "entity");
        } else
            error(player, "entity");
        return true;
    }
}