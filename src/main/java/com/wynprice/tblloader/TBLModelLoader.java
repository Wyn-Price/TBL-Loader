package com.wynprice.tblloader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class TBLModelLoader implements ICustomModelLoader
{

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) 
	{
		LOADED_LOGGED_LOCATIONS.clear();
		TBLZipHandler.LOADED_MODELS.clear();
	}
	
	private static final ArrayList<ResourceLocation> LOADED_LOGGED_LOCATIONS = new ArrayList<>();

	@Override
	public boolean accepts(ResourceLocation modelLocation) 
	{
		boolean isDirect = true;
		if(!modelLocation.getResourcePath().endsWith(".tbl"))
		{				
			try
			{
				Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(modelLocation.getResourceDomain() + ":" + modelLocation.getResourcePath() + ".tbl")).getInputStream();
			}
			catch (IOException e2) 
			{
				try
				{
					JsonParser paraser = new JsonParser();
					JsonObject json = paraser.parse(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(modelLocation.getResourceDomain() + ":" + modelLocation.getResourcePath() + ".json")).getInputStream(), StandardCharsets.UTF_8)).getAsJsonObject();
					if(json.has("parent"))
					{
						ResourceLocation loc = new ResourceLocation(json.get("parent").getAsString());
						isDirect = accepts(new ResourceLocation(loc.getResourceDomain(), "models/" + loc.getResourcePath()));
					}
					else
						isDirect = false;
				}
				catch (IOException e) 
				{
					isDirect = false;
				}
				
			}
		}
		else
		{
			try
			{
				Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(modelLocation.getResourceDomain() + ":" + modelLocation.getResourcePath())).getInputStream();
			}
			catch (IOException e2) 
			{
				isDirect = false;
			}
		}
		return isDirect;
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception 
	{
		if(!LOADED_LOGGED_LOCATIONS.contains(modelLocation)) //Stops multiple logging of the same location
		{
			TBLMod.LOGGER.info("Loading tbl model: " + modelLocation);
			LOADED_LOGGED_LOCATIONS.add(modelLocation);
		}
		return TBLZipHandler.getZippedModel(modelLocation);
	}

}
