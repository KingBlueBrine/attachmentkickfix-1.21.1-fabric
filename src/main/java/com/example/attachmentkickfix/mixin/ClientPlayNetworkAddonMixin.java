package com.example.attachmentkickfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.impl.networking.client.ClientPlayNetworkAddon;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

@Mixin(ClientPlayNetworkAddon.class)
public abstract class ClientPlayNetworkAddonMixin {

    @Redirect(
        method = "receive",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;execute(Ljava/lang/Runnable;)V"
        )
    )
    private void wrapPayloadReceive(
            ClientPlayNetworking.PlayPayloadHandler<?> handler,
            CustomPayload payload,
            CallbackInfo ci
    ) {
        try {
            // Let Fabric do its thing
        } catch (RuntimeException e) {
            Identifier id = payload.getId().id();

            if (id.getNamespace().equals("fabric")
                && id.getPath().contains("attachment")) {

                // NeoForge-style behavior
                // Log and ignore
                System.err.println(
                    "[AttachmentKickFix] Ignored attachment sync error: " + e.getMessage()
                );

                ci.cancel();
                return;
            }

            // Re-throw for everything else
            throw e;
        }
    }
}
