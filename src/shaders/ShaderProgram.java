package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class ShaderProgram {

	private int programID; //l'ID del programma
	private int vertexShaderID; //l'ID del vertexshader
	private int fragmentShaderID; //l'ID del fragmentshader
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(String vertexFile, String fragmentFile){
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER); //carica in RAM il vertex shader, dando l'id
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER); //carica in RAm il fragment shader, dando l'id
		programID = GL20.glCreateProgram(); //crea il programma, e da l'id
		GL20.glAttachShader(programID, vertexShaderID); //mette il vertexShader nel programma
		GL20.glAttachShader(programID, fragmentShaderID); //mette il fragmentShadernel programma
		bindAttributes();
		GL20.glLinkProgram(programID); //linka il programma
		GL20.glValidateProgram(programID); //valida il programma
		getAllUniformLocations(); //Viene ridefinita nella classe che eredita ShaderProgram
								//Cosi' dalla sottoclasse, si possono definire le uniform di cui si vuole sapere la posizione
	}
	
	protected abstract void getAllUniformLocations();//Ridefinita sottoclasse
	
	protected int getUniformLocation(String uniformName){ //Restituisce la posizione di quella uniform nel programma
		return GL20.glGetUniformLocation(programID, uniformName);
	}

	public void start(){
		GL20.glUseProgram(programID); //dice al motore di usare questo programma per il rendering quando fa' drawElements
									  //Installs a program object as part of current rendering state
	}
	
	public void stop(){
		GL20.glUseProgram(0); //non lo usa piu'
	}
	
	public void cleanUp(){
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	
	protected abstract void bindAttributes();
	
	
	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);//Associates a generic vertex attribute index with a named attribute variable
												//variableName ad esempio e' position,che e' lo stesso nome della var nel GLSL
		/*
		 * In Chapter 6, “Vertex Attributes, Vertex Arrays, and Buffer Objects,” 
		 * we go into more detail on binding attributes. For now, 
		 * note that the call to glBindAttribLocation binds the vPosition attribute declared in the vertex shader to location 0. 
		 * Later, when we specify the vertex data, this location is used to specify the position.
		 */
		
	}
	
	//Le 4 funzioni, servono a caricare i valori di tipo UNIFORM da Java al GLSL nelle uniform in RUNTIME degli shader
	protected void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}
	protected void loadInt(int location, int value){
		GL20.glUniform1i(location, value);
	}
	protected void loadVector(int location, Vector3f vector){
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	protected void loadBoolean(int location, boolean value){
		float toLoad = 0;
		if(value) toLoad = 1;
		GL20.glUniform1f(location, toLoad);
	}
	protected void loadMatrix(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location,  false, matrixBuffer);
	}
	
	private static int loadShader(String file, int type){
		  StringBuilder shaderSource = new StringBuilder();
		  try{
			   BufferedReader reader = new BufferedReader(new FileReader(file));
			   String line;
			   while((line = reader.readLine())!=null){
			    shaderSource.append(line).append("//\n");
			   }
			   reader.close();
		  }catch(IOException e){
			   e.printStackTrace();
			   System.exit(-1);
		  }
		  int shaderID = GL20.glCreateShader(type);
		  GL20.glShaderSource(shaderID, shaderSource);
		  GL20.glCompileShader(shaderID);
		  if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS ) == GL11.GL_FALSE){
			   System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			   System.err.println("Could not compile shader!");
			   System.exit(-1);
		  }
		  return shaderID;
	 }
	
}
