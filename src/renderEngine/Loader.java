package renderEngine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

//Carica i RawModel nella VAO del modello
public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	
	public RawModel loadToVAO(float[] positions, int indices[]){
		//positions, è l'insieme di tutte le coordinate di tutti i vertici del modello
		int vaoID = createVAO(); //Sta scritto sotto, binda il VAO
		bindIndicesBuffer(indices);//
		//createVAO, ha creato e il cursore è bindato il VAO per questo modello, return l'ID
		storeDataInAttributeList(0, positions); //salva alla posizione 0 del VAO, il VBO 0(positions)
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	public void cleanUp(){
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos){
			GL15.glDeleteBuffers(vbo);
		}
	}
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays(); //Genera un array di vertici, assegnandogli un ID univoco, e lo return
						//da notare che è Arrays, quindi yun array di array, ovvero un array di VBO
		vaos.add(vaoID);//lo aggiungo alla lista, per poterlo eliminare dopo
		GL30.glBindVertexArray(vaoID); //Il cursore BINDA il VAO di questo modello		
		return vaoID; //ritorna il l'id VAO
	}
	private void storeDataInAttributeList(int attributeNumber, float[] data){
		int vboID = GL15.glGenBuffers(); //Crea un buffer, e ritorna ID
		vbos.add(vboID);//lo aggiungo alla lista vbo, per poterlo eliminare dopo
		FloatBuffer buffer = storeDataInFloatBuffer(data); //Converte array float, in buffer float
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);//Il cursore BINDA la locazione all'id vboID(vbo)
														//il vbo per ora e' solo in ram, non nel VAO
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);//scrive il buffer nella locazione bindata sopra(VBO)
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0,0);//sposta il vbo bindato sopra, nella 
									//riga con indice attributeNumber, del VAO bindato in loadToVAO
									//3 perche', ogni vertice ha 3 coordinate, 0,0 perche' non abbiamo dati tra le le varie tuple di coordinate (x,y,z) (x1,y1,z1)
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);//Seleziona un VBO inesistente perche' ha finito
	}
	private void unbindVAO(){
		GL30.glBindVertexArray(0);//il cursore BINDA un vao inesistente
	}
	
	private void bindIndicesBuffer(int[] indices){//LEGGERE ASSOLUTAMENTE
		
		int vboID = GL15.glGenBuffers(); //crea un'array(vbo)
		vbos.add(vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID); //BINDA, il buffer e lo mette nella VAO, ma non tra i VBO, in automatico
																//succede solo con GL_ELEMENT_ARRAY_BUFFER, non con GL_ARRAY_BUFFER
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,  buffer, GL15.GL_STATIC_DRAW);
		
		//COSA STRANA, in storeDataInAttributeList, lui setta con glVertexAttribPointer
		//in quale parte del VAO il vbo deve essere salvato, qui no
		
		//TROVATO
		
		/*According to the OpenGL wiki,  
		 * unlike a GL_ARRAY_BUFFER, when you call glBindBuffer on a GL_ELEMENT_ARRAY_BUFFER, 
		 * you are actually binding that element array to the VAO, whereas when you call glBindBuffer on a GL_ARRAY_BUFFER, 
		 * you are only telling OpenGL to use that buffer when calling 
		*/
		
		
		/*
		 * In pratica chiamando glBindBuffer su un GL_ARRAY_BUFFER, dici solo a OpenGL, dove sta questo buffer in memoria
		 * e poi per dire in che parte della VAO bindata copiare quel buffer, devi usare glVertexAttribPointer(posinVAO),
		 * , perche' e' un VBO, quindi va nella attribute list (VAO), perche' ci salvi solo i vertici, colori, ecc
		 * 
		 * Quando fai glBindBuffer su GL_ELEMENT_ARRAY_BUFFER, in automatico te lo mette nella VAO bindata, poiche non e' un VBO ma un IBO
		 * quindi non va' salvata nella attribute list(VAO), poiche' tu stai dicendo al VAO come disegnare i vertici, quindi non p un attributo dei vertici e non va nella VAO
		 */
	}
	
	private IntBuffer storeDataInIntBuffer(int data[]){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float data[]){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
