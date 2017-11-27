import java.util.Random;
import java.util.ArrayList;

public class Pokemon {
	private String name;
	private int healthPoints;
	private int maxHealthPoints;
	private int strength;
	private double criticalChance;
	private Random random;
	private int catchRate;
	private int exp;
	private int level;
	private int fleeCount;
	
	public Pokemon(String name) {
		this.random = new Random();
		this.name = name;
		this.healthPoints = 30 + random.nextInt(70);
		this.maxHealthPoints = this.healthPoints;
		this.criticalChance = Math.abs(0.1 * random.nextGaussian());
		this.catchRate = (int)(Math.random() * 256);
		this.exp = 0;
		this.level = (int)(Math.abs(2 * random.nextGaussian()) + 1);
		this.strength = random.nextInt(7) + this.level;
		this.fleeCount = 0;
	}
	
	public Pokemon(String name, int healthPoints, int maxHealthPoints, int strength, double criticalChance, int catchRate, int exp, int level) {
		this.random = new Random();
		this.name = name;
		this.healthPoints = healthPoints;
		this.maxHealthPoints = maxHealthPoints;
		this.criticalChance = criticalChance;
		this.catchRate = catchRate;
		this.exp = exp;
		this.level = level;
		this.strength = strength;
		this.fleeCount = 0;
	}

	public String toString() {
		return String.format("Level %d %s HP: (%d/%d) STR: %d CHC: %.0f%%", this.level, this.name, this.healthPoints, this.maxHealthPoints, this.strength, (this.criticalChance*100));
	}
	
	/**
	 * Returns a string containing all the pokemon's data.
	 */
	public String saveString() {
		return String.format("%s;%d;%d;%d;%f;%d;%d;%d", this.name, this.healthPoints, this.maxHealthPoints, this.strength, this.criticalChance, this.catchRate, this.exp, this.level);
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getHP() {
		return this.healthPoints;
	}
	
	public boolean equals(Pokemon target) {
		return target == this;
	}
	
	/**
	 * Heals a pokemon.
	 * @param amount	How many hitpoints to heal.
	 * @return			The exact amount of hitpoints healed.
	 */
	public int heal(int amount) {
		if (amount < 0) {
			amount = this.maxHealthPoints - this.healthPoints;
		} else {
			amount = Math.min(this.maxHealthPoints - this.healthPoints, amount);
		}
		this.healthPoints += amount;
		return amount;
	}
	
	/**
	 * Checks if a pokemon has not taken any damage or is fully healed.
	 * @param return	True if health is full. False otherwise.
	 */
	public boolean isDamaged() {
		return this.healthPoints < this.maxHealthPoints;
	}
	
	/**
	 * Tries to catch a pokemon.
	 * @param current	The list where the pokemon currently belongs.
	 * @param catcher	The list to send the pokemon on successfull capture.
	 * @return			True on successfull capture. False otherwise.
	 */
	public boolean tryCapture(ArrayList<Pokemon> current, Trainer catcher, int range) {
		if (range == 0) {
			this.capture(current, catcher);
			System.out.printf("%s was caught.%n", this.name);
			return true;
		}
		if ((int)(Math.random() * (range + 1)) < Math.max((3 * this.maxHealthPoints - 2 * this.healthPoints) * this.catchRate / (3 * this.maxHealthPoints), 1)) {
			this.capture(current, catcher);
			System.out.printf("%s was caught.%n", this.name);
			return true;
		} else {
			System.out.printf("%s escaped from the pokeball.%n", this.name);
			return false;
		}
	}
	
	/**
	 * Captures a wild pokemon.
	 * @param current	The pokemon list the pokemon belongs to.
	 * @param catcher	The pokemon list of the trainer.
	 */
	private void capture(ArrayList<Pokemon> current, Trainer trainer) {
		trainer.addPokemon(this);
		for (int i = 0; i < current.size(); i++) {
			if (current.get(i) == this) {
				current.remove(i);
			}
		}
	}
	
	/** Restores the health of a fainted pokemon. */
	public void revive() {
		this.healthPoints = this.maxHealthPoints;
	}
	
	/**
	 * Checks if the pokemon has any HP left.
	 * @return	True if any HP is left. False otherwise.
	 */
	public boolean isConscious() {
		return this.healthPoints > 0;
	}
	
	/**
	 * Damages a pokemon.
	 * @param	How many hitpoints are to be deducted.
	 */
	public void damage(int damageTaken) {
		this.healthPoints = Math.max(this.healthPoints -= damageTaken, 0);
		System.out.printf("%s takes %d damage and is left with %d/%d HP%n", this.name, damageTaken, this.healthPoints, this.maxHealthPoints);
	}
	
	/**
	 * Gives a pokemon exp after each successfull battle. Also handles leveling up.
	 * @target	Which pokemon did we beat.
	 */
	private void giveEXP(Pokemon target) {
		int exp = (100 * target.level)/7;
		this.exp += exp;
		System.out.printf("%s gained %d exp.%n", this.name, exp);
		if (this.exp > (4 * Math.pow(this.level, 3)) / 5) {
			this.level++;
			this.maxHealthPoints += 5;
			this.healthPoints = maxHealthPoints;
			this.strength += 1;
			System.out.printf("%s is now level %d. Health has been increased and restored. Strength has been increased.%n", this.name, this.level);
		}
	}
	
	/**
	 * Makes a pokemon attack another pokemon.
	 * @param target	The pokemon to take damage.
	 */
	public void attack(Pokemon target) {
		if (this.healthPoints > 0) {
			System.out.printf("%s attacks %s.%n", this.getName(), target.getName());
			double critical = 1;
			if (Math.random() < this.criticalChance) {
				System.out.println("Critical hit!");
				critical = (2 * this.level +5) / (this.level + 5);
			}
			double randomDouble = 0.85 + (1.0 - 0.85) * random.nextDouble();
			int moveDamage = (int)(Math.random() * 60 + 40);
			int damageInflicted = this.level + (int)((((((2 * this.strength) / 5) + 2) * moveDamage * 1.75) / 50 + 2) * critical * randomDouble);
			target.damage(damageInflicted);
			if (!target.isConscious()) {
				System.out.printf("%s is defeated by %s.%n", target.getName(), this.getName());
				this.giveEXP(target);
				this.fleeCount = 0;
			}
		} else {
			System.out.println("No cheating!");
		}
	}
	
	/**
	 * The trainer may flee if the battle is too hard.
	 * @param target	Who are we trying to flee from
	 * @return			True on successfull escape. False otherwise.			
	 */
	public boolean flee(Pokemon target) {
		if (random.nextInt(256) < (this.level * 128 / target.level) + (30 * this.fleeCount % 256)) {
			this.fleeCount = 0;
			return true;
		} else {
			this.fleeCount++;
			return false;
		}
	}
}