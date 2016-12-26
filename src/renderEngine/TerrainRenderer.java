
package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

public class TerrainRenderer {
	
	private TerrainShader shader;
	public TerrainRenderer(TerrainShader shader , Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();//Connette gli id (0..4) con i 5 sample2d nel terrainFragmentShader
		shader.stop();
	}
	public void render(List<Terrain> terrains){
		for(Terrain terrain:terrains){
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0); //renderizzo
			unbindTexturedModel();
		}
	}
	
	private void prepareTerrain(Terrain terrain){//Carica i VBO e texture, per le entity che hanno questo stesso TexturedModel
		RawModel model = terrain.getModel();
		GL30.glBindVertexArray(model.getVaoID());//Il cursore si sposta sul VAO di questo modello
		GL20.glEnableVertexAttribArray(0);//Ogni VBO è come se fosse bloccato, con questo metodo
							//metodo, andiamo ad unlockare il VBO 0 , ovvero le posizioni
							//inoltre binda il VBO 0
		GL20.glEnableVertexAttribArray(1);//textureCoords
		GL20.glEnableVertexAttribArray(2);//normal
		
		bindTextures(terrain);
		
		shader.loadShineVariables(1,0);//carico i valori nello shader per questo texturedModel
		
		
		
		
		
	}
	
	private void bindTextures(Terrain terrain){
		TerrainTexturePack texturePack = terrain.getTexturePack();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0); //Significa che al primo sampler2D con id 0, bindato nel TerrainShader che e' presente nel fragmentShader voglio passare la texture sotto
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());//passo questa texture al sampler2d 0
										//l'id ad ogni uniform sampler2D viene settato nel terrainShader
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
	}
	
	private void unbindTexturedModel(){
		GL20.glDisableVertexAttribArray(0); //Locka, ed unbinda il VBO 0 di questo VAO
		GL20.glDisableVertexAttribArray(1); //textureCoords
		GL20.glDisableVertexAttribArray(2); //normal
		GL30.glBindVertexArray(0);//Binda un VAO a caso, perche' ha finito
	}
	private void loadModelMatrix(Terrain terrain){ //Carica la trasformation matrix di questa entity
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(),0,terrain.getZ()), 0,0,0,1);
		shader.loadTransformationMatrix(transformationMatrix);//Carichiamo la matrix nello shader, passandola alla uniform transformationMatrix
	}
}
