import java.util.ArrayList;
import java.util.Scanner;

public class Inventory {
	private static final Scanner in = new Scanner(System.in);
	private final ArrayList<Pokeball> pokeballs;
	private final ArrayList<Potion> potions;
	
	public Inventory() {
		this.pokeballs = new ArrayList<>();
		this.potions = new ArrayList<>();
	}
	
	public Inventory(ArrayList<Pokeball> pokeballs, ArrayList<Potion> potions) {
		this.pokeballs = pokeballs;
		this.potions = potions;
	}
	
	public void addPokeball(String name, String description, int amount) {
		Pokeball pokeball = new Pokeball(name, description, amount, Pokeball.Pokeballs.valueOf(name.toUpperCase().replace(" ", "")));
		this.pokeballs.add(pokeball);
	}
	
	public void addPotion(String name, String description, int amount, boolean alive) {
		Potion potion = new Potion(name, description, amount, Potion.Potions.valueOf(name.toUpperCase().replace(" ", "")), alive);
		this.potions.add(potion);
	}
	
	public ArrayList<Pokeball> getPokeballs() {
		ArrayList<Pokeball> pokeballs = new ArrayList<>();
		for (Pokeball pokeball : this.pokeballs) {
			if (pokeball.getAmount() > 0) {
				pokeballs.add(pokeball);
			}
		}
		return pokeballs;
	}
	
	public ArrayList<Potion> getPotions() {
		ArrayList<Potion> potions = new ArrayList<>();
		for (Potion potion : this.potions) {
			if (potion.getAmount() > 0) {
				potions.add(potion);
			}
		}
		return potions;
	}
	
	public void availablePokeballs() {
		System.out.println("You may choose from these items:");
		for (int i = 0; i < this.pokeballs.size(); i++) {
			System.out.printf("%d: %s%n", i + 1, this.pokeballs.get(i));
		}
		System.out.print(">");
	}
	
	public void availablePotions() {
		System.out.println("You may choose from these items:");
		ArrayList<Potion> potionList = this.getPotions();
		for (int i = 0; i < potionList.size(); i++) {
			System.out.printf("%d: %s%n", i + 1, potionList.get(i));
		}
		System.out.print(">");
	}
	
	public Potion chosenPotion() {
		ArrayList<Potion> potions = this.getPotions();
		if (in.hasNextInt()) {
			int choice = in.nextInt() - 1;
			if (choice >= 0 && choice < potions.size()) {
				in.nextLine();
				return potions.get(choice);
			}
		} else {
			in.nextLine();
		}
		return null;
	}
	
	public Pokeball chosenPokeball() {
		ArrayList<Pokeball> pokeballs = this.getPokeballs();
		if (in.hasNextInt()) {
			int choice = in.nextInt() - 1;
			if (choice >= 0 && choice < pokeballs.size()) {
				in.nextLine();
				return pokeballs.get(choice);
			}
		} else {
			in.nextLine();
		}
		return null;
	}
}
