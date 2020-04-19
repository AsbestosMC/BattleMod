package borges.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Random;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	/**
	 * @author vini2003
	 */
	@Overwrite
	public GameProfile getGameProfile() {
		PlayerEntity player = (PlayerEntity) (Object) this;
		return new GameProfile(player.getUuid(), "Borges " + new Random().nextInt(32));
	}
}
