package textures;

public class TerrainTexturePack {

	
	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture,
			TerrainTexture bTexture) {
		super();
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}
	public TerrainTexture getrTexture() {
		return rTexture;
	}
	public void setrTexture(TerrainTexture rTexture) {
		this.rTexture = rTexture;
	}
	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}
	public TerrainTexture getgTexture() {
		return gTexture;
	}
	public TerrainTexture getbTexture() {
		return bTexture;
	}
	
}
