public class Map {
	private Tile[][] tiles;
	
	public Map(int width, int height, int wWidth, int wHeight, Tile empty, Tile wall) {
		int fullHeight = height + (2 * wHeight);
		int fullWidth = width + (2 * wWidth);
		Tile[][] map = new Tile[fullHeight][fullWidth];
		for (int i = 0; i < fullHeight; i++) {
			for (int j = 0; j < fullWidth; j++) {
				if (i < wHeight || i >= height + wHeight || j < wWidth || j >= width + wWidth) {
					map[i][j] = wall;
				} else {
					map[i][j] = empty;
				}
			}
		}
		this.tiles = map;
	}
	
	public Tile[][] getTiles() {
		return this.tiles;
	}
	
	public void generateStructure(String name, int x, int y) {
		for (Structure structure : Structure.getStructures()) {
			if (name.equals(structure.getName())) {
				this.placeStructure(structure, x, y);
			}
		}
		System.out.println("Invalid structure name.");
	}
	
	private void placeStructure(Structure structure, int x, int y) {
		
	}
	
	public String toString() {
		Tile[][] tiles = this.getTiles();
		String str = "";
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				str += tiles[i][j].toChar();
			}
			str += "\n";
		}
		return str;
	}
}