import java.util.ArrayList;

public class Trainer {
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
}