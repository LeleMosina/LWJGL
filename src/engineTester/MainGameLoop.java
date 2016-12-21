package engineTester;


import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Render;

public class MainGameLoop {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		Render renderer = new Render();
		
		float[] vertices = { -0.5f,0.5f,0,
							-0.5f,-0.5f,0,
							0.5f,-0.5f, 0,
							0.5f,0.5f,0
		};
		int indices[] = {
				0,1,3,
				3,1,2
		};
		//i vertici dei triangoli sono in ordine anti-orario
		RawModel model1 = loader.loadToVAO(vertices, indices);
		
		while(!Display.isCloseRequested()){
			renderer.prepare();
			renderer.render(model1);
			//game logic
			//render
			DisplayManager.updateDisplay();
		}
		DisplayManager.closeDisplay();
	}

}
