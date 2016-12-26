package renderEngine;

import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;
import models.TexturedModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
public class MasterRenderer {

	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	
	private static final float RED = 0.5f;
	private static final float GREEN = 0.5f;
	private static final float BLUE = 0.5f;
	
	private Matrix4f projectionMatrix;
	
	private EntityRender renderer = null;
	private StaticShader shader = new StaticShader();
	
	private TerrainRenderer terrainRenderer = null;
	private TerrainShader terrainShader = new TerrainShader();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public MasterRenderer(){
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRender(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}
	
	
	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST); //Serve a disegnare nel giusto ordine i vertici, capisce quali sono davanti e quali dietro
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1); //setta lo sfondo base
		
		//I due metodi, puliscono i buffers, dai colori dell'ultimo frame
	}
	
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE); //servono per non renderizzare il modello all'interno cosi' da risparmiare gpu
		GL11.glCullFace(GL11.GL_BACK);
	}
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render(Light sun, Camera camera){ //E' il render piu' ottimizzato
							/*Con il vecchio render se volevamo disegnare 200 dragoni uguali, veniva fatto il bind e unbind
							 * delle texture, e dei VBO 200 volte, quindi una per ogni dragone, pero' tutti e 200 utilizzano le stesse cose
							 * quindi conviene creare questa nuova classe, che funziona caricando tutti i modelli dello stesso tipo a blocchi
							 * prima tutti i dragoni poi tutti i cubi ecc..
							 * cosi' il bind unbind, per disegnare tutti i dragoni che usano gli stessi VBO, texture ecc
							 * viene fatto una volta
							 * 
							 * binda texture, enable VBO0,VBO1,VBO2 del TexturedModel dragon
							 * 
							 * for 200 volte
							 * 		carica Trasformation matrix dragonex
							 * 		renderizza
							 *
							 *unbinda tutto
							 * 
							 */
		
					/*Nella classe infatti e' presente una HashMap, dove come chiave ha un TexturedModel, quindi cubo, dragone, stall
					 * e come valore ha un ArrayList di Entity, quindi ad esempio alla chiave dragon, ci sono 100 entity di tipo dragone
					 * quindi si renderizzano tutte le liste delle chiavi sequenzialmente
					 * prima tutti i dragoni, poi cubi ecc
					 * cosi' per ogni tipo di Modello diverso(dragon, stall, cube)
					 * il bind della texture di quel modello, e l'enable dei VBO viene fatto una sola volta
					 */
		
		
		prepare();
		
		shader.start();//utilizza questo shader
		
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		
		renderer.render(entities);//renderizza tutta la hashmap
		
		shader.stop();//stop shader
		
		
		terrainShader.start();//utilizza questo shader
		
		shader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		
		terrainRenderer.render(terrains);
		
		terrainShader.stop();//stop shader
		
		terrains.clear();
		entities.clear(); //pulisce la HashMap, che viene ricaricata ad ogni ciclo while nel MainGameLoop
	}
	
	
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity){ //Aggiunge ogni entity alla HashMap, in particolare
										      //aggiunge questa entity nella lista, appartenente alla chiave nella hashmap
											  //che corrisponde al texturedModel di questa entity, quindi se e' un dragone, uno stall, un cubo ecc..
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
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
