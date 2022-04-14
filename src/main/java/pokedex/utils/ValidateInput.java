package pokedex.utils;

import pokedex.objects.BattleReadyPokemon;
import pokedex.objects.Move;
import pokedex.objects.Pokemon;
import pokedex.objects.Team;

import java.util.ArrayList;
import java.util.HashSet;
/**
 * Author: Amin Elnasri
 */

/**
 * Class to help validate inputs in the GUI.
 */
public class ValidateInput {
    /**
     * Validate that a given Pokemon name is valid, and can be added to the program.
     * @param name The name to validate.
     * @return An Error if the name isn't valid, otherwise null.
     */
    public static Error pokemonNameValidate(String name) {
        if (name.equals("")) {
            return new Error("Please enter a name for the Pokemon.");
        }
        if (Pokemon.pokemonExists(name)) {
            return new Error("A pokemon with the name '" + name + "' already exists. Please select a different name.");
        }
        if (name.length() > 30) {
            return new Error("A Pokemon can only have up to 30 characters in their name.");
        }
        return null; // return null to indicate there's no error
    }

    /**
     * Validate that a given Pokemon's stat value is valid.
     * @param stat The stat to validate.
     * @return An error if the stat is invalid; otherwise null.
     */
    public static Error pokemonStatValidate(String stat) {
        try {
            int s = Integer.parseInt(stat);
            boolean inRange = !(s < 1 || s > 999);
            if (!inRange) {
                return new Error("Please enter a number between 0-999 for each of the Pokemon's stats.");
            }
            return null;
        } catch (Exception e) {
            return new Error("Please enter a number between 0-999 for each of the Pokemon's stats.");
        }
    }

    /**
     * Validate that a given Pokemon's description is valid.
     * @param description The description to validate.
     * @return An error if the description is valid; otherwise null.
     */
    public static Error descriptionValidate(String description) {
        if (description.length() > 300) {
            return new Error("Maximum length for a description is 300 characters.");
        }
        return null;
    }

    /**
     * Validate that a nickname is valid and can be used to create a Battle Pokemon.
     * @param nickname The nickname to validate.
     * @return An error if the nickname is invalid; otherwise null.
     */
    public static Error validateNickname(String nickname) {
        if (nickname.equals("")) {
            return new Error("Please enter a nickname for the Battle Pokemon.");
        }
        if (nickname.length() > 30) {
            return new Error("A Battle-Pokemon can only have up to 30 characters in their nickname.");
        }
        if (BattleReadyPokemon.nicknameUsed(nickname)) {
            return new Error("A Battle-Pokemon with the name '" + nickname + "' already exists.");
        }
        return null;
    }

    /**
     * Validate that a move's name is valid and can be used to create a move.
     * @param name The move name to validate.
     * @return An error if the move name is invalid; otherwise null.
     */
    public static Error moveNameValidate(String name) {
        if (name.equals("")) {
            return new Error("Please enter a name for the Move.");
        }
        if (name.length() > 30) {
            return new Error("A Move can only have up to 30 characters in its name.");
        }
        if (Move.nameUsed(name)) {
            return new Error("A move with name '" + name + "' already exists.");
        }
        return null;
    }

    /**
     * Validate that a team name is valid and can be used to create a team.
     * @param name The team name to validate.
     * @return An error if the team name is in valid; otherwise null.
     */
    public static Error teamNameValidate(String name) {
        if (name.equals("")) {
            return new Error("Please enter a name for the Team.");
        }
        if (name.length() > 30) {
            return new Error("A Team can only have up to 30 characters in its name.");
        }
        if (Team.nameTaken(name)) {
            return new Error("A Team with name '" + name + "' already exists.");
        }
        return null;
    }

    /**
     * Validate that a list of moves is valid (that they're all different).
     * @param moves A list of moves to validate.
     * @return A boolean; true if the moves are valid, false otherwise.
     */
    public static boolean validateMoves(ArrayList<Move> moves) {
        HashSet<String> seenMoves = new HashSet<>();
        for (int i=0; i < moves.size(); i++) {
            String curName = moves.get(i).name();
            if (seenMoves.contains(curName)) {
                return false;
            }
            seenMoves.add(curName);
        }
        return true;
    }
}
