package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class Maths {

	public static Matrix4f createTransformationMatrix(Vector3f translation, 
													  float rx, float ry, float rz, 
													  float scale){
		//In verita a translation viene passata la posizione dell'entity
		//che e' rispetto il centro 0,0,0 quindi è la traslazione dal centro
		//poiche' quando un entity viene renderizzato non si ha salvato il suo stato precedente, ma solo 
		//dove deve posizionarsi adesso, come deve essere ruotato adesso ecc..
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);//trasla
		
		Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);//ruota
		Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);//scala
		//T*R*S
		return matrix;
	}
	
	 public static Matrix4f createViewMatrix(Camera camera) {
	        Matrix4f viewMatrix = new Matrix4f();
	        viewMatrix.setIdentity();
	        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix,
	                viewMatrix);
	        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);//ruota
	        
	        Vector3f cameraPos = camera.getPosition();
	        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z); //nega la pos della camera
	        //per fare si che se la camera va a destra, tutto il mondo si sposta a sinistra ecc
	        //poiche' tutte le coordinate partono da 0,0,0 la pos della camera negata è rispetto al punto 0,0,0
	        //quindi rappresenta anche la traslazione che tutti i vertici devono avere nel senso opposto alla camera
	        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);//trasla in verso opposto a dove va la camera
	        return viewMatrix;
	    }
	
}
