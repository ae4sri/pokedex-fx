package pokedex.utils;

import pokedex.objects.*;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Author: Amin Elnasri
 */

/**
 * Class to read a csv file containing Pokemon,BattlePokemon,Moves,and Teams.
 */
public final class Reader {
    /**
     * SOME NOTES:
     * - "." represents a null field in the csv file. This is so we don't get lines like ",,," in the file, making the
     * split function return awkward results.
     *
     * - The first four fields of every row in the CSV indicate which type of data is represented in the row (Pokemon,
     * BattleReadyPokemon, Move, or Team).
     " */

    /**
     * The arraylist where the objects created from scanning the csv file will be stored.
     */
    private static final ArrayList<Object> data = new ArrayList<Object>();

    /**
     * Indexes for where attributes should be in a line in the csv file.
     */
    private static final int IS_POKEMON_INDEX = 0;
    private static final int IS_POKEMON_IN_TEAM_INDEX = 1;
    private static final int IS_MOVE_INDEX = 2;
    private static final int IS_TEAM_INDEX = 3;

    private static final int NAME_INDEX = 4;
    private static final int HP_INDEX = 5;
    private static final int ATTACK_INDEX = 6;
    private static final int SPATTACK_INDEX = 7;
    private static final int DEFENSE_INDEX = 8;
    private static final int SPDEFENSE_INDEX = 9;
    private static final int SPEED_INDEX = 10;
    private static final int DESCRIPTION_INDEX = 11;
    private static final int TYPE_1_INDEX = 12;
    private static final int TYPE_2_INDEX = 13;
    private static final int MOVE_1_INDEX = 14;
    private static final int MOVE_2_INDEX = 15;
    private static final int MOVE_3_INDEX = 16;
    private static final int MOVE_4_INDEX = 17;
    private static final int NICKNAME_INDEX = 18;

    private static final int POKE_1_INDEX = 19;
    private static final int POKE_2_INDEX = 20;
    private static final int POKE_3_INDEX = 21;
    private static final int POKE_4_INDEX = 22;
    private static final int POKE_5_INDEX = 23;
    private static final int POKE_6_INDEX = 24;

    /**
     * Read data from a given file, and return as a list of objects.
     * @param file The file to read data from.
     * @return A list of objects that may be either Pokemon, Move, or PokemonInTeam.
     * @throws IOException
     */
    public static ArrayList<Object> readData(File file) throws Exception {
        try {
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            String line = buffered_reader.readLine();
            line = buffered_reader.readLine(); // first line contains table headers
            while (line != null) {
                // Reference for split: https://www.javacodeexamples.com/java-split-string-by-comma-example/740
                // Pokemon and move descriptions will often require commas, so to avoid splitting the double quote
                // surrounded descriptions
                // into pieces with the split function, we use some regex (from the reference).
                String[] rowDetails = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // split by , but ignore when theyre in double quotes
                if (rowDetails[IS_POKEMON_INDEX].equals("Y")) { // if the row contains values for a pokemon
                    data.add(createPokemon(rowDetails));
                } else if (rowDetails[IS_POKEMON_IN_TEAM_INDEX].equals("Y")) { // if it contains values for a pokemonInTeam
                    data.add(createBattlePokemon(rowDetails));
                } else if (rowDetails[IS_MOVE_INDEX].equals("Y")) { // if it contains val for move
                    data.add(createMove(rowDetails));
                } else { // otherwise it must be a team
                    Team team = createTeam(rowDetails);
                    if (team != null) {
                        data.add(team);
                    }
                }
                line = buffered_reader.readLine();
            }
            return data;

        } catch(Exception e) { // print specific error message for filenotfound exceptions,
            // otherwise print generic error message
            if (e instanceof FileNotFoundException) {
                throw new Exception("File doesn't exist!");
            } else {
                throw new Exception("Error reading from csv file!");
            }
        }
    }

    /**
     * Create a Pokemon based off a single line in the CSV file.
     * @param rowDetails The line in the csv file (must be marked as a Pokemon line)
     * @return A Pokemon object using the details in the line of the csv file.
     */
    private static Pokemon createPokemon(String[] rowDetails) {

        String name = rowDetails[NAME_INDEX];
        int hp = Integer.parseInt(rowDetails[HP_INDEX]);
        int attack = Integer.parseInt(rowDetails[ATTACK_INDEX]);
        int spattack = Integer.parseInt(rowDetails[SPATTACK_INDEX]);
        int defense = Integer.parseInt(rowDetails[DEFENSE_INDEX]);
        int spdefense = Integer.parseInt(rowDetails[SPDEFENSE_INDEX]);
        int speed = Integer.parseInt(rowDetails[SPEED_INDEX]);
        Type type1 = new Type(rowDetails[TYPE_1_INDEX]);
        Type type2 = new Type(rowDetails[TYPE_2_INDEX]);
        String description = rowDetails[DESCRIPTION_INDEX];

        if (description.equals(".")) { description = ""; }

        else {
            // remove quotes around description as it'll be surrounded with quotes to avoid issues with commas/csvs
            description = description.substring(3,description.length() - 3);
        }
        return new Pokemon(name,hp,attack,spattack,
                defense,spdefense,speed,description,type1,type2);
    }

    /**
     * Create a Move based off a single line in the CSV file.
     * @param rowDetails rowDetails The line in the csv file (must be marked as a Move line)
     * @return A Move object using the details in the line of the csv file.
     */
    private static Move createMove(String[] rowDetails) {
        String name = rowDetails[NAME_INDEX];
        String description = rowDetails[DESCRIPTION_INDEX];
        if (description.equals(".")) { description = ""; }
        else {
            // remove quotes around description as it'll be surrounded with quotes to avoid issues with commas/csvs
            description = description.substring(3,description.length() - 3);
        }
        Type type = new Type(rowDetails[TYPE_1_INDEX]);
        return new Move(name,description,type);
    }

    /**
     * Create a BattleReadyPokemon object given a row with its details, and return it.
     * @param rowDetails An array representing a row from the csv file split using .split().
     * @return The pokemon object created using the data from rowDetails.
     */
    private static BattleReadyPokemon createBattlePokemon(String[] rowDetails) {
        String name = rowDetails[NAME_INDEX];
        String nickname = rowDetails[NICKNAME_INDEX];

        if (nickname.equals(".")) { nickname = ""; } // if username is null, make it an empty string

        // get attributes from line
        int hp = Integer.parseInt(rowDetails[HP_INDEX]);
        int attack = Integer.parseInt(rowDetails[ATTACK_INDEX]);
        int spattack = Integer.parseInt(rowDetails[SPATTACK_INDEX]);
        int defense = Integer.parseInt(rowDetails[DEFENSE_INDEX]);
        int spdefense = Integer.parseInt(rowDetails[SPDEFENSE_INDEX]);
        int speed = Integer.parseInt(rowDetails[SPEED_INDEX]);
        Type type1 = new Type(rowDetails[TYPE_1_INDEX]);
        Type type2 = new Type(rowDetails[TYPE_2_INDEX]);
        String description = rowDetails[DESCRIPTION_INDEX];
        ArrayList<Move> pokemonMoves = new ArrayList<Move>();

        // Create the Moves if they exist and have already been defined.
        if (!rowDetails[MOVE_1_INDEX].equals(".")) {
            Move newMove = getMoveFromScannedMoves(rowDetails[MOVE_1_INDEX]);
            if (newMove != null) { pokemonMoves.add(newMove); }
        }
        if (!rowDetails[MOVE_2_INDEX].equals(".")) {
            Move newMove = getMoveFromScannedMoves(rowDetails[MOVE_2_INDEX]);
            if (newMove != null) { pokemonMoves.add(newMove); }
        }
        if (!rowDetails[MOVE_3_INDEX].equals(".")) {
            Move newMove = getMoveFromScannedMoves(rowDetails[MOVE_3_INDEX]);
            if (newMove != null) { pokemonMoves.add(newMove); }
        }
        if (!rowDetails[MOVE_4_INDEX].equals(".")) {
            Move newMove = getMoveFromScannedMoves(rowDetails[MOVE_4_INDEX]);
            if (newMove != null) { pokemonMoves.add(newMove); }
        }

        return new BattleReadyPokemon(nickname,name,hp,attack,spattack,
                defense,spdefense,speed,description.substring(3,description.length() - 3),type1,type2,pokemonMoves);

    }

    /**
     * Get a move with a specific name from the move objects that have already been created. Used to
     * add Move objects to BattleReadyPokemon, as they only list move names in the csv file.
     * @param name
     * @return
     */
    private static Move getMoveFromScannedMoves(String name) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) instanceof Move) {
                Move move = (Move) data.get(i);
                if (move.name().equals(name)) {
                    return move;
                }
            }
        }
        /**
         * If csv file has been saved correctly, this shouldnt be returned, as moves should be stored in a line
         * ABOVE where the BattleReadyPokemon are defined.
         */
        return null;
    }

    /**
     * Create a Team object based off a line in the csv file.
     * @param rowDetails
     * @return A Team object containing the BattleReadyPokemon listed in the line.
     */
    private static Team createTeam(String[] rowDetails) {
        String teamName = rowDetails[NAME_INDEX];
        ArrayList<BattleReadyPokemon> initialPokemon = new ArrayList<>();

        if (!rowDetails[POKE_1_INDEX].equals(".")) {
            initialPokemon.add(getBRPfromScanned(rowDetails[POKE_1_INDEX]));
        }
        if (!rowDetails[POKE_2_INDEX].equals(".")) {
            initialPokemon.add(getBRPfromScanned(rowDetails[POKE_2_INDEX]));
        }
        if (!rowDetails[POKE_3_INDEX].equals(".")) {
            initialPokemon.add(getBRPfromScanned(rowDetails[POKE_3_INDEX]));
        }
        if (!rowDetails[POKE_4_INDEX].equals(".")) {
            initialPokemon.add(getBRPfromScanned(rowDetails[POKE_4_INDEX]));
        }
        if (!rowDetails[POKE_5_INDEX].equals(".")) {
            initialPokemon.add(getBRPfromScanned(rowDetails[POKE_5_INDEX]));
        }
        if (!rowDetails[POKE_6_INDEX].equals(".")) {
            initialPokemon.add(getBRPfromScanned(rowDetails[POKE_6_INDEX]));
        }
        if (initialPokemon.size() > 0) {
            return new Team(initialPokemon, teamName);
        }
        return null;
    }


    /**
     * Find an already created BattleReadyPokemon given its nickname. Used to create teams by just specifying
     * the nickname of BRP in the csv file.
     * @param name Nickname of BattleReadyPokemon to get.
     * @return A Battle
     */
    private static BattleReadyPokemon getBRPfromScanned(String name) {
        for (int i = 0; i <= data.size(); i++) {
            if (data.get(i) instanceof BattleReadyPokemon) {
                BattleReadyPokemon poke = (BattleReadyPokemon) data.get(i);
                if (poke.nickname().equals(name)) {
                    return poke.clone();
                }
            }
        }
        return null;
    }
}
