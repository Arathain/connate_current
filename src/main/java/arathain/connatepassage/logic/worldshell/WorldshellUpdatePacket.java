package arathain.connatepassage.logic.worldshell;

import arathain.connatepassage.ConnatePassage;
import arathain.connatepassage.content.cca.ConnateWorldComponents;
import arathain.connatepassage.content.cca.WorldshellComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.List;

public class WorldshellUpdatePacket {
	public static final Identifier ID = ConnatePassage.id("update_worldshells");

	public static void send(List<ServerPlayerEntity> players, List<Worldshell> shells) {
		if(shells.size() > 0) {
			PacketByteBuf buf = PacketByteBufs.create();

			for (int i = 0; i < shells.size(); i++) {
				buf.writeNbt(shells.get(i).writeUpdateNbt(new NbtCompound()));
				buf.writeInt(i);
				ServerPlayNetworking.send(players, ID, buf);
			}
		}
	}

	public static void apply(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf payload, PacketSender responseSender) {
		WorldshellComponent c = handler.getWorld().getComponent(ConnateWorldComponents.WORLDSHELLS);
		if(c.getWorldshells().size() > 0){
			NbtCompound toRead = payload.readUnlimitedNbt();
			int i = payload.readInt();
			c.getWorldshells().get(i).readUpdateNbt(toRead);
		}
	}
}
