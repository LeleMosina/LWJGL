package engineTester;


import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
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
		
		RawModel model = OBJLoader.loadObjModel("stall", loader);
		
													//Sara' uno
													//Perche' contiene i vao ecc, e da questo possiamo ricavarci
													//i vertici vergini che sono uguali pe rogni entity
		ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
								
		TexturedModel texturedModel = new TexturedModel(model, texture);//Sara' sempre uno, ovvero come è texturizzato il rawModel
														//Useremo questa instanza per tutte le entity
														//che devono essere questo modello
		//Ogni entity partira dallo stesso texturedModel, perche' tutte le entity
		//Dello stesso modello, useranno il VAO del modello vergine,
		//Passando alla shader la propria transformationMatrix
		Entity entity = new Entity(texturedModel, new Vector3f(0,0,-20),0,0,0,1);
		
		while(!Display.isCloseRequested()){
			//entity.increasePosition(0, 0, -0.01f);
			camera.move();
			entity.increaseRotation(0, 1, 0);
			renderer.prepare();
			shader.start(); //usa i due shader
			shader.loadViewMatrix(camera);
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
