package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0,0,0);
	private float pitch;
	private float yaw;
	private float roll;
	public Camera() {
	}
	public void move(){
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.position.z-=0.02;	
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.position.z+=0.02;	
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			this.position.x+=0.02;	
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			this.position.x-=0.02;	
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	
	
}