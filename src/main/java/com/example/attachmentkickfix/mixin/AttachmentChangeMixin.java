package com.example.attachmentkickfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.example.attachmentkickfix.AttachmentKickFix;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.world.World;

@Pseudo
@Mixin(targets = "net.fabricmc.fabric.impl.attachment.sync.AttachmentChange")
public abstract class AttachmentChangeMixin {

    @Shadow
    public abstract Object targetInfo();

    @Invoker("getTarget")
    public static AttachmentTarget attachmentkickfix$invokeGetTarget(Object targetInfo, World world) {
        throw new AssertionError();
    }

    @Inject(
        method = "tryApply",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/fabricmc/fabric/impl/attachment/sync/AttachmentTargetInfo;getTarget(Lnet/minecraft/world/World;)Lnet/fabricmc/fabric/api/attachment/v1/AttachmentTarget;"
        ),
        cancellable = true
    )
    private void fabric$ignoreUnknownAttachmentTarget(World world, CallbackInfo ci) {

        AttachmentTarget target =
                attachmentkickfix$invokeGetTarget(this.targetInfo(), world);
        if (target == null) {
            // Silently ignore instead of kicking the player
            // Attachment will be re-sent once the entity exists
            AttachmentKickFix.LOGGER.warn(AttachmentKickFix.MOD_ID + ": Just recieved and parsed attachment for unknown target");
            
            ci.cancel();
        }
    }
}







