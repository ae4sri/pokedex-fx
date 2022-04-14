# Pokedex

A Pokedex built in Java using JavaFX and Scenebuilder 2.0.

## About

This is an object-oriented, event-driven GUI program that allows users to add, delete,
and view data about their Pokemon. Data can be imported and 
exported through csv files.

Users can add generic Pokemon, individual instances of a Pokemon (called "BattlePokemon") which inherit from Pokemon, moves, and teams. Each of which
are treated as objects in the program (object definitions are in src/main/java/pokedex/objects).

## How to Run

You could run the program through your IDE,
or just run the .jar I've put in the directory. To run the program through your IDE, javafx needs
to be added to the project as a library, and the Main class should be set as 
pokedex.app.Main. This might require some extra configuration (Java can be very annoying). 

Once the project is built, you run it from the command line in the /target/classes using the following:

`java --module-path "<path to javafx installation>" --add-modules javafx.controls,javafx.fxml pokedex.app.Main`

To run the .jar file, go to /out/artifacts/pokedex_jar in your command line. Then run the following: 

`java --module-path "<path to javafx installation>" --add-modules javafx.controls,javafx.fxml -jar pokedex.jar`

Note, `<path to javafx installation>` is the absolute path to the lib folder of your javafx installation.

data.csv contains a bunch of default data to showcase the program, you can load it using the load file in the menu.

## Images

### Creating a Pokemon

<img src="/images/createPokemonView.png">

### Creating a 'Battle' Pokemon (individual instance of a Pokemon)

<img src="/images/createBattle.png">

### Creating a Team

<img src="/images/createTeamView.png">

### Viewing Pokemon

<img src="/images/pokemonView.png">

### Viewing Battle Pokemon

<img src="/images/viewBattlePokemon.png">
