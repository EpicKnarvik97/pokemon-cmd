class MapTest {
	public static void main(String[] args) {
		Tile rock = new Tile('■', true, "NONE");
		Tile empty = new Tile(' ', false, "NONE");
		Map map = new Map(10, 10, 5, 5, empty, rock);
		Tile wallTopRight = new Tile('╗', true, "NONE");
		Tile wallTopLeft = new Tile('╔', true, "NONE");
		Tile wallBottomRight = new Tile('╝', true, "NONE");
		Tile wallBottomLeft = new Tile('╚', true, "NONE");
		Tile wallSide = new Tile('║', true, "NONE");
		Tile wallLying = new Tile('═', true, "NONE");
		Tile roof = new Tile(' ', true, "NONE");
		Tile door = new Tile('╼', false, "TELEPORT", 10, 10);
		Tile player = new Tile('P', false, "NONE");
		Tile[][] tiles = {{wallTopLeft, wallSide, wallBottomLeft}, {wallLying, roof, door}, {wallTopRight, wallSide, wallBottomRight}};
		new Structure("House", tiles);
		map.generateStructure("House", 4, 2);
		map.placeTile(player, 8, 8);
		System.out.println(map);
	}
}