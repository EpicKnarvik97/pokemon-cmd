import java.util.ArrayList;

public class Inventory {
	private ArrayList<Pokeball> pokeballs;
	private ArrayList<Potion> potions;
	
	public Inventory() {
		this.pokeballs = new ArrayList<Pokeball>();
		this.potions = new ArrayList<Potion>();
	}
	
	public Inventory(ArrayList<Pokeball> pokeballs, ArrayList<Potion> potions) {
		this.pokeballs = pokeballs;
		this.potions = potions;
	}
	
	public void addPokeball(String name, String description, int amount) {
		Pokeball pokeball = new Pokeball(name, description, amount, Pokeball.Pokeballs.valueOf(name.toUpperCase().replace(" ", "")));
		if (pokeball != null) {
			this.pokeballs.add(pokeball);
		}
	}
	
	public void addPotion(String name, String description, int amount, boolean alive) {
		Potion potion = new Potion(name, description, amount, Potion.Potions.valueOf(name.toUpperCase().replace(" ", "")), alive);
		if (potion != null) {
			this.potions.add(potion);
		}
	}
	
	public ArrayList<Pokeball> getPokeballs() {
		ArrayList<Pokeball> pokeballs = new ArrayList<Pokeball>();
		for (Pokeball pokeball : this.pokeballs) {
			if (pokeball.getAmount() > 0) {
				pokeballs.add(pokeball);
			}
		}
		return pokeballs;
	}
	
	public ArrayList<Potion> getPotions() {
		ArrayList<Potion> potions = new ArrayList<Potion>();
		for (Potion potion : this.potions) {
			if (potion.getAmount() > 0) {
				potions.add(potion);
			}
		}
		return potions;
	}
}