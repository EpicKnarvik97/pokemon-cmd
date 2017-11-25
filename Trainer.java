import java.util.ArrayList;

public class Trainer {
	String name;
	private ArrayList<Pokemon> pokemon;
	private ArrayList<Item> items;
	
	public Trainer(String name, ArrayList<Pokemon> pokemon, ArrayList<Item> items) {
		this.name = name;
		this.pokemon = pokemon;
		this.items = items;
	}
	
	public void setPokemon(ArrayList<Pokemon> pokemon) {
		this.pokemon = pokemon;
	}
	
	public void setItems(ArrayList<Item> items) {
		this.items = items;
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
	
	public ArrayList<Item> getItems() {
		ArrayList<Item> items = new ArrayList<Item>();
		for (Item item : this.items) {
			if (item.getAmount() > 0) {
				items.add(item);
			}
		}
		return items;
	}
}