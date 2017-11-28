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
		
		System.out.println("What is your name?");
		String name = in.nextLine();
		Trainer player = new Trainer(name, randomTeam(), createInventory());
		
		boolean done = false;
		Pokemon opponentPokemon = null;
		Pokemon trainersPokemon = null;
		Pokeball currentPokeball = null;
		Potion currentPotion = null;
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
			if (player.getConsciousPokemon().size() < 1) {
				System.out.println("All your pokemon have fainted. Your journey ends here.");
				return;
			}
			while (trainersPokemon == null || !trainersPokemon.isConscious()) {
				player.availablePokemon(true);
				trainersPokemon = player.choosePokemon(true);
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
					if (player.getInventory().getPotions().size() > 0) {
						player.getInventory().availablePotions();
						currentPotion = player.getInventory().chosenPotion();
						if (currentPotion == null) {
							in.nextLine();
							System.out.println("Invalid potion.");
						} else {
							if (currentPotion.needsAlive()) {
								player.availablePokemon(true);
								pokemonToHeal = player.choosePokemon(true);
							} else {
								player.availablePokemon(false);
								pokemonToHeal = player.choosePokemon(false);
							}
							if (pokemonToHeal == null) {
								System.out.println("That is not a valid pokemon");
							} else {
								if (currentPotion.use(pokemonToHeal)) {
									opponentPokemon.attack(trainersPokemon);
								}
							}
						}
					} else {
						System.out.println("You have used all your healing items.");
					}
					break;
				case 't':
					if (player.getInventory().getPokeballs().size() > 0) {
						player.getInventory().availablePokeballs();
						currentPokeball = player.getInventory().chosenPokeball();
						if (currentPokeball == null) {
							in.nextLine();
							System.out.println("Invalid pokeball.");
						} else {
							if (currentPokeball.getType() == Pokeball.Pokeballs.MASTERBALL) {
								currentPokeball.use(opponentPokemon, pokemon, player);
								opponentPokemon = randomPokemon(pokemon);
								System.out.printf("A wild %s appeared.%n", opponentPokemon.getName());
							} else {
								boolean captured = currentPokeball.use(opponentPokemon, pokemon, player);
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
					player.availablePokemon(true);
					trainersPokemon = player.choosePokemon(true);
					while (trainersPokemon == null) {
						player.availablePokemon(true);
						trainersPokemon = player.choosePokemon(true);
					}
					opponentPokemon.attack(trainersPokemon);
					break;
				case 's':
					savePokemon(pokemon, "pokemon.save");
					savePokemon(player.getPokemon(), "user.save");
					saveInventory(player.getInventory(), "inventory.save");
					break;
				case 'l':
					ArrayList<Pokemon> loadedPokemon = loadPokemon("pokemon.save");
					ArrayList<Pokemon> loadedUsersPokemon = loadPokemon("user.save");
					Inventory loadedInventory = loadInventory("inventory.save");
					if (loadedPokemon == null || loadedUsersPokemon == null || loadedInventory == null) {
						System.out.println("One or more savefiles seem corrupt. Please delete or fix the affected file(s).");
					} else {
						pokemon = loadedPokemon;
						player.setPokemon(loadedUsersPokemon);
						player.setInventory(loadedInventory);
						if (pokemon.size() > 0 && player.getPokemon().size() > 0) {
							do {
								player.availablePokemon(true);
								trainersPokemon = player.choosePokemon(true);
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
					player.printPokemon();
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
	 * Prevents wild fainted pokemon from ever being encountered again.
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
	 * Gives a trainer necessary starting items.
	 * @return	A list of items.
	 */
	public static Inventory createInventory() {
		Inventory inventory = new Inventory();
		inventory.addPokeball("Poke Ball", "A device for catching wild Pokémon. It's thrown like a ball at a Pokémon, comfortably encapsulating its target.", 15);
		inventory.addPokeball("Great ball", "A good, high-performance Poké Ball that provides a higher Pokémon catch rate than a standard Poké Ball.", 10);
		inventory.addPokeball("Ultra ball", "An ultra-high-performance Poké Ball that provides a higher success rate for catching Pokémon than a Great Ball.", 5);
		inventory.addPokeball("Master ball", "The best Poké Ball with the ultimate level of performance. With it, you will catch any wild Pokémon without fail.", 1);
		inventory.addPotion("Potion", "Heals a pokemon for 20 HP.", 20, true);
		inventory.addPotion("Super Potion", "Heals a pokemon for 50 HP.", 10, true);
		inventory.addPotion("Hyper Potion", "Heals a pokemon for 200 HP.", 5, true);
		inventory.addPotion("Max Potion", "Fully heals a pokemon.", 1, true);
		inventory.addPotion("Revive", "Revives a fainted pokemon.", 2, false);
		return inventory;
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
		try (Scanner file = new Scanner(new File("config/Pokemon.txt"))) {
			while (file.hasNextLine()) {
				pokemon.add(new Pokemon(file.nextLine()));
			}
		} catch (FileNotFoundException e) {
			System.out.println("You seem to be missing one of the necessary files to run this program.");
		}
		/** If the file is compiled as jar, this will prevent an empty list. */
		if (pokemon.size() < 1) {
			try (Scanner file = new Scanner(Game.class.getResourceAsStream("/config/Pokemon.txt"))) {
				while (file.hasNextLine()) {
					pokemon.add(new Pokemon(file.nextLine()));
				}
			}
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
	public static void saveInventory(Inventory inventory, String savefile) {
		try (PrintWriter file = new PrintWriter(savefile)) {
			for (Pokeball pokeball : inventory.getPokeballs()) {
				file.println(pokeball.saveString());
			}
			file.println(":NEXTLIST:");
			for (Potion potion : inventory.getPotions()) {
				file.println(potion.saveString());
			}
			System.out.println("Successfully saved inventory.");
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
	public static Inventory loadInventory(String savefile) {
		ArrayList<Pokeball> pokeballs = new ArrayList<Pokeball>();
		ArrayList<Potion> potions = new ArrayList<Potion>();
		try (Scanner file = new Scanner(new File(savefile))) {
			String next = "";
			while (file.hasNextLine() && !next.equals(":NEXTLIST:")) {
				try {
					next = file.nextLine();
					if (!next.equals(":NEXTLIST:")) {
						String[] data = next.split(";");
						pokeballs.add(new Pokeball(data[0], data[1], Integer.parseInt(data[2]), Pokeball.Pokeballs.valueOf(data[0].toUpperCase().replace(" ", ""))));
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Invalid savefile: " + savefile);
					return null;
				}
			}
			while (file.hasNextLine()) {
				try {
					String[] data = file.nextLine().split(";");
					potions.add(new Potion(data[0], data[1], Integer.parseInt(data[2]), Potion.Potions.valueOf(data[0].toUpperCase().replace(" ", ""))));
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Invalid savefile: " + savefile);
					return null;
				}
			}
			System.out.println("Successfully loaded items.");
		} catch (FileNotFoundException e) {
			System.out.println("You don't have a valid savefile.");
		}
		return new Inventory(pokeballs, potions);
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
