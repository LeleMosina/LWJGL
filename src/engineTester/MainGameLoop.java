

package engineTester;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.EntityRender;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {
	
		// TODO Auto-generated method stub
		DisplayManager.createDisplay();
		
		//Le textureCoords devono essere nello stesso ordine dei vertices
		//i vertici dei triangoli sono in ordine anti-orario
		
		Loader loader = new Loader();
		Camera camera = new Camera();
		//RawModel model = OBJLoader.loadObjModel("dragon", loader);
		
													//Sara' uno
													//Perche' contiene i vao ecc, e da questo possiamo ricavarci
													//i vertici vergini che sono uguali pe rogni entity
								
		//TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("stallTexture")));//Sara' sempre uno, ovvero come è texturizzato il rawModel
														//Useremo questa instanza per tutte le entity
														//che devono essere questo modello
		//Ogni entity partira dallo stesso texturedModel, perche' tutte le entity
		//Dello stesso modello, useranno il VAO del modello vergine,
		//Passando alla shader la propria transformationMatrix
		//ModelTexture texture = texturedModel.getTexture();
		//texture.setShineDamper(10);
		//texture.setReflectivity(1);
		
		
		RawModel model = OBJLoader.loadObjModel("tree", loader);
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));
		
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
							new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setHasTrasparency(true);
		grass.getTexture().setUseFakeLighting(true);
		
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
				new ModelTexture(loader.loadTexture("fern")));
		fern.getTexture().setHasTrasparency(true);
		fern.getTexture().setUseFakeLighting(true);
		
		//staticModel.getTexture().setReflectivity(10);
		//staticModel.getTexture().setShineDamper(10);
		
		RawModel bunnyModel = OBJLoader.loadObjModel("stanfordBunny", loader);
		TexturedModel standfordBunny = new TexturedModel(bunnyModel, new ModelTexture(
				loader.loadTexture("white")
				));
		
		Player player = new Player(standfordBunny, new Vector3f(100,0,-50), 0,0,0, 1);
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for(int i=0;i<500;i++){
			entities.add(new Entity(staticModel, 
					new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
			
			entities.add(new Entity(grass, 
						new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,1));
			
			entities.add(new Entity(fern, 
					new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,0.6f));
			
		}
		
		Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		
		//Light light = new Light(new Vector3f(0,10,-5), new Vector3f(1,1,1));
		

		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, 
																					gTexture, bTexture);
		
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("cazzoblendMap"));
		
		
		Terrain terrain = new Terrain(-1,-1,loader,texturePack,blendMap);
		Terrain terrain2 = new Terrain(0,-1,loader, texturePack,blendMap);
		
		/*List<Entity> allCubes = new ArrayList<Entity>();
		
		for(int i=0;i<1;i++){
			float x = random.nextFloat() * 100 -50;
			float y = random.nextFloat() * 100 -50;
			float z = random.nextFloat() *-300;
			allCubes.add(new Entity(texturedModel, new Vector3f(0,0,-5),0,0,0,1));
		}*/
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()){
			//camera.move();
			player.move();
			renderer.processEntity(player);
			
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			
			for(Entity entity:entities){
				//entity.increaseRotation(0, 0, 1);
				renderer.processEntity(entity);
			}
			renderer.render(light,  camera);
			DisplayManager.updateDisplay();
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
