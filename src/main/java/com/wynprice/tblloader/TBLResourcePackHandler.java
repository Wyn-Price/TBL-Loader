package com.wynprice.tblloader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class TBLResourcePackHandler implements IResourcePack
{

	public static final TBLResourcePackHandler INSTANCE = new TBLResourcePackHandler();
	
	private final DefaultResourcePack resourcePack;
	
	public TBLResourcePackHandler() 
	{
		this.resourcePack = Minecraft.getMinecraft().mcDefaultResourcePack;
	}
	
	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException 
	{
		return TBLImageLocation.hasStream(location) ? TBLImageLocation.getStream(location) : resourcePack.getInputStream(location);
	}

	@Override
	public boolean resourceExists(ResourceLocation location) 
	{
		return resourcePack.resourceExists(location) || TBLImageLocation.hasStream(location);
	}

	@Override
	public Set<String> getResourceDomains() 
	{
		return resourcePack.getResourceDomains();
	}

	@Override
	public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer,
			String metadataSectionName) throws IOException 
	{
		return resourcePack.getPackMetadata(metadataSerializer, metadataSectionName);
	}

	@Override
	public BufferedImage getPackImage() throws IOException //Shouldn't be called
	{
		return resourcePack.getPackImage();
	}

	@Override
	public String getPackName() 
	{
		return "TBL Loader";
	}

}
