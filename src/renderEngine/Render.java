package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Render {

	public void prepare(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1); //setta lo sfondo base
		
		//I due metodi, puliscono i buffers, dai colori dell'ultimo frame
	}
	
	public void render(RawModel model){
		GL30.glBindVertexArray(model.getVaoID());//Il cursore si sposta sul VAO di questo modello
		GL20.glEnableVertexAttribArray(0);//Ogni VBO è come se fosse bloccato, con questo metodo
							//metodo, andiamo ad unlockare il VBO 0 , ovvero le posizioni
							//inoltre binda il VBO 0
		//rendering non ottimizzato --> GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());//disegna i vertici e li unisce, leggendoli dal VBO bindato, ovvero VBO 0
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0); //Locka, ed unbinda il VBO 0 di questo VAO
		GL30.glBindVertexArray(0);//Binda un VAO a caso, perche' ha finito
							
	}
	
}
