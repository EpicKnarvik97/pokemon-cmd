import java.util.ArrayList;
public class Item {
	public enum Target { SELF, OTHER }
	private String name;
	private String description;
	private Target target;
	private boolean alive;
	private int amount;
	
	public Item(String name, String description, int amount, Target target) {
		this.name = name;
		this.description = description;
		this.target = target;
		this.alive = true;
		this.amount = amount;
	}
	
	public Item(String name, String description, int amount, Target target, boolean alive) {
		this.name = name;
		this.description = description;
		this.target = target;
		this.alive = alive;
		this.amount = amount;
	}
	
	public String toString() {
		return String.format("(%d) %s: %s", this.amount, this.name, this.description);
	}
	
	public String saveString() {
		return String.format("%s;%s;%d;%s;%b", this.name, this.description, this.amount, this.target, this.alive);
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public Target getTarget() {
		return this.target;
	}
	
	public String getName() {
		return this.name;
	}
	
	/** Checks if an item should be used on alive or fainted pokemon. */
	public boolean needsAlive() {
		return alive;
	}
	
	/**
	 * Spends an item and does something based on the type of item.
	 * @param target	Which pokemon should the item be used on.
	 * @return			True if nothing went wrong. False otherwise.
	 */
	public boolean use(Pokemon target) {
		if (this.amount > 0) {
			String name = this.name.toLowerCase().replace(" ", "");
			switch (name) {
				case "potion":
					return potion(target, 20);
				case "superpotion":
					return potion(target, 50);
				case "hyperpotion":
					return potion(target, 200);
				case "maxpotion":
					return potion(target, -1);
				case "revive":
					if (!target.isConscious()) {
						this.amount--;
						target.revive();
						System.out.printf("%s was revived.%n", target.getName());
						return true;
					} else {
						System.out.println("You can't revive a conscious pokemon.");
						return false;
					}
				default:
					System.out.printf("Invalid item %s%n", name);
					return false;
			}
		} else {
			System.out.println("No cheating!");
			return false;
		}
	}
	
	/**
	 * Checks if a pokemon is able to, and in need of a potion. If it is, heal it.
	 * @param target	The pokemon to heal.
	 * @param amount	The amount to heal the pokemon.
	 * @return			True if nothing went wrong. False otherwise.
	 */
	private boolean potion(Pokemon target, int amount) {
		if (target.isConscious()) {
			if (target.isDamaged()) {
				this.amount--;
				int healed = target.heal(amount);
				System.out.printf("%s was healed for %d HP and now has %d HP.%n", target.getName(), healed, target.getHP());
			} else {
				System.out.printf("%s has not taken damage, and does not require a potion.%n", target.getName());
				return false;
			}
		} else {
			System.out.println("You can't heal a fainted pokemon.");
			return false;
		}
		return true;
	}
	
	/**
	 * Uses a pokeball on an opponent.
	 * @param target	Which pokemon to target.
	 * @param current	Current list the pokemon belongs to.
	 * @param catcher	Where we send the pokemon on a successfull capture.
	 * @return			True if nothing went wrong. False otherwise.
	 */
	public boolean use(Pokemon target, ArrayList<Pokemon> current, ArrayList<Pokemon> catcher) {
		if (this.amount > 0) {
			this.amount--;
			switch (this.name.toLowerCase().replace(" ", "")) {
				case "pokeball":
					return target.tryCapture(current, catcher, 255);
				case "greatball":
					return target.tryCapture(current, catcher, 200);
				case "ultraball":
					return target.tryCapture(current, catcher, 150);
				case "masterball":
					return target.tryCapture(current, catcher, 0);
				default:
					System.out.println("That item does not exist.");
			}
		} else {
			System.out.println("No cheating!");
		}
		return false;
	}
}