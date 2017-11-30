public class Test {
	public static void main(String[] args) {
		Tile rock = new Tile('#', true, "NONE");
		Tile empty = new Tile(' ', false, "NONE");
		Map map = new Map(50, 5, 5, 5, empty, rock);
		Tile wall = new Tile('=', true, "NONE");
		Tile door = new Tile('_', false, "TELEPORT", 0, 0);
		Tile[][] tiles = {{wall, wall, wall}, {wall, wall, wall}, {wall, door, wall}};
		Structure structure = new Structure("House", tiles);
		map.generateStructure("House", 10, 10);
		System.out.println(map);
	}
}