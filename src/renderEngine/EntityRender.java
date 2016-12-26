package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

public class EntityRender {
	
	

	
	private StaticShader shader;
	
	public EntityRender(StaticShader shader, Matrix4f projectionMatrix){
		this.shader = shader;


		shader.start();//significa usa questo render
		shader.loadProjectionMatrix(projectionMatrix);//Carica la prijectionMatrix, che e' uguale per tutti i modelli
					//quindi la carica solo all'inizio
		
		shader.connectTextureUnits();//Connette l'id (0) con il sampler2d(textureSample) che sta nel fragmentShader
		shader.stop();//non usarlo piu'
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities){ 
		
		for(TexturedModel model:entities.keySet()){//in model, c'e' una chiave della matrice
													//dragone, cube, stall ecc..
			
			prepareTexturedModel(model);//preparo i VBO e texture, per le entity che andro a renderizzare
										//si fa' una volta, perche' tanto le entity che stiamo per renderizzare
										//condividono tutti lo stesso TexturedModel
			
			List<Entity> batch = entities.get(model);//prendo la lista delle entity da renderizzare(tutti dragoni, tutti stall), in base a model che e' la key della hashmap
			
			for(Entity entity:batch){//per ogni entity dello stesso tipo
				prepareInstance(entity);//preparo questa entity
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0); //renderizzo
			}
			
			unbindTexturedModel();
		}
	}
	private void prepareTexturedModel(TexturedModel texturedModel){//Carica i VBO e texture, per le entity che hanno questo stesso TexturedModel
		RawModel model = texturedModel.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());//Il cursore si sposta sul VAO di questo modello
		GL20.glEnableVertexAttribArray(0);//Ogni VBO è come se fosse bloccato, con questo metodo
							//metodo, andiamo ad unlockare il VBO 0 , ovvero le posizioni
							//inoltre binda il VBO 0
		GL20.glEnableVertexAttribArray(1);//textureCoords
		GL20.glEnableVertexAttribArray(2);//normal
		//con glEnableVerterAttribArray si caricano i VBO nei valori di in del vertexShader
		
		ModelTexture texture = texturedModel.getTexture(); //
		if(texture.isHasTrasparency()){//Disattiva il culling se questo modello e' trasparente
			MasterRenderer.disableCulling();
		}
		
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());//carico i valori nello shader per questo texturedModel
		
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());//Se per questo modelTexture, ad esempio grass, ferb dobbiamo usare un fake lighting
																	   //significa che il vettore normal di questo vertex dobbiamo considerarlo verso l'alto
																	   //perche' l'erba deve avere un'illuminazione omogenea
																	   //useFakeLighting è un bool
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);//Significa che al primo sampler2D nel fragmentShader voglio passare la texture sotto
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());//passo questa texture al sampler2d 0
		
		
	}
	private void unbindTexturedModel(){
		MasterRenderer.enableCulling(); //Rienable il culling
		GL20.glDisableVertexAttribArray(0); //Locka, ed unbinda il VBO 0 di questo VAO
		GL20.glDisableVertexAttribArray(1); //textureCoords
		GL20.glDisableVertexAttribArray(2); //normal
		GL30.glBindVertexArray(0);//Binda un VAO a caso, perche' ha finito
	}
	private void prepareInstance(Entity entity){ //Carica la trasformation matrix di questa entity
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);//Carichiamo la matrix nello shader, passandola alla uniform transformationMatrix
	}
	/*public void render(Entity entity, StaticShader shader){ VECCHIO METODO
		TexturedModel texturedModel = entity.getModel(); //restituisce il modello texturizzato
		RawModel model = texturedModel.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());//Il cursore si sposta sul VAO di questo modello
		GL20.glEnableVertexAttribArray(0);//Ogni VBO è come se fosse bloccato, con questo metodo
							//metodo, andiamo ad unlockare il VBO 0 , ovvero le posizioni
							//inoltre binda il VBO 0
		GL20.glEnableVertexAttribArray(1);//textureCoords
		GL20.glEnableVertexAttribArray(2);//normal
		
		//glEnableVertexAttribArray serve a passare i dati al vertexShader, i dati di in
		//mentre shader.loadd ecc per caricare i valori delle uniform
		//Si intende la traslazione rispetto al punto (0,0,0) quindi la sua posizione, non la traslazione dall'ultimo
		//punto in cui si trovava perche' non viene salvato
		//La rotazione è intesa dalla rot(0,0,0), quindi le componenti che deve avere ora, non di quanto deve ruotare
		//dall'ultima rotazione che aveva, perche' non viene salvata
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);//Carichiamo la matrix nello shader, passandola alla uniform transformationMatrix
		ModelTexture texture = texturedModel.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);//Esiste il multitexturing, quindi cosi' specifico quale texture voglio utilizzare
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());//VOGLIO USARE QUESTA TEXTURE, LA BINDO
		//rendering non ottimizzato --> GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());//disegna i vertici e li unisce, leggendoli dal VBO bindato, ovvero VBO 0
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0); //Locka, ed unbinda il VBO 0 di questo VAO
		GL20.glDisableVertexAttribArray(1); //textureCoords
		GL20.glDisableVertexAttribArray(2); //normal
		GL30.glBindVertexArray(0);//Binda un VAO a caso, perche' ha finito
							
	}*/
	

	
}
