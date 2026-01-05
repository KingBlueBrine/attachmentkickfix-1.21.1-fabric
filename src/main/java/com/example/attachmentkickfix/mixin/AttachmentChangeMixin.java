package com.example.attachmentkickfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.example.attachmentkickfix.AttachmentKickFix;
import net.minecraft.world.World;

@Pseudo
@Mixin(targets = "net.fabricmc.fabric.impl.attachment.sync.AttachmentChange")
public abstract class AttachmentChangeMixin {

    @Inject(
        method = "tryApply",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text;empty()Lnet/minecraft/text/MutableText;"
        ),
        cancellable = true
    )
    @SuppressWarnings("unused")
    private void attachmentkickfix$skipIfMissingTarget(World world, CallbackInfo ci) {
        // If Fabric reaches this point and the target is missing,
        // it will throw and disconnect the player.
        // Cancelling here safely skips this attachment.

        AttachmentKickFix.LOGGER.warn(AttachmentKickFix.MOD_ID + ": Just recieved and parsed attachment for unknown target");
        
        ci.cancel();
    }
}







