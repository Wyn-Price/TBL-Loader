package com.wynprice.tblloader;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=TBLMod.MODID, name=TBLMod.MODNAME, version=TBLMod.VERSION, clientSideOnly=true)
public class TBLMod 
{
	public static final String MODID = "tblloader";
	public static final String MODNAME = "TBL Loader";
	public static final String VERSION = "0.1";
	public static final Logger LOGGER  = LogManager.getLogger(MODID);
		
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new TextureStitch());
		ModelLoaderRegistry.registerLoader(new TBLModelLoader());

		((List<IResourcePack>) ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "defaultResourcePacks", "field_110449_ao")).add(TBLResourcePackHandler.INSTANCE);
		
        ((List<IResourcePack>) ObfuscationReflectionHelper.getPrivateValue
        	(
        		FallbackResourceManager.class,
        		((Map<String, FallbackResourceManager>) ObfuscationReflectionHelper.getPrivateValue
        				(
        						SimpleReloadableResourceManager.class,
        						(SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager(),
        						"domainResourceManagers",
        						"field_110548"+"_a"
        				)
        		).get("minecraft"),
        		"resourcePacks",
        		"field_110540"+"_a")
        	).add(TBLResourcePackHandler.INSTANCE);
	}
}
