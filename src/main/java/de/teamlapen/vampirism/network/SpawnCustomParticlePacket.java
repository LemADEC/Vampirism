package de.teamlapen.vampirism.network;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.teamlapen.vampirism.client.render.particle.FlyingBloodParticle;
import de.teamlapen.vampirism.util.Helper;
import de.teamlapen.vampirism.util.Logger;

/**
 * Packet to spawn custom particles
 * @author Maxanier
 *
 */
public class SpawnCustomParticlePacket implements IMessage {

	
	private NBTTagCompound data;
	private double posX,posY,posZ;
	private int amount;
	/**
	 * @param type 0:Flying_Blood
	 * @param data CustomData
	 */
	public SpawnCustomParticlePacket(int type,double posX,double posY,double posZ,int amount,NBTTagCompound data){
		this.data=data;
		this.data.setInteger("type", type);
		this.data.setDouble("poxX",posX);
		this.data.setDouble("posY", posY);
		this.data.setDouble("posZ", posZ);
		this.data.setInteger("amount", amount);
	}
	
	/**
	 * Dont use
	 */
	public SpawnCustomParticlePacket(){
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		data=ByteBufUtils.readTag(buf);
		posX=data.getDouble("posX");
		posY=data.getDouble("posY");
		posZ=data.getDouble("posZ");
		amount=data.getInteger("amount");
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
	}
	
	
	public static class Handler implements IMessageHandler<SpawnCustomParticlePacket, IMessage>{

		@Override
		public IMessage onMessage(SpawnCustomParticlePacket message, MessageContext ctx) {
			
			try {
				switch(message.data.getInteger("type")){
				case 0: 
					for(int i=0;i<message.amount;i++){
						EntityFX p=new FlyingBloodParticle(Minecraft.getMinecraft().theWorld,message.posX,message.posY,message.posZ,message.data);
						Minecraft.getMinecraft().effectRenderer.addEffect(p);
						
					}
					break;

				default:
					Logger.w("CustomParticlePacket", "Particle of type "+message.data.getInteger("type")+" is unknown");
					return null;
				}

			} catch (Exception e) {
				Logger.e("CustomParticlePacket", "Error",e);
			}
			return null;
			
			
		}
		
	}

}