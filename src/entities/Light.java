package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Light {

	private Vector3f position;
	private Vector3f colour;
	public Light(Vector3f position, Vector3f colour) {
		super();
		this.position = position;
		this.colour = colour;
	}
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public Vector3f getColour() {
		return colour;
	}
	public void setColour(Vector3f colour) {
		this.colour = colour;
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
}
