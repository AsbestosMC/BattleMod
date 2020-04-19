package borges.mixin;

import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(Session.class)
public class SessionMixin {
	/**
	 * @author vini2003
	 */
	@Overwrite
	public String getUsername() {
		return "Borges " + new Random().nextInt(32);
	}
}
