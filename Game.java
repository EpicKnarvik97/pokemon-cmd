import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.NumberFormatException;
import java.text.NumberFormat;
import java.util.Locale;
import java.text.ParseException;

/** Simulates the game Pokémon. */
public class Game {
	public static Scanner in = new Scanner(System.in);
	
	public static void main(String[] args) {
		ArrayList<Pokemon> pokemon = readPokemon();
		int initialPokemon = pokemon.size();
		ArrayList<Pokemon> usersPokemon = randomTeam();
		ArrayList<Item> usersItems = prepareInventory();
		
		System.out.println("What is your name?");
		String name = in.nextLine();
		Trainer player = new Trainer(name, usersPokemon, usersItems);
		
		boolean done = false;
		Pokemon opponentPokemon = null;
		Pokemon trainersPokemon = null;
		Item currentItem = null;
		boolean fleeSuccess = false;
		Pokemon pokemonToHeal = null;
		
		opponentPokemon = randomPokemon(pokemon);
		System.out.printf("A wild %s appeared.%n", opponentPokemon.getName());
		
		while (!done) {
			if (opponentPokemon == null) {
				System.out.printf("You have brutally murdered %d pokemon.%n"
								+ "The only ones left are the ones in your posession.%n"
								+ "There really is nothing more to do here.%n", initialPokemon);
				return;
			}
			if (!consciousPokemon(player.getPokemon())) {
				System.out.println("All your pokemon have fainted. Your journey ends here.");
				return;
			}
			while (trainersPokemon == null || !trainersPokemon.isConscious()) {
				availablePokemon(player.getConsciousPokemon());
				trainersPokemon = usersChoice(player.getConsciousPokemon());
			}
			System.out.printf("Opponent: %s%nWhat will you do?%n", opponentPokemon);
			System.out.printf("b: battle"
						  + "%nh: heal or revive"
						  + "%nt: throw pokeball"
						  + "%nc: change pokemon"
						  + "%nf: flee"
						  + "%nv: view my pokemon"
						  + "%ns: save"
						  + "%nl: load"
						  + "%nq: quit%n>");
			char command = in.next().toLowerCase().charAt(0);
			switch (command) {
				case 'b':
					if (opponentPokemon.isConscious() && trainersPokemon.isConscious()) {
						trainersPokemon.attack(opponentPokemon);
						if (opponentPokemon.isConscious()) {
							opponentPokemon.attack(trainersPokemon);
							if (!trainersPokemon.isConscious()) {
								System.out.println("Your pokemon fainted.");
							}
						} else {
							pokemonFainted(pokemon, opponentPokemon);
							System.out.println("The opponent pokemon fainted.");
							opponentPokemon = randomPokemon(pokemon);
						}
					}
					break;
				case 'h':
					if (itemsLeft(usersItems, Item.Target.SELF)) {
						availableItems(usersItems, Item.Target.SELF);
						currentItem = chosenItem(usersItems, Item.Target.SELF);
						if (currentItem == null) {
							System.out.println("Invalid item.");
						} else {
							if (currentItem.needsAlive()) {
								availablePokemon(player.getConsciousPokemon());
								pokemonToHeal = usersChoice(player.getConsciousPokemon());
							} else {
								availablePokemon(player.getFaintedPokemon());
								pokemonToHeal = usersChoice(player.getFaintedPokemon());
							}
							if (pokemonToHeal == null) {
								System.out.println("That is not a valid pokemon");
							} else {
								if (currentItem.use(pokemonToHeal)) {
									opponentPokemon.attack(trainersPokemon);
								}
							}
						}
					} else {
						System.out.println("You have used all your healing items.");
					}
					break;
				case 't':
					if (itemsLeft(usersItems, Item.Target.OTHER)) {
						availableItems(usersItems, Item.Target.OTHER);
						currentItem = chosenItem(usersItems, Item.Target.OTHER);
						if (currentItem == null) {
							System.out.println("Invalid pokeball.");
						} else {
							if (currentItem.getName().toLowerCase().replace(" ", "").equals("masterball")) {
								currentItem.use(opponentPokemon, pokemon, usersPokemon);
								opponentPokemon = randomPokemon(pokemon);
								System.out.printf("A wild %s appeared.%n", opponentPokemon.getName());
							} else {
								boolean captured = currentItem.use(opponentPokemon, pokemon, usersPokemon);
								if (captured) {
									opponentPokemon = randomPokemon(pokemon);
									System.out.printf("A wild %s appeared.%n", opponentPokemon.getName());
								} else {
									opponentPokemon.attack(trainersPokemon);
								}
							}
						}
					} else {
						System.out.println("You have used all your pokeballs.");
					}
					break;
				case 'c':
					availablePokemon(player.getConsciousPokemon());
					trainersPokemon = usersChoice(player.getConsciousPokemon());
					while (trainersPokemon == null) {
						availablePokemon(player.getConsciousPokemon());
						trainersPokemon = usersChoice(player.getConsciousPokemon());
					}
					opponentPokemon.attack(trainersPokemon);
					break;
				case 's':
					savePokemon(pokemon, "pokemon.save");
					savePokemon(usersPokemon, "user.save");
					saveItems(usersItems, "items.save");
					break;
				case 'l':
					ArrayList<Pokemon> loadedPokemon = loadPokemon("pokemon.save");
					ArrayList<Pokemon> loadedUsersPokemon = loadPokemon("user.save");
					ArrayList<Item> loadedUsersItems = loadItems("items.save");
					if (loadedPokemon == null || loadedUsersPokemon == null || loadedUsersItems == null) {
						System.out.println("One or more savefiles seem corrupt. Please delete or fix the affected file(s).");
					} else {
						pokemon = loadedPokemon;
						player.setPokemon(loadedUsersPokemon);
						player.setItems(loadedUsersItems);
						if (pokemon.size() > 0 && usersPokemon.size() > 0) {
							do {
								availablePokemon(player.getConsciousPokemon());
								trainersPokemon = usersChoice(player.getConsciousPokemon());
							} while (trainersPokemon == null || !trainersPokemon.isConscious());
							opponentPokemon = randomPokemon(pokemon);
						}
					}
					break;
				case 'f':
					fleeSuccess = trainersPokemon.flee(opponentPokemon);
					if (fleeSuccess) {
						System.out.println("You fled the battle.");
						opponentPokemon = randomPokemon(pokemon);
						System.out.printf("A wild %s appeared.%n", opponentPokemon.getName());
					} else {
						System.out.printf("Failed to flee from %s.%n", opponentPokemon.getName());
						opponentPokemon.attack(trainersPokemon);
					}
					break;
				case 'v':
					availablePokemon(player.getPokemon());
					System.out.println();
					break;
				case 'q':
					done = true;
					break;
				default:
					System.out.println("[Invalid command]");
			}
		}
	}
	
	/**
	 * Prevents wild fainted pokemon to ever be encountered again.
	 * @param pokemonList	List of pokemon to search.
	 * @param target		The pokemon to remove.
	 */
	public static void pokemonFainted(ArrayList<Pokemon> pokemonList, Pokemon target) {
		for (int i = 0; i < pokemonList.size(); i++) {
			if (pokemonList.get(i).equals(target)) {
				pokemonList.remove(i);
			}
		}
	}
	
	/**
	 * Lists all currently available items for the user.
	 * @param items		List of all of the user's items.
	 * @param target	We are either listing items targeted at an opponent pokemon or at our own pokemon.
	 */
	public static void availableItems(ArrayList<Item> items, Item.Target target) {
		System.out.println("You may choose from these items:");
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getAmount() > 0 && items.get(i).getTarget().equals(target)) {
				System.out.printf("%d: %s%n", i + 1, items.get(i));
			}
		}
		System.out.print(">");
	}
	
	/**
	 * Gives a trainer necessary starting items.
	 * @return	A list of items.
	 */
	public static ArrayList<Item> prepareInventory() {
		ArrayList<Item> usersItems = new ArrayList<Item>();
		usersItems.add(new Item("Poke Ball", "A device for catching wild Pokémon. It's thrown like a ball at a Pokémon, comfortably encapsulating its target.", 15, Item.Target.OTHER));
		usersItems.add(new Item("Great ball", "A good, high-performance Poké Ball that provides a higher Pokémon catch rate than a standard Poké Ball.", 10, Item.Target.OTHER));
		usersItems.add(new Item("Ultra ball", "An ultra-high-performance Poké Ball that provides a higher success rate for catching Pokémon than a Great Ball.", 5, Item.Target.OTHER));
		usersItems.add(new Item("Master ball", "The best Poké Ball with the ultimate level of performance. With it, you will catch any wild Pokémon without fail.", 1, Item.Target.OTHER));
		usersItems.add(new Item("Potion", "Heals a pokemon for 20 HP.", 20, Item.Target.SELF));
		usersItems.add(new Item("Super Potion", "Heals a pokemon for 50 HP.", 10, Item.Target.SELF));
		usersItems.add(new Item("Hyper Potion", "Heals a pokemon for 200 HP.", 5, Item.Target.SELF));
		usersItems.add(new Item("Max Potion", "Fully heals a pokemon.", 1, Item.Target.SELF));
		usersItems.add(new Item("Revive", "Revives a fainted pokemon.", 2, Item.Target.SELF, false));
		return usersItems;
	}
	
	/**
	 * Checks if the user has any item left of any type.
	 * @param items	List of all of the user's items.
	 * @return		True if any items are left. False otherwise.
	 */
	public static boolean itemsLeft(ArrayList<Item> items, Item.Target target) {
		int count = 0;
		for (Item item : items) {
			if (item.getAmount() > 0 && item.getTarget().equals(target)) {
				count++;
			}
		}
		return count > 0;
	}
	
	/**
	 * Lists all currently available pokemon for the user.
	 * @param pokemonList	List of all the user's pokemon.
	 * @param type			Should we only include a certain type of pokemon.
	 */
	public static void availablePokemon(ArrayList<Pokemon> pokemonList) {
		System.out.println("You may choose from these pokemon:");
		for (int i = 0; i < pokemonList.size(); i++) {
			System.out.printf("%d: %s%n", i + 1, pokemonList.get(i));
			
		}
		System.out.print(">");
	}
	
	/**
	 * Checks if all of the pokemon in a list have fainted.
	 * @param pokemonList	List of the user's pokemon.
	 * @return				True if the user has at least one pokemon left. False otherwise.
	 */
	public static boolean consciousPokemon(ArrayList<Pokemon> pokemonList) {
		int pokemonLeft = 0;
		for (Pokemon pokemon : pokemonList) {
			if (pokemon.isConscious()) {
				pokemonLeft++;
			}
		}
		return pokemonLeft > 0;
	}
	
	/**
	 * Asks the user for the name of a pokemon and returns it.
	 * @param pokemonList	A list of available pokemon
	 * @return				A pokemon object or null.
	 */
	public static Pokemon usersChoice(ArrayList<Pokemon> pokemonList) {
		if (in.hasNextInt()) {
			int choice = in.nextInt() - 1;
			in.nextLine();
			if (choice >= 0 && choice < pokemonList.size()) {
				return pokemonList.get(choice);
			} else {
				System.out.println("Invalid pokemon");
			}
		}
		return null;
	}
	
	/**
	 * Asks the user for an item, and validates it.
	 * @param itemList	Available items.
	 * @return			An Item object or null.
	 */
	public static Item chosenItem(ArrayList<Item> itemList, Item.Target target) {
		if (in.hasNextInt()) {
			int choice = in.nextInt() - 1;
			in.nextLine();
			if (choice >= 0 && choice < itemList.size()) {
				return itemList.get(choice);
			} else {
				System.out.println("Invalid item");
			}
		}
		return null;
	}
	
	/**
	 * Chooses a random pokemon from a list.
	 * @param pokemon	The list to choose from.
	 * @return			A Pokemon object, or null if the list is empty.
	 */
	public static Pokemon randomPokemon(ArrayList<Pokemon> pokemon) {
		if (pokemon.size() > 0) {
			return pokemon.get((int)(Math.random() * pokemon.size()));
		} else {
			return null;
		}
	}
	
	/**
	 * Reads pokemon from Pokemon.txt to an ArrayList.
	 * @return	An ArrayList of pokemon objects.
	 */
	public static ArrayList<Pokemon> readPokemon() {
		ArrayList<Pokemon> pokemon = new ArrayList<Pokemon>();
		try (Scanner file = new Scanner(new File("Pokemon.txt"))) {
			while (file.hasNextLine()) {
				pokemon.add(new Pokemon(file.nextLine()));
			}
		} catch (FileNotFoundException e) {
			System.out.println("You seem to be missing one of the necessary files to run this program.");
		}
		return pokemon;
	}
	
	/**
	 * Picks a random pokemon from a list.
	 * @param pokemon	A list of pokemon objects.
	 * @return			A pokemon object.
	 */
	public static Pokemon pick(ArrayList<Pokemon> pokemon) {
		int index = (int)(Math.random() * (pokemon.size()));
		Pokemon randomPokemon = pokemon.get(index);
		pokemon.remove(index);
		return randomPokemon;
	}
	
	/**
	 * Saves all pokemon in a list with stats to a text file.
	 * @param pokemonList	List of Pokemon objects to save.
	 * @param savefile		The file to write to.
	 */
	public static void savePokemon(ArrayList<Pokemon> pokemonList, String savefile) {
		try (PrintWriter file = new PrintWriter(savefile)) {
			for (Pokemon pokemon : pokemonList) {
				file.println(pokemon.saveString());
			}
			System.out.println("Successfully saved pokemon.");
		} catch (FileNotFoundException e) {
			System.out.println("The savefile could not be written.");
		}
	}
	
	/**
	 * Saves all items in a list to a text file.
	 * @param itemList	List of Item objects to save.
	 * @param savefile	The file to write to.
	 */
	public static void saveItems(ArrayList<Item> itemList, String savefile) {
		try (PrintWriter file = new PrintWriter(savefile)) {
			for (Item item : itemList) {
				file.println(item.saveString());
			}
			System.out.println("Successfully saved items.");
		} catch (FileNotFoundException e) {
			System.out.println("The savefile could not be written.");
		}
	}
	
	/**
	 * Loads pokemon from a text file.
	 * @param savefile	The file to write to.
	 * @return			A list of pokemon or null on failiure.
	 */
	public static ArrayList<Pokemon> loadPokemon(String savefile) {
		ArrayList<Pokemon> pokemon = new ArrayList<Pokemon>();
		try (Scanner file = new Scanner(new File(savefile))) {
			NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
			while (file.hasNextLine()) {
				String[] data = file.nextLine().split(";");
				try {
					pokemon.add(new Pokemon(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]), format.parse(data[4]).doubleValue(), Integer.parseInt(data[5]), Integer.parseInt(data[6]), Integer.parseInt(data[7])));
				} catch (NumberFormatException e) {
					System.out.println("Malformed number " + e);
				} catch (ParseException e) {
					System.out.println("Malformed number " + e);
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Invalid savefile: " + savefile);
					return null;
				}
			}
			System.out.println("Successfully loaded pokemon.");
		} catch (FileNotFoundException e) {
			System.out.println("You don't have a valid savefile.");
		}
		return pokemon;
	}
	
	/**
	 * Loads items from a text file.
	 * @param savefile	The file to write to.
	 * @return			A list of items or null on failiure.
	 */
	public static ArrayList<Item> loadItems(String savefile) {
		ArrayList<Item> items = new ArrayList<Item>();
		try (Scanner file = new Scanner(new File(savefile))) {
			while (file.hasNextLine()) {
				try {
					String[] data = file.nextLine().split(";");
					items.add(new Item(data[0], data[1], Integer.parseInt(data[2]), Item.Target.valueOf(data[3]), Boolean.parseBoolean(data[4])));
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Invalid savefile: " + savefile);
					return null;
				}
			}
			System.out.println("Successfully loaded items.");
		} catch (FileNotFoundException e) {
			System.out.println("You don't have a valid savefile.");
		}
		return items;
	}
	
	public static ArrayList<Pokemon> randomTeam() {
		ArrayList<Pokemon> temporary = readPokemon();
		ArrayList<Pokemon> pokemon = new ArrayList<Pokemon>();
		for (int i = 1; i <= 6; i++) {
			pokemon.add(pick(temporary));
		}
		return pokemon;
	}
}