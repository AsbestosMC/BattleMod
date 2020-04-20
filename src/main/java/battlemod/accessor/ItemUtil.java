package battlemod.accessor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

public class ItemUtil {
	public static HitResult rayTrace(World world, PlayerEntity player, RayTraceContext.FluidHandling fluidHandling) {
		float f = player.pitch;
		float g = player.yaw;
		Vec3d vec3d = player.getCameraPosVec(1.0F);
		float h = MathHelper.cos(-g * 0.017453292F - 3.1415927F);
		float i = MathHelper.sin(-g * 0.017453292F - 3.1415927F);
		float j = -MathHelper.cos(-f * 0.017453292F);
		float k = MathHelper.sin(-f * 0.017453292F);
		float l = i * j;
		float n = h * j;
		Vec3d vec3d2 = vec3d.add((double)l * 5.0D, (double)k * 5.0D, (double)n * 5.0D);
		return world.rayTrace(new RayTraceContext(vec3d, vec3d2, RayTraceContext.ShapeType.OUTLINE, fluidHandling, player));
	}
}
