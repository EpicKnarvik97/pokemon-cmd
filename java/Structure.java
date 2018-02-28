import java.util.ArrayList;

class Structure {
	private static final ArrayList<Structure> structures = new ArrayList<>();
	private final Tile[][] tiles;
	private final String name;
	
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

	public Direction getDoorDirection() {
		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getHeight(); y++) {
				if (!tiles[x][y].isSolid()) {
					if (y == tiles[x].length - 1) {
						return Direction.SOUTH;
					} else if (y == 0) {
						return Direction.NORTH;
					} else if (x == 0) {
						return Direction.WEST;
					} else if (x == tiles.length - 1) {
						return Direction.EAST;
					}
				}
			}
		}
		return null;
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