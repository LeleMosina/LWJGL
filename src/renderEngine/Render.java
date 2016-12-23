package renderEngine;

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
import toolbox.Maths;

public class Render {
	
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	private Matrix4f projectionMatrix;
	
	
	public Render(StaticShader shader){
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);//Carica la prijectionMatrix, che e' uguale per tutti i modelli
					//quindi la carica solo all'inizio
		shader.stop();
	}
	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST); //Serve a disegnare nel giusto ordine i vertici, capisce quali sono davanti e quali dietro
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1); //setta lo sfondo base
		
		//I due metodi, puliscono i buffers, dai colori dell'ultimo frame
	}
	
	public void render(Entity entity, StaticShader shader){
		TexturedModel texturedModel = entity.getModel(); //restituisce il modello texturizzato
		RawModel model = texturedModel.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());//Il cursore si sposta sul VAO di questo modello
		GL20.glEnableVertexAttribArray(0);//Ogni VBO è come se fosse bloccato, con questo metodo
							//metodo, andiamo ad unlockare il VBO 0 , ovvero le posizioni
							//inoltre binda il VBO 0
		GL20.glEnableVertexAttribArray(1);//textureCoords
		
		
		//Si intende la traslazione rispetto al punto (0,0,0) quindi la sua posizione, non la traslazione dall'ultimo
		//punto in cui si trovava perche' non viene salvato
		//La rotazione è intesa dalla rot(0,0,0), quindi le componenti che deve avere ora, non di quanto deve ruotare
		//dall'ultima rotazione che aveva, perche' non viene salvata
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);//Carichiamo la matrix nello shader, passandola alla uniform transformationMatrix
		
		
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);//Esiste il multitexturing, quindi cosi' specifico quale texture voglio utilizzare
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());//VOGLIO USARE QUESTA TEXTURE, LA BINDO
		//rendering non ottimizzato --> GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());//disegna i vertici e li unisce, leggendoli dal VBO bindato, ovvero VBO 0
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0); //Locka, ed unbinda il VBO 0 di questo VAO
		GL20.glDisableVertexAttribArray(1); //textureCoords
		GL30.glBindVertexArray(0);//Binda un VAO a caso, perche' ha finito
							
	}
	
	private void createProjectionMatrix(){
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
 
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
	}
	
}
