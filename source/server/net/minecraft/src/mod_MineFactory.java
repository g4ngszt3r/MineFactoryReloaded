package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import net.minecraft.src.powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import net.minecraft.src.powercrystals.minefactoryreloaded.api.IFactoryFertilizable;
import net.minecraft.src.powercrystals.minefactoryreloaded.api.IFactoryHarvestable;
import net.minecraft.src.powercrystals.minefactoryreloaded.api.IFactoryPlantable;
import net.minecraft.src.powercrystals.minefactoryreloaded.api.IFactoryRanchable;
import net.minecraft.src.powercrystals.minefactoryreloaded.core.IMFRProxy;

public class mod_MineFactory extends BaseModMp
{	
	private static mod_MineFactory instance;
	
	public mod_MineFactory()
	{
		MineFactoryReloadedCore.Init(new ServerProxy());
		ModLoaderMp.InitModLoaderMp();
		instance = this;
	}

	@Override
	public String Version()
	{
		return MineFactoryReloadedCore.version;
	}
	
	@Override
	public void ModsLoaded()
	{
		MineFactoryReloadedCore.afterModsLoaded();
	}
	
	public static void registerPlantable(IFactoryPlantable plantable)
	{
		MineFactoryReloadedCore.registerPlantable(plantable);
	}
	
	public static void registerHarvestable(IFactoryHarvestable harvestable)
	{
		MineFactoryReloadedCore.registerHarvestable(harvestable);
	}
	
	public static void registerFertilizable(IFactoryFertilizable fertilizable)
	{
		MineFactoryReloadedCore.registerFertilizable(fertilizable);
	}
	
	public static void registerFertilizerItem(int itemId)
	{
		MineFactoryReloadedCore.registerFertilizerItem(itemId);
	}
	
	public static void registerRanchable(IFactoryRanchable ranchable)
	{
		MineFactoryReloadedCore.registerRanchable(ranchable);
	}
	
	public class ServerProxy implements IMFRProxy
	{
		@Override
		public boolean isClient(World world)
		{
			return world.singleplayerWorld;
		}

		@Override
		public boolean isServer()
		{
			return true;
		}

		@Override
		public void movePlayerToCoordinates(EntityPlayer e, double x, double y,	double z)
		{
			if(!(e instanceof EntityPlayerMP))
			{
				return;
			}
			((EntityPlayerMP)e).playerNetServerHandler.teleportTo(x, y, z, e.rotationYaw, e.rotationPitch);
		}

		@Override
		public int getBlockDamageDropped(Block block, int metadata)
		{
			return block.damageDropped(metadata);
		}

		@Override
		public int getRenderId()
		{
			return 0;
		}

		@Override
		public boolean fertilizeGiantMushroom(World world, int x, int y, int z)
		{
			int blockId = world.getBlockId(x, y, z);
			return ((BlockMushroom)Block.blocksList[blockId]).fertilizeMushroom(world, x, y, z, world.rand);
		}

		@Override
		public void fertilizeStemPlant(World world, int x, int y, int z)
		{
			int blockId = world.getBlockId(x, y, z);
			((BlockStem)Block.blocksList[blockId]).func_35066_f_(world, x, y, z);
		}

		@Override
		public String getConfigPath()
		{
			return "config/MineFactoryReloaded.cfg";
		}
		@Override
		public Packet getTileEntityPacket(TileEntity te, int[] dataInt, float[] dataFloat, String[] dataString)
		{
			return ModLoaderMp.GetTileEntityPacket(instance, te.xCoord, te.yCoord, te.zCoord, 0, dataInt, dataFloat, dataString);
		}

		@Override
		public void sendPacketToAll(Packet230ModLoader p)
		{
			ModLoaderMp.SendPacketToAll(instance, p);
		}

		@Override
		public int calcItemStackEnchantability(Random random, int i, int j, ItemStack itemstack)
		{
			return EnchantmentHelper.calcItemStackEnchantability(random, i, j, itemstack);
		}

		@Override
		public List<?> buildEnchantmentList(Random random, ItemStack itemstack, int i)
		{
			return EnchantmentHelper.buildEnchantmentList(random, itemstack, i);
		}

		@Override
		public Enchantment getEnchantment(EnchantmentData ed)
		{
			return ed.field_40494_a;
		}

		@Override
		public int getLevel(EnchantmentData ed)
		{
			return ed.field_40493_b;
		}

		@Override
		public void applyEnchantment(EnchantmentData ed, ItemStack stack)
		{
			stack.addEnchantment(getEnchantment(ed), getLevel(ed));
		}

		@Override
		public void setInLove(EntityAnimal animal, int value)
		{
			try
			{
				Field f = Class.forName("bi").getDeclaredFields()[0];
				f.setAccessible(true);
				f.set(animal, value);
			}
			catch(SecurityException e)
			{
				e.printStackTrace();
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}	
		}

		@Override
		public void setEntityToAttack(EntityCreature entity, Entity target)
		{
			entity.setTarget(target);
		}
	}
}
