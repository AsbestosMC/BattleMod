package borges.mixin;

import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(GameProfile.class)
public class GameProfileMixin {
	/**
	 * @author vini2003
	 */
	@Overwrite(remap = false)
	public String getName() {
		return "Borges " + new Random().nextInt(32);
	}
}
