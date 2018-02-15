package com.wynprice.tblloader;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TextureStitch 
{
	
	@SubscribeEvent
	public void onTextureStitchPre(TextureStitchEvent.Pre event)
	{
		for(TBLModel model : TBLZipHandler.LOADED_MODELS)
			model.registerTexture(event.getMap());
	}
}