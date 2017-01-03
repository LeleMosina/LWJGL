package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;

public class Player extends Entity{

	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
	}
	public void move(){
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();//Sarebbe il vettore velocita, modulo
		//Sappiamo l'angolo che ha sulla y
		//Dobbiamo calcolarci in base a questa velocita, di quanto si spostera' sulla x, ruotando su y
		
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		
		//Sappiamo l'angolo che ha sulla z
		
		//Sta tutto spiegato nel file player.png
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotZ())));
		
		//il concetto è dobbiamo andare dove stiamo guardando, dove stiamo guardando lo capiamo in base a rotX, rotY, e rotZ
	
		super.increasePosition(dx, 0, dz);
	}
	
	private void checkInputs(){
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.currentSpeed = RUN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.currentSpeed = -RUN_SPEED;
		}else{
			this.currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			this.currentTurnSpeed = -TURN_SPEED; //clocwise
		}else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			this.currentTurnSpeed = TURN_SPEED;
		}else{
			this.currentTurnSpeed = 0;
		}

	}
}
