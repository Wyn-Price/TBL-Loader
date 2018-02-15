package com.wynprice.tblloader;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;

public class TBLZipHandler 
{
	
	
	private static final PrintStream CHARACTER_STREAM = new PrintStream(new OutputStream() {
		
		@Override
		public void write(int b) throws IOException 
		{
			System.out.print((char)b);
		}
		
	});
	
	public static final ArrayList<TBLModel> LOADED_MODELS = new ArrayList<>();
	
	public static TBLModel getZippedModel(ResourceLocation location)
	{
		String errorFileName = "";
		InputStream stream = null;
		if(!location.getResourcePath().endsWith(".tbl"))
		{
			try
			{
				for(ResourcePackRepository.Entry entry : Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntries())
					try
					{
						stream = entry.getResourcePack().getInputStream(new ResourceLocation(location.toString() + ".tbl"));
					}
					catch (FileNotFoundException e) 
					{
						continue;
					}
				if(stream == null)
					stream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(location.toString() + ".tbl")).getInputStream();

			}
			catch (IOException e2) 
			{
				errorFileName = location.toString() + ".tbl";
			}
		}
		else
			try
			{
				for(ResourcePackRepository.Entry entry : Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntries())
					try
					{
						stream = entry.getResourcePack().getInputStream(location);
					}
					catch (FileNotFoundException e) 
					{
						continue;
					}
				if(stream == null)
					stream = Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
			}
			catch (IOException e2) 
			{
				errorFileName = location.toString();
			}
		
		if(stream == null) //Possible child
		{
			try
			{
				JsonParser paraser = new JsonParser();
				JsonObject json = paraser.parse(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(location.getResourceDomain() + ":" + location.getResourcePath() + ".json")).getInputStream(), StandardCharsets.UTF_8)).getAsJsonObject();
				if(json.has("parent"))
				{
					ResourceLocation loc = new ResourceLocation(json.get("parent").getAsString());
					return getZippedModel(new ResourceLocation(loc.getResourceDomain(), "models/" + loc.getResourcePath()));
				}
			}
			catch (JsonSyntaxException e) //If not a json then just ignore it. 
			{
				;
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		if(stream == null)
		{
			new FileNotFoundException("Could not find file " + errorFileName)
				.printStackTrace(CHARACTER_STREAM);
			return null;
		}
		

		ZipInputStream zipStream = new ZipInputStream(stream);
		try
		{
			ZipEntry entry = zipStream.getNextEntry();
			
			TBLModel model = null;
			BufferedImage texture = null; 
			
			while(entry != null)
			{
				if(entry.getName().split("\\.")[entry.getName().split("\\.").length - 1].equals("json")) //could be renamed
				{
					String out = "";
					int len = 0;
					while(len != -1)
					{
						len = zipStream.read();
						out += (char)len;
					}
					
					model = TBLRegistry.getModel(new StringReader(out));
				}
				else if(entry.getName().split("\\.")[entry.getName().split("\\.").length - 1].equals("png"))
				{
					ArrayList<Byte> byteList = new ArrayList<>();
					int len = 0;
					while(len != -1)
					{
						len = zipStream.read();
						byteList.add((byte) len);
					}
					byte[] abyte = new byte[byteList.size()];
					for(int i = 0; i < abyte.length; i++)
						abyte[i] = byteList.get(i);
					
					texture = ImageIO.read(new ByteArrayInputStream(abyte));
				}
				entry = zipStream.getNextEntry();
			}
			
			model.setImage(texture);
			
			LOADED_MODELS.add(model);
			
			return model;
			
		}
		catch (Exception e) 
		{
			CHARACTER_STREAM.println("Could not load file " + location.toString() + " for reason " + e.getMessage());
			e.printStackTrace(CHARACTER_STREAM);
		}
		return null;

	}
}
