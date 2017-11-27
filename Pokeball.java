import java.util.ArrayList;

public class Pokeball {
	public static enum Pokeballs { POKEBALL, GREATBALL, ULTRABALL, MASTERBALL }
	private String name;
	private String description;
	private Pokeballs type;
	private int amount;
	
	public Pokeball(String name, String description, int amount, Pokeballs type) {
		this.type = type;
		this.name = name;
		this.description = description;
		this.amount = amount;
	}
	
	public String toString() {
		return String.format("(%d) %s: %s", this.amount, this.name, this.description);
	}
	
	public String saveString() {
		return String.format("%s;%s;%d", this.name, this.description, this.amount);
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Pokeballs getType() {
		return this.type;
	}
	
	/**
	 * Uses a pokeball on an opponent.
	 * @param target	Which pokemon to target.
	 * @param current	Current list the pokemon belongs to.
	 * @param catcher	Where we send the pokemon on a successfull capture.
	 * @return			True if nothing went wrong. False otherwise.
	 */
	public boolean use(Pokemon target, ArrayList<Pokemon> current, Trainer catcher) {
		if (this.amount > 0) {
			this.amount--;
			switch (this.type) {
				case POKEBALL:
					return target.tryCapture(current, catcher, 255);
				case GREATBALL:
					return target.tryCapture(current, catcher, 200);
				case ULTRABALL:
					return target.tryCapture(current, catcher, 150);
				case MASTERBALL:
					return target.tryCapture(current, catcher, 0);
			}
		} else {
			System.out.println("No cheating!");
		}
		return false;
	}
}