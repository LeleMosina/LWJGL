package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class StaticShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	private int location_trasformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	private int location_skyColour;
	
	private int location_textureSample;
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes(){//PER IL VERTEX SHADER, i valori di in, per gli shader indicati sopra
		super.bindAttribute(0, "position");//Associa alla posizione 0 del VERTEXSHADER, la var position che e' nel VERTEXSHADER
										   //poiche' piu' in la' lo useremo
											//l'indice con cui bindiamo deve corrispondere all'ordine dei VBO nel VA0
											//nella funzione rendere utilizza glEnableVertexAttribArray
											//per mettere i valori in position ad esempio
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normals");
		
	}

	public void loadLight(Light light){//Carica i valori della luce nelle uniform
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}
	
	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	@Override
	protected void getAllUniformLocations() { //Viene richiamata nel costruttore della classe super, serve per trovare la posizione delle uniform negli shader
											//ovvero dove caricare il valore per quella uniform
		location_trasformationMatrix = super.getUniformLocation("trasformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_skyColour = super.getUniformLocation("skyColour");
		
		location_textureSample = super.getUniformLocation("textureSample");
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_textureSample, 0); //specifico qual'e' la texture con id GL_TEXTURE0
	}
	
	public void loadSkyColour(float r, float g, float b){
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
	}
	public void loadFakeLightingVariable(boolean useFake){
		super.loadBoolean(location_useFakeLighting, useFake);
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
		//System.out.println(viewMatrix.toString());
		super.loadMatrix(location_viewMatrix, viewMatrix);//Carica la transformation matrix, nello shader
	}
	

}
