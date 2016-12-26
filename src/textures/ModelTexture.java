package textures;

public class ModelTexture {
	
	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTrasparency = false;
	private boolean useFakeLighting = false;
	
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public ModelTexture(int id){
		this.textureID = id;
	}
	
	public int getID(){
		return this.textureID;
	}

	public boolean isHasTrasparency() {
		return hasTrasparency;
	}

	public void setHasTrasparency(boolean hasTrasparency) {
		this.hasTrasparency = hasTrasparency;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	
}
