import java.util.ArrayList;
import java.util.Scanner;

public class Trainer {
	public static Scanner in = new Scanner(System.in);
	String name;
	private ArrayList<Pokemon> pokemon;
	private Inventory inventory;
	
	public Trainer(String name, ArrayList<Pokemon> pokemon, Inventory inventory) {
		this.name = name;
		this.pokemon = pokemon;
		this.inventory = inventory;
	}
	
	public void setPokemon(ArrayList<Pokemon> pokemon) {
		this.pokemon = pokemon;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public ArrayList<Pokemon> getPokemon() {
		return this.pokemon;
	}
	
	public void addPokemon(Pokemon pokemon) {
		this.pokemon.add(pokemon);
	}
	
	public ArrayList<Pokemon> getConsciousPokemon() {
		ArrayList<Pokemon> pokemon = new ArrayList<Pokemon>();
		for (Pokemon singlePokemon : this.pokemon) {
			if (singlePokemon.isConscious()) {
				pokemon.add(singlePokemon);
			}
		}
		return pokemon;
	}
	
	public ArrayList<Pokemon> getFaintedPokemon() {
		ArrayList<Pokemon> pokemon = new ArrayList<Pokemon>();
		for (Pokemon singlePokemon : this.pokemon) {
			if (!singlePokemon.isConscious()) {
				pokemon.add(singlePokemon);
			}
		}
		return pokemon;
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	/** Lists all currently available pokemon for the trainer.*/
	public void availablePokemon(boolean alive) {
		ArrayList<Pokemon> pokemonList = null;
		if (alive) {
			pokemonList = this.getConsciousPokemon();
		} else {
			pokemonList = this.getFaintedPokemon();
		}
		System.out.println("You may choose from these pokemon:");
		for (int i = 0; i < pokemonList.size(); i++) {
			System.out.printf("%d: %s%n", i + 1, pokemonList.get(i));
		}
		System.out.print(">");
	}
	
	public void printPokemon() {
		for (Pokemon pokemon : this.pokemon) {
			System.out.println(pokemon);
		}
	}
	
	/**
	 * Asks the user for the name of a pokemon and returns it.
	 * @param pokemonList	A list of available pokemon
	 * @return				A pokemon object or null.
	 */
	public Pokemon choosePokemon(boolean alive) {
		ArrayList<Pokemon> pokemonList = null;
		if (alive) {
			pokemonList = this.getConsciousPokemon();
		} else {
			pokemonList = this.getFaintedPokemon();
		}
		if (in.hasNextInt()) {
			int choice = in.nextInt() - 1;
			if (choice >= 0 && choice < pokemonList.size()) {
				in.nextLine();
				return pokemonList.get(choice);
			}
		}
		in.nextLine();
		return null;
	}
}