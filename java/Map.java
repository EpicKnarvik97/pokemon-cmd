class Map {
	private final Tile[][] tiles;
	private int fullWidth;
	private int fullHeight;

	public Map(int width, int height, int wWidth, int wHeight, Tile empty, Tile wall) {
		this.fullHeight = height + (2 * wHeight);
		this.fullWidth = width + (2 * wWidth);
		Tile[][] map = new Tile[this.fullWidth][this.fullHeight];
		for (int i = 0; i < this.fullWidth; i++) {
			for (int j = 0; j < this.fullHeight; j++) {
				if (i < wWidth || i >= width + wWidth || j < wHeight || j >= height + wHeight) {
					map[i][j] = wall;
				} else {
					map[i][j] = empty;
				}
			}
		}
		this.tiles = map;
	}
	
	private Tile[][] getTiles() {
		return this.tiles;
	}
	
	public void generateStructure(String name, int x, int y) throws IllegalArgumentException {
		for (Structure structure : Structure.getStructures()) {
			if (name.equals(structure.getName())) {
				this.placeStructure(structure, x, y);
				return;
			}
		}
		throw new IllegalArgumentException("Invalid structure name.");
	}
	
	private void placeStructure(Structure structure, int x, int y) throws IllegalArgumentException {
		if (x < 0 || y < 0 || this.fullWidth < x + structure.getWidth() || this.fullHeight < y + structure.getHeight()) {
			throw new IllegalArgumentException("The structure is misplaced or does not fit.");
		}
		Tile[][] tiles = structure.getTiles();
		for (int i = 0; i < structure.getWidth(); i++) {
			for (int j = 0; j < structure.getHeight(); j++) {
				if (!tiles[i][j].isSolid()) {
					switch (structure.getDoorDirection()) {
						case NORTH:
							if (this.tiles[x+i][y+j-1].isSolid()) {
								throw new IllegalArgumentException("A structure is blocked");
							}
							break;
						case SOUTH:
							if (this.tiles[x+i][y+j+1].isSolid()) {
								throw new IllegalArgumentException("A structure is blocked");
							}
							break;
						case WEST:
							if (this.tiles[x+i-1][y+j].isSolid()) {
								throw new IllegalArgumentException("A structure is blocked");
							}
							break;
						case EAST:
							if (this.tiles[x+i+1][y+j].isSolid()) {
								throw new IllegalArgumentException("A structure is blocked");
							}
					}
				}
				this.tiles[x+i][y+j] =  tiles[i][j];
			}
		}
	}

	public void placeTile(Tile tile, int x, int y) {
		if (x < 0 || y < 0 || x >= this.tiles.length || y >= this.tiles[0].length) {
			throw new IllegalArgumentException("Invalid tile position");
		}
		this.tiles[x][y] = tile;
	}
	
	public String toString() {
		Tile[][] tiles = this.getTiles();
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				str.append(tiles[j][i].toChar());
			}
			str.append("\n");
		}
		return str.toString();
	}
}