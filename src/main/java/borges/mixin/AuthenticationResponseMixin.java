package borges.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.response.AuthenticationResponse;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(AuthenticationResponse.class)
public class AuthenticationResponseMixin {
	/**
	 * @author vini2003
	 */
	@Overwrite(remap = false)
	public GameProfile getSelectedProfile() {
		PlayerEntity player = (PlayerEntity) (Object) this;
		return new GameProfile(player.getUuid(), "Borges " + new Random().nextInt(32));
	}
}
