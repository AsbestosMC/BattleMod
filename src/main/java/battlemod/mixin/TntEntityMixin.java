package battlemod.mixin;

import battlemod.BattleMod;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.explosion.Explosion;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TntEntity.class)
public class TntEntityMixin {
	/**
	 * @author vini2003
	 */
	@Overwrite
	private void explode() {
		TntEntity entity = (TntEntity) (Object) this;
		entity.getEntityWorld().createExplosion(entity, entity.getX(), entity.getBodyY(0.0625D), entity.getZ(), 32.0F, Explosion.DestructionType.DESTROY);
	}

	static {
		BattleMod.LOGGER.log(Level.INFO, TntEntityMixin.class.getName() + " Mixin applied.");
	}
}
