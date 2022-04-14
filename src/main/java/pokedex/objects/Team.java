package pokedex.objects;

import java.util.ArrayList;
import java.util.HashSet;

import static java.lang.Math.min;
/**
 * Author: Amin Elnasri
 */

/**
 * Team class to represent a Pokemon team as an Object.
 */
public class Team {

    /**
     * Set of all team names (used for validation in the program).
     */
    private static HashSet<String> teamNamesSet = new HashSet<>();

    /**
     * Length of the Pokemon team.
     */
    private final static int TEAM_LENGTH = 6;

    /**
     * User provided list of Pokemon in a team.
     */
    private ArrayList<BattleReadyPokemon> team = new ArrayList<BattleReadyPokemon>();

    /**
     * The user provided team name.
     */
    private String name;

    /**
     * Initialize a Team object given a list of Pokemon.
     * @param pokemonList List of Pokemon to put into the team.
     * @param name The team name for this particular team.
     */
    public Team(ArrayList<BattleReadyPokemon> pokemonList, String name) {
        for (int i = 0; i < min(pokemonList.size(),TEAM_LENGTH); i++) { // only get first 6 elements from team
            team.add(pokemonList.get(i));
        }
        this.name = name;
        teamNamesSet.add(name);
    }

    /**
     * Get number of Pokemon in a team.
     * @return The number of Pokemon in a particular team.
     */
    public int numPokemon() { return team.size(); }

    /**
     * Get the name of the Pokemon team
     * @return The team's name.
     */
    public String name() { return name; }

    public static boolean nameTaken(String nameToCheck) {
        return teamNamesSet.contains(nameToCheck);
    }

    /**
     * Add a Pokemon to the team object.
     * @param pokemon The pokemon object to add.
     */
    public void addPokemon(BattleReadyPokemon pokemon) {
        if (team.size() < 6) {
            team.add(pokemon);
        }
    }

    /**
     * Swap a Pokemon in a team for another.
     * @param oldPokemon The Pokemon to replace.
     * @param newPokemon The Pokemon to replace oldPokemon with.
     */
    public void swapPokemon(BattleReadyPokemon oldPokemon, BattleReadyPokemon newPokemon) {
        team.remove(oldPokemon);
        team.add(newPokemon);
    }

    /**
     * Get all the Pokemon in a team. Used for writing t
     * @return
     */
    public ArrayList<String> getAllNicknames() {
        ArrayList<String> nicknames = new ArrayList<>();
        for (int i = 0; i < team.size(); i++) {
            nicknames.add(team.get(i).nickname());
        };
        return nicknames;
    }

    /**
     * Create string representation of the object.
     * @return String representation of a team.
     */
    @Override
    public String toString() {
        String details = "Team Name: " + name + "\n\n";

        for (int i = 0; i < team.size(); i++) {
                details += i+1 + ") " + team.get(i).nameAndNickName() + "\n\n";
            }

        return details;
    }

    /**
     * Find if there's a Pokemon in a team already given a name.
     * @param nameToCheck Check if a Pokemon with this name exists in the name.
     * @return A boolean, true if the team contains a Pokemon with nameToCheck, false otherwise.
     */
    public Boolean contains(String nameToCheck) {
        for (int i = 0; i < team.size(); i++) {
            if (team.get(i).name().equals(nameToCheck)) {
                return true;
            }
        }
        return false;
    }



}
