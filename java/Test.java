public class Test {
	public static void main(String[] args) {
		Tile wall = new Tile('#', true, "NONE");
		Tile empty = new Tile('+', false, "NONE");
		Map map = new Map(50, 5, 5, 5, empty, wall);
		System.out.println(map);
	}
}