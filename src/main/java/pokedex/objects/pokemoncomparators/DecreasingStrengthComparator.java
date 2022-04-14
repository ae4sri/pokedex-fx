package pokedex.objects.pokemoncomparators;

import pokedex.objects.Pokemon;

import java.util.Comparator;

public class DecreasingStrengthComparator implements Comparator<Pokemon> {
    @Override
    public int compare(Pokemon poke1, Pokemon poke2) {
        return poke2.strength() - poke1.strength();
    }
}
