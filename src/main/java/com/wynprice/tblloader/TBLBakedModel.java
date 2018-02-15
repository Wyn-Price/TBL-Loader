package com.wynprice.tblloader;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class TBLBakedModel implements IBakedModel
{
	
	private final List<BakedQuad> quads;
	
	private final TextureAtlasSprite sprite;
	
	public TBLBakedModel(TextureAtlasSprite sprite, List<BakedQuad> quads) 
	{
		this.quads = quads;
		this.sprite = sprite;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return side == null ? quads : Lists.newArrayList();
	}

	@Override
	public boolean isAmbientOcclusion() 
	{
		return true;
	}

	@Override
	public boolean isGui3d() 
	{
		return true;
	}

	@Override
	public boolean isBuiltInRenderer(){
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture(){
		return sprite == null ? Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel().getParticleTexture() : sprite;
	}

	@Override
	public ItemOverrideList getOverrides(){
		return ItemOverrideList.NONE;
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) 
	{
		Matrix4f matrix;
		switch (cameraTransformType) //TODO make possibly configurable?
		{
			case THIRD_PERSON_LEFT_HAND:
				matrix = makeMatrix(0.26516503, 1.1175871E-8, 0.26516506, 0.0,
						0.25612977, 0.09705714, -0.25612974, 0.15625003,
						-0.06862976, 0.36222214, 0.06862976, 0.0,
						0.0, 0.0, 0.0, 1.0);
				break;
			case THIRD_PERSON_RIGHT_HAND:
				matrix = makeMatrix(0.26516503, 1.1175871E-8, 0.26516506, 0.0,
						0.25612977, 0.09705714, -0.25612974, 0.15625003,
						-0.06862976, 0.36222214, 0.06862976, 0.0,
						0.0, 0.0, 0.0, 1.0);
				break;
			case FIRST_PERSON_LEFT_HAND:
				matrix = makeMatrix(-0.2828428, 0.0, -0.2828427, 0.0,
						0.0, 0.4, 0.0, 1.4901161E-8,
						0.2828427, 0.0, -0.2828428, 1.4901161E-8,
						0.0, 0.0, 0.0, 1.0);
				break;
			case FIRST_PERSON_RIGHT_HAND:
				matrix = makeMatrix(0.2828427, 0.0, 0.28284273, 0.0,
						0.0, 0.4, 0.0, 1.4901161E-8,
						-0.28284273, 0.0, 0.2828427, -1.4901161E-8,
						0.0, 0.0, 0.0, 1.0);
				break;
			case GUI:
				matrix = makeMatrix(-0.44194186, -9.313226E-9, -0.44194168, -2.9802322E-8,
						-0.22097084, 0.54126585, 0.22097088, 0.0,
						0.38273275, 0.3125, -0.38273287, 0.0,
						0.0, 0.0, 0.0, 1.0);
				break;
			case GROUND:
				matrix = makeMatrix(0.25, 0.0, 0.0, 0.0,
						0.0, 0.25, 0.0, 0.1875,
						0.0, 0.0, 0.25, 0.0,
						0.0, 0.0, 0.0, 1.0);
				break;
			case FIXED:
				matrix = makeMatrix(0.5, 0.0, 0.0, 0.0,
				0.0, 0.5, 0.0, 0.0,
				0.0, 0.0, 0.5, 0.0,
				0.0, 0.0, 0.0, 1.0);
				break;
			default:
				matrix = null;
				break;
		}
		return Pair.of(this, matrix);
		
	}
	
	private Matrix4f makeMatrix(
			double m00, double m01, double m02, double m03,
			double m10, double m11, double m12, double m13,
			double m20, double m21, double m22, double m23,
			double m30, double m31, double m32, double m33)
	{
		return new Matrix4f(
				(float) m00, (float) m01, (float) m02, (float) m03,
				(float) m10, (float) m11, (float) m12, (float) m13, 
				(float) m20, (float) m21, (float) m22, (float) m23,
				(float) m30, (float) m31, (float) m32, (float) m33);
	}

}
