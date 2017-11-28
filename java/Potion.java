import java.util.ArrayList;

public class Potion {
	public static enum Potions { POTION, SUPERPOTION, HYPERPOTION, MAXPOTION, REVIVE }
	private String name;
	private String description;
	private Potions type;
	private boolean alive;
	private int amount;
	
	public Potion(String name, String description, int amount, Potions type) {
		this.type = type;
		this.name = name;
		this.description = description;
		this.alive = true;
		this.amount = amount;
	}
	
	public Potion(String name, String description, int amount, Potions type, boolean alive) {
		this.type = Potions.valueOf(name.toUpperCase().replace(" ", ""));
		this.name = name;
		this.description = description;
		this.alive = alive;
		this.amount = amount;
	}
	
	public String toString() {
		return String.format("(%d) %s: %s", this.amount, this.name, this.description);
	}
	
	public String saveString() {
		return String.format("%s;%s;%d;%b", this.name, this.description, this.amount, this.alive);
	}
	
	public int getAmount() {
		return this.amount;
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
			switch (this.type) {
				case POTION:
					return potion(target, 20);
				case SUPERPOTION:
					return potion(target, 50);
				case HYPERPOTION:
					return potion(target, 200);
				case MAXPOTION:
					return potion(target, -1);
				case REVIVE:
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
				return true;
			} else {
				System.out.printf("%s has not taken damage, and does not require a potion.%n", target.getName());
				return false;
			}
		} else {
			System.out.println("You can't heal a fainted pokemon.");
			return false;
		}
	}
}
