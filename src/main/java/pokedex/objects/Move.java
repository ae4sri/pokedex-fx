package pokedex.objects;

import java.util.HashSet;

/**
 * Author: Amin Elnasri
 */

/**
 * Class to represent a Pokemon's Move as an object.
 */
public class Move {

    private static HashSet<String> namesSet = new HashSet<String>();

    /**
     * Name of the move.
     */
    private String name;

    /**
     * Description of the move.
     */
    private String description;

    /**
     * Type of the move.
     */
    private Type type;

    /**
     * Object initializer for Move.
     * @param name Name of the move.
     * @param description Description of the move.
     */
    public Move(String name,String description, Type type) {
        this.name = name;
        this.description = description;
        this.type = type;
        namesSet.add(name);
    }

    /**
     * Get name of the Pokemon.
     * @return Name of the Pokemon.
     */
    public String name() { return name; }

    /**
     * Get the description of the Pokemon.
     * @return Description of the Pokemon.
     */
    public String description() { return description; }

    /**
     * Get type of the move.
     * @return The type of the move.
     */
    public Type getType() { return type; }

    /**
     * Check if there already exists a move with a given name.
     * @param name
     * @return
     */
    public static boolean nameUsed(String name) {
        return namesSet.contains(name);
    }


    /**
     * Modify String representation of a Move.
     * @return String representation of a Move.
     */
    @Override
    public String toString() {
        return String.format("""
               Move Name: %1$s \n
               Description: %2$s  \n
               Type: %3$s               
               """,name,description,type);
    }
}
