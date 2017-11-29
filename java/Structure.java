import java.util.ArrayList;

public class Structure {
	private static ArrayList<Structure> structures;
	private Tile[][] tiles;
	private String name;
	
	public Structure(String name, Tile[][] tiles) {
		this.name = name;
		this.tiles = tiles;
		structures.add(this);
	}
	
	public String getName() {
		return this.name;
	}
	
	public Tile[][] getTiles() {
		return this.tiles;
	}
	
	public static ArrayList<Structure> getStructures() {
		return structures;
	}
	
	public int getWidth() {
		return this.tiles.length;
	}
	
	public int getHeight() {
		int max = 0;
		for (Tile[] tile : this.tiles) {
			if (tile.length > max) {
				max = tile.length;
			}
		}
		return max;
	}
}