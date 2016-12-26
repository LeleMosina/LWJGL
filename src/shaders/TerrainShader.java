package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class TerrainShader extends ShaderProgram{
	private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.txt";
	
	private int location_trasformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColour;
	
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes(){//PER IL VERTEX SHADER, i valori di in
		super.bindAttribute(0, "position");//Associa alla posizione 0 del VERTEXSHADER, la var position che e' nel VERTEXSHADER
										   //poiche' piu' in la' lo useremo
											//l'indice con cui bindiamo deve corrispondere all'ordine dei VBO nel VA0
											//nella funzione rendere utilizza glEnableVertexAttribArray
											//per mettere i valori in position ad esempio
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normals");
		
	}

	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}
	
	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(location_shineDamper, damper); 
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	@Override
	protected void getAllUniformLocations() { //Viene richiamata nel costruttore della classe super
		location_trasformationMatrix = super.getUniformLocation("trasformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColour = super.getUniformLocation("skyColour");
		
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_backgroundTexture, 0); //questa e' la texture con id GL_TEXTURE0 nel terrainFragmentShader
		super.loadInt(location_rTexture, 1);  //questa e' la texture con id GL_TEXTURE1 nel terrainFragmentShader
		super.loadInt(location_gTexture, 2);  //questa e' la texture con id GL_TEXTURE2 nel terrainFragmentShader
		super.loadInt(location_bTexture, 3);  //questa e' la texture con id GL_TEXTURE3 nel terrainFragmentShader
		super.loadInt(location_blendMap, 4);  //questa e' la texture con id GL_TEXTURE4 nel terrainFragmentShader
	}
	
	public void loadSkyColour(float r, float g, float b){
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
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
