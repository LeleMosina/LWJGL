package engineTester;


import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Render;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DisplayManager.createDisplay();
		
		//Le textureCoords devono essere nello stesso ordine dei vertices
		//i vertici dei triangoli sono in ordine anti-orario
		
		Loader loader = new Loader();
		
		StaticShader shader = new StaticShader();
		Render renderer = new Render(shader);
		Camera camera = new Camera();
		
		RawModel model = OBJLoader.loadObjModel("dragon", loader);
		
													//Sara' uno
													//Perche' contiene i vao ecc, e da questo possiamo ricavarci
													//i vertici vergini che sono uguali pe rogni entity
								
		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("stallTexture")));//Sara' sempre uno, ovvero come è texturizzato il rawModel
														//Useremo questa instanza per tutte le entity
														//che devono essere questo modello
		//Ogni entity partira dallo stesso texturedModel, perche' tutte le entity
		//Dello stesso modello, useranno il VAO del modello vergine,
		//Passando alla shader la propria transformationMatrix
		ModelTexture texture = texturedModel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		Light light = new Light(new Vector3f(0,0,-20), new Vector3f(1,1,1));
		
		Entity entity = new Entity(texturedModel, new Vector3f(0,0,-20),0,0,0,1);
		entity.increaseRotation(0, 180, 1);
		
		
		while(!Display.isCloseRequested()){
			//entity.increasePosition(0, 0, -0.01f);
			//camera.move();
			light.move();
			//entity.increaseRotation(0, 0.5f, 0);
			renderer.prepare();
			shader.start(); //usa i due shader
			
			shader.loadViewMatrix(camera);
			shader.loadLight(light);
			
			renderer.render(entity, shader); //renderizza, tenendo conto dei due shader
			shader.stop(); //non usare piu' i due shader
			//game logic
			//render
			DisplayManager.updateDisplay();
		}
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
