package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import toolbox.Maths;

public class StaticShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	private int location_trasformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes(){
		super.bindAttribute(0, "position");//Associa alla posizione 0 del VERTEXSHADER, la var position che e' nel VERTEXSHADER
										   //poiche' piu' in la' lo useremo
		super.bindAttribute(1, "textureCoords");
		
		
	}

	@Override
	protected void getAllUniformLocations() { //Viene richiamata nel costruttore della classe super
		location_trasformationMatrix = super.getUniformLocation("trasformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		//Gia' sappiamo la posizione di "transformationMatrix" nello shader grazie a getAllUniformLocations()
		super.loadMatrix(location_trasformationMatrix, matrix);//Carica la transformation matrix, nello shader
	}
	public void loadProjectionMatrix(Matrix4f matrix){
		//Gia' sappiamo la posizione di "transformationMatrix" nello shader grazie a getAllUniformLocations()
		super.loadMatrix(location_projectionMatrix, matrix);//Carica la transformation matrix, nello shader
	}
	public void loadViewMatrix(Camera camera){
		//Gia' sappiamo la posizione di "transformationMatrix" nello shader grazie a getAllUniformLocations()
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);//Carica la transformation matrix, nello shader
	}
	

}
