public class Tile {
	private char representation;
	private boolean solid;
	private enum Action { TELEPORT, NONE, ENCOUNTER, MENUBATTLE, MENUSHOP }
	private Action action;
	private int[] teleportTarget;
	
	public Tile (char representation, boolean solid, String action) throws IllegalArgumentException {
		this.representation = representation;
		this.solid = solid;
		this.action = Action.valueOf(action.toUpperCase());
		if (this.action != Action.TELEPORT) {
			this.teleportTarget = null;
		} else {
			throw new IllegalArgumentException("A teleport tile must have a valid target.");
		}
	}
	
	public Tile (char representation, boolean solid, String action, int x, int y) throws IllegalArgumentException {
		this.representation = representation;
		this.solid = solid;
		this.action = Action.valueOf(action.toUpperCase());
		if (this.action == Action.TELEPORT) {
			int[] intArray = {x, y};
			this.teleportTarget = intArray;
		} else {
			throw new IllegalArgumentException("A non-teleport tile can't have a teleport target.");
		}
	}
	
	public char toChar() {
		return this.representation;
	}
	
	public int[] getTeleportTarget() {
		return this.teleportTarget;
	}
	
	public int onWalk() {
		if (this.solid) {
			System.out.println("You bumped against an immovable structure.");
			return -1;
		} else {
			switch (this.action) {
				case TELEPORT:
					return 1;
				case ENCOUNTER:
					return 2;
				case MENUBATTLE:
					return 3;
				case MENUSHOP:
					return 4;
			}
		}
		return 0;
	}
}