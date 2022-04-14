package pokedex.app;


import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pokedex.objects.*;
import pokedex.objects.pokemoncomparators.DecreasingStrengthComparator;
import pokedex.utils.Reader;
import pokedex.utils.ValidateInput;
import pokedex.utils.Writer;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Author: Amin Elnasri
 */

/**
 * Controller class to handle events in the GUI.
 */
public class MainController {
    FileChooser fileChooser = new FileChooser();

    /**
     * Placeholder. Actual file will be put here in main function, so that it can be used for exporting data as well.
     */
    private File globalFile = null;
    /**
     * Store list of Pokemon in the program.
     */
    private static ArrayList<Pokemon> pokemon = new ArrayList<Pokemon>();

    /**
     * Store list of teams in the program.
     */
    private static ArrayList<Team> teams = new ArrayList<Team>();

    /**
     * Store list of moves in the program.
     */
    private static ArrayList<Move> moves = new ArrayList<Move>();

    /**
     * Store list of individual instances of Pokemon.
     */
    private static ArrayList<BattleReadyPokemon> battlePokemon = new ArrayList<BattleReadyPokemon>();

    @FXML
    private TextArea battlePokemonDetails;

    @FXML
    private TextField battlePokemonName;

    @FXML
    private TableColumn<String, String> battlePokemonNameColumn;

    @FXML
    private TableColumn<String, String> moveNameColumn;

    @FXML
    private TableColumn<String,String> teamNameColumn;

    @FXML
    private TextField battlePokemonNickname;

    @FXML
    private TextField battlePokemonSearchBar;

    @FXML
    private TableView<String> battlePokemonTableView;

    @FXML
    private ChoiceBox<String> firstMove;

    @FXML
    private ChoiceBox<String> fourthMove;

    @FXML
    private TextArea moveDescription;

    @FXML
    private TextArea moveDetails;

    @FXML
    private TextField moveName;

    @FXML
    private TextField moveSearchBar;

    @FXML
    private TableView<String> moveTableView;

    @FXML
    private ChoiceBox<String> moveType;

    @FXML
    private TextField pokemonAttack;

    @FXML
    private TextField pokemonDefense;

    @FXML
    private TextArea pokemonDescription;

    @FXML
    private TextArea pokemonDetails;

    @FXML
    private TextField pokemonHealth;

    @FXML
    private TextField pokemonName;

    @FXML
    private TableColumn<String, String> pokemonNameColumn;

    @FXML
    private TextField pokemonSpattack;

    @FXML
    private TextField pokemonSpdefense;

    @FXML
    private TextField pokemonSpeed;

    @FXML
    private TableView<String> pokemonTableView;

    @FXML
    private ChoiceBox<String> pokemonType1;

    @FXML
    private ChoiceBox<String> pokemonType2;

    @FXML
    private ChoiceBox<String> secondMove;

    @FXML
    private TextArea teamDetails;

    @FXML
    private TextField teamName;

    @FXML
    private TextField teamPokemon1;

    @FXML
    private TextField teamPokemon2;

    @FXML
    private TextField teamPokemon3;

    @FXML
    private TextField teamPokemon4;

    @FXML
    private TextField teamPokemon5;

    @FXML
    private TextField teamPokemon6;

    @FXML
    private TableView<String> teamTableView;

    @FXML
    private ChoiceBox<String> thirdMove;

    @FXML
    private TextField viewPokemonSearchBar;

    @FXML
    private Label status;

    /**
     * Create a BattlePokemon (if inputs are valid) and update the GUI.
     * @param event  The event of the user clicking the "Create Battle Pokemon" button.
     */
    @FXML
    void createBattlePokemon(MouseEvent event) {
        // get inputs from textareas and choiceboxes
        String name = battlePokemonName.getText();
        String nickname = battlePokemonNickname.getText();
        String move1 = firstMove.getSelectionModel().getSelectedItem();
        String move2 = secondMove.getSelectionModel().getSelectedItem();
        String move3 = thirdMove.getSelectionModel().getSelectedItem();
        String move4 = fourthMove.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Creating Battle Pokemon");
        Error nicknameError = ValidateInput.validateNickname(nickname);

        // validate the inputs
        if (!Pokemon.pokemonExists(name)) {
            alert.setContentText("A pokemon with the name '" + name + "' doesn't exist. Please enter" +
                    " an existing Pokemon's name for the name value.");
            alert.show();
            status.setText("Failed to create Battle Pokemon.");
            return;
        }
        if (nicknameError != null) {
            alert.setContentText(nicknameError.getMessage());
            alert.show();
            status.setText("Failed to create Battle Pokemon.");
            return;
        }

        // create a list of the pokemon's moves out of the selected moves
        ArrayList<Move> movesList = new ArrayList<>();
        if (move1 != null) {
            movesList.add(getMoveWithName(move1));
        }
        if (move2 != null) {
            movesList.add(getMoveWithName(move2));
        }
        if (move3 != null) {
            movesList.add(getMoveWithName(move3));
        }
        if (move4 != null) {
            movesList.add(getMoveWithName(move4));
        }

        // validate said list
        if (!ValidateInput.validateMoves(movesList)) {
            alert.setContentText("Invalid moves for the BattleReadyPokemon; only one of each move can be added.");
            alert.show();
            status.setText("Failed to create Battle Pokemon.");
            return;
        }

        // get pokemon to base battlepokemon based off
        Pokemon poke = getPokemonWithName(name);

        // create and add the new battle pokemon
        BattleReadyPokemon newPoke = new BattleReadyPokemon(nickname,poke.name(),poke.health(),poke.attack(),
                poke.specialatk(), poke.defense(), poke.specialdef(), poke.speed(),
                poke.description(), poke.firstType(),poke.secondType(),movesList);
        battlePokemon.add(newPoke);

        // update battlePokemon table
        populateBattlePokemonTable(battlePokemon);


        // reset the input fields
        firstMove.valueProperty().set(null);
        secondMove.valueProperty().set(null);
        thirdMove.valueProperty().set(null);
        fourthMove.valueProperty().set(null);

        battlePokemonName.clear();
        battlePokemonNickname.clear();
        status.setText("Successfully created Battle Pokemon.");

    }

    /**
     * Create a Move (if inputs are valid) and update the GUI.
     * @param event  The event of the user clicking the "Create Move" button.
     */
    @FXML
    void createMove(MouseEvent event) {
        // get strings from input fields
        String name = moveName.getText();
        String description = moveDescription.getText();
        String type = moveType.getSelectionModel().getSelectedItem();

        // validate input
        Error nameError = ValidateInput.moveNameValidate(name);
        Error descriptionError = ValidateInput.descriptionValidate(description);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Creating Move");
        if (nameError != null) {
            alert.setContentText(nameError.getMessage());
            alert.show();
            status.setText("Failed to create move.");
            return;
        }
        if (descriptionError != null) {
            alert.setContentText(descriptionError.getMessage());
            alert.show();
            status.setText("Failed to create move.");
            return;
        }

        if (type == null) { type = ""; } // type for a move is optional

        // create move, add it to global list
        Type newType = new Type(type);
        Move newMove = new Move(name,description,newType);
        moves.add(newMove);

        // refresh moves table
        populateMovesTable(moves);

        // clear input fields
        moveName.clear();
        moveDescription.clear();
        moveType.valueProperty().set(null);
        status.setText("Move added successfully.");


    }

    /**
     * Create a Pokemon (if inputs are valid) and update the GUI.
     * @param event The event of the user clicking the "Create Pokemon" button.
     */
    @FXML
    void createPokemon(MouseEvent event) {
        // get input fields and potential errors
        String name = pokemonName.getText();
        String health = pokemonHealth.getText();
        String speed = pokemonSpeed.getText();
        String attack = pokemonAttack.getText();
        String spAttack = pokemonSpattack.getText();
        String defense = pokemonDefense.getText();
        String spDefense = pokemonSpdefense.getText();
        String description = pokemonDescription.getText();
        String type1 = pokemonType1.getSelectionModel().getSelectedItem();
        String type2 = pokemonType2.getSelectionModel().getSelectedItem();
        Error nameError = ValidateInput.pokemonNameValidate(name);
        Error healthError = ValidateInput.pokemonStatValidate(health);
        Error atkError = ValidateInput.pokemonStatValidate(attack);
        Error spatkError = ValidateInput.pokemonStatValidate(spAttack);
        Error defError = ValidateInput.pokemonStatValidate(defense);
        Error spdefError = ValidateInput.pokemonStatValidate(spDefense);
        Error speedError = ValidateInput.pokemonStatValidate(speed);
        Error descriptionError = ValidateInput.descriptionValidate(description);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Creating Pokemon");

        // text input validation, make sure everything is valid
        if (nameError != null) {
            alert.setContentText(nameError.getMessage());
            alert.show();
            status.setText("Failed to create Pokemon.");
            return;
        }
        if (!Type.validateType(type1)) {
            alert.setContentText("Please enter a type for the program.");
            alert.show();
            status.setText("Failed to create Pokemon.");
            return;
        } // type 2 doesnt need to be validated as its optional (and is a choicebox)
        if (healthError != null) {
            alert.setContentText(healthError.getMessage());
            alert.show();
            status.setText("Failed to create Pokemon.");
            return;
        }
        if (atkError != null) {
            alert.setContentText(atkError.getMessage());
            alert.show();
            status.setText("Failed to create Pokemon.");
            return;
        }
        if (spatkError != null) {
            alert.setContentText(spatkError.getMessage());
            alert.show();
            status.setText("Failed to create Pokemon.");
            return;
        }
        if (defError != null) {
            alert.setContentText(defError.getMessage());
            alert.show();
            status.setText("Failed to create Pokemon.");
            return;
        }
        if (spdefError != null) {
            alert.setContentText(spdefError.getMessage());
            alert.show();
            status.setText("Failed to create Pokemon.");
            return;
        }
        if (speedError != null) {
            alert.setContentText(speedError.getMessage());
            alert.show();
            status.setText("Failed to create Pokemon.");
            return;
        }
        if (descriptionError != null) {
            alert.setContentText(descriptionError.getMessage());
            alert.show();
            status.setText("Failed to create Pokemon.");
            return;
        }
        if (type1.equals(type2)) {
            alert.setContentText("Type 1 cannot be the same as type 2.");
            alert.show();
            status.setText("Failed to create Pokemon.");
            return;
        }
        // if everything is valid, parse stats into integers and create Type objects
        int hp = Integer.parseInt(health);
        int atk = Integer.parseInt(attack);
        int spatk = Integer.parseInt(spAttack);
        int def = Integer.parseInt(defense);
        int spdef = Integer.parseInt(spDefense);
        int spd = Integer.parseInt(speed);
        Type firstType = new Type(type1);
        Type secondType = new Type(type2);

        // create the pokemon, add it to the list
        Pokemon newPokemon = new Pokemon(name,hp,atk,spatk,def,spdef,spd,description,firstType,secondType);
        pokemon.add(newPokemon);

        // update the table
        populatePokemonTable(pokemon);

        // clear the input fields
        pokemonHealth.clear();
        pokemonName.clear();
        pokemonSpdefense.clear();
        pokemonDefense.clear();
        pokemonAttack.clear();
        pokemonSpattack.clear();
        pokemonType1.valueProperty().set(null);
        pokemonType2.valueProperty().set(null);
        pokemonDescription.clear();
        status.setText("Successfully created Pokemon.");

    }

    /**
     * If inputs are valid, create a team and update the GUI.
     * @param event Event of the user clicking the "Create Team" button.
     */
    @FXML
    void createTeam(MouseEvent event) {
        // get strings from input fields
        String newTeamName = teamName.getText();
        String firstPokemon = teamPokemon1.getText();
        String secondPokemon = teamPokemon2.getText();
        String thirdPokemon = teamPokemon3.getText();
        String fourthPokemon = teamPokemon4.getText();
        String fifthPokemon = teamPokemon5.getText();
        String sixthPokemon = teamPokemon6.getText();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Creating Team");

        Error teamNameError = ValidateInput.teamNameValidate(newTeamName);

        // check if no input was entered at all
        if (firstPokemon.equals("") && secondPokemon.equals("") && thirdPokemon.equals("") && fourthPokemon.equals("")
        && fifthPokemon.equals("") && sixthPokemon.equals("")) {
            alert.setContentText("Please enter the nicknames of the Pokemon you'd like to create your team with.");
            alert.show();
            status.setText("Error creating team.");
            return;
        }

        // validate team name
        if (teamNameError != null) {
            alert.setContentText(teamNameError.getMessage());
            alert.show();
            status.setText("Error creating team.");
            return;
        }
        // validate the pokemon nicknames (the ones that were entered)
        if (!BattleReadyPokemon.nicknameUsed(firstPokemon) && !firstPokemon.equals("")) {
            alert.setContentText("Battle-Pokemon with nickname '" + firstPokemon + "' doesn't exist.");
            alert.show();
            status.setText("Error creating team.");
            return;
        }
        if (!BattleReadyPokemon.nicknameUsed(secondPokemon) && !secondPokemon.equals("") ) {
            alert.setContentText("Battle-Pokemon with nickname '" + secondPokemon + "' doesn't exist.");
            alert.show();
            status.setText("Error creating team.");
            return;
        }
        if (!BattleReadyPokemon.nicknameUsed(thirdPokemon) && !thirdPokemon.equals("")) {
            alert.setContentText("Battle-Pokemon with nickname '" + thirdPokemon + "' doesn't exist.");
            alert.show();
            status.setText("Error creating team.");
            return;
        }
        if (!BattleReadyPokemon.nicknameUsed(fourthPokemon)  && !fourthPokemon.equals("")) {
            alert.setContentText("Battle-Pokemon with nickname '" + fourthPokemon + "' doesn't exist.");
            alert.show();
            status.setText("Error creating team.");
            return;
        }
        if (!BattleReadyPokemon.nicknameUsed(fifthPokemon) && !fifthPokemon.equals("")) {
            alert.setContentText("Battle-Pokemon with nickname '" + fifthPokemon + "' doesn't exist.");
            alert.show();
            status.setText("Error creating team.");
            return;
        }
        if (!BattleReadyPokemon.nicknameUsed(sixthPokemon) && !sixthPokemon.equals("")) {
            alert.setContentText("Battle-Pokemon with nickname '" + sixthPokemon + "' doesn't exist.");
            alert.show();
            status.setText("Error creating team.");
            return;
        }

        // create team, add non-empty pokemon to the team
        ArrayList<BattleReadyPokemon> team = new ArrayList<>();

        if (!firstPokemon.equals("")) {
            team.add(getBRPWithExactName(firstPokemon));
        }
        if (!secondPokemon.equals("")) {
            team.add(getBRPWithExactName(secondPokemon));
        }
        if (!thirdPokemon.equals("")) {
            team.add(getBRPWithExactName(thirdPokemon));
        }
        if (!fourthPokemon.equals("")) {
            team.add(getBRPWithExactName(fourthPokemon));
        }
        if (!fifthPokemon.equals("")) {
            team.add(getBRPWithExactName(fifthPokemon));
        }
        if (!sixthPokemon.equals("")) {
            team.add(getBRPWithExactName(sixthPokemon));
        }

        // add team, update gui
        Team newTeam = new Team(team, newTeamName);
        teams.add(newTeam);
        populateTeamsTable();

        // clear the textfields in the gui
        teamName.clear();
        teamPokemon1.clear();
        teamPokemon2.clear();
        teamPokemon3.clear();
        teamPokemon4.clear();
        teamPokemon5.clear();
        teamPokemon6.clear();
        status.setText("Successfully created team.");
    }

    /**
     * Filter out battlePokemon table in GUI by the text input.
     * @param event Event of user clicking the "Search Battle Pokemon" button.
     */
    @FXML
    void searchBattlePokemon(MouseEvent event) {
        // deselect current row, then give the table a new list of battlePokemon based off the query
        battlePokemonTableView.getSelectionModel().clearSelection();
        String query = battlePokemonSearchBar.getText();
        populateBattlePokemonTable(searchForBattlePokemon(query));
    }

    /**
     * Filter out moves table in GUI by the text input.
     * @param event Event of the user clicking the "Search Moves" button.
     */
    @FXML
    void searchMoves(MouseEvent event) {
        // deselect current row, then give the moves table a new list of moves based off the query
        moveTableView.getSelectionModel().clearSelection();
        String query = moveSearchBar.getText();
        populateMovesTable(searchForMoves(query));
    }

    /**
     * Filter out Pokemon in GUI by the text input.
     * @param event Event of user clicking the "Search Pokemon" button.
     */
    @FXML
    void searchPokemon(MouseEvent event) {
        // deselect current row, then give the pokemon table a new list of pokemon based off the query
        pokemonTableView.getSelectionModel().clearSelection();
        String query = viewPokemonSearchBar.getText();
        populatePokemonTable(searchForPokemon(query));
    }

    /**
     * Sort Pokemon in table by their strengths in decreasing order.
     * @param event Event of user clicking the "Sort by Decreasing Strength" button.
     */
    @FXML
    void sortPokemonDecreasing(MouseEvent event) {
        // deselect current row
        pokemonTableView.getSelectionModel().clearSelection();

        // create clone of global pokemon list, sort it using a custom comparator, then use the cloned list
        // to populate the table.
        ArrayList<Pokemon> clone = new ArrayList<>(pokemon);
        Collections.sort(clone, new DecreasingStrengthComparator());
        populatePokemonTable(clone);
    }

    /**
     * Sort Pokemon in table by their strengths in increasing order.
     * @param event Event of user clicking the "Sort by Increasing Strength" button.
     * @param event
     */
    @FXML
    void sortPokemonIncreasing(MouseEvent event) {
        // pretty much same as sortPokemonDecreasing, except we reverse the list as well to get the pokemon sorted
        // in increasing order (by strength, aka sum of stats)
        pokemonTableView.getSelectionModel().clearSelection();
        ArrayList<Pokemon> clone = new ArrayList<>(pokemon);
        Collections.sort(clone, new DecreasingStrengthComparator());
        Collections.reverse(clone);
        populatePokemonTable(clone);
    }


    /**
     * Initialize gui; set up tables and choiceboxes.
     * @throws IOException
     */
    public void initialize() throws IOException {

        pokemonNameColumn.setCellValueFactory(cellData ->new ReadOnlyStringWrapper(cellData.getValue()));
        battlePokemonNameColumn.setCellValueFactory(cellData ->new ReadOnlyStringWrapper(cellData.getValue()));
        moveNameColumn.setCellValueFactory(cellData ->new ReadOnlyStringWrapper(cellData.getValue()));
        teamNameColumn.setCellValueFactory(cellData ->new ReadOnlyStringWrapper(cellData.getValue()));

        pokemonTableView.getSelectionModel().selectedIndexProperty().addListener((num) -> {
            updatePokemonDetails();
        });
        battlePokemonTableView.getSelectionModel().selectedIndexProperty().addListener((num) -> {
            updateBattlePokemonDetails();
        });
        moveTableView.getSelectionModel().selectedIndexProperty().addListener((num) -> {
            updateMoveDetails();
        });
        teamTableView.getSelectionModel().selectedIndexProperty().addListener((num) -> {
            updateTeamDetails();
        });
        pokemonType1.setItems(FXCollections.observableArrayList(
                "Normal", "Fire", "Water", "Grass", "Electric","Ice", "Fighting", "Poison",
                "Ground", "Flying", "Psychic", "Bug", "Rock", "Ghost", "Dark", "Dragon", "Steel",
                "Fairy"));
        pokemonType2.setItems(FXCollections.observableArrayList(
                "","Normal", "Fire", "Water", "Grass", "Electric","Ice", "Fighting", "Poison",
                "Ground", "Flying", "Psychic", "Bug", "Rock", "Ghost", "Dark", "Dragon", "Steel",
                "Fairy"));
        moveType.setItems(FXCollections.observableArrayList(
                "","Normal", "Fire", "Water", "Grass", "Electric","Ice", "Fighting", "Poison",
                "Ground", "Flying", "Psychic", "Bug", "Rock", "Ghost", "Dark", "Dragon", "Steel",
                "Fairy"));

        populatePokemonTable(pokemon);
        populateBattlePokemonTable(battlePokemon);
        populateMovesTable(moves);
        populateTeamsTable();

        ArrayList<String> moveNames = new ArrayList<>();
        for (int i = 0; i < moves.size(); i++) {
            moveNames.add(moves.get(i).name());
        }
        firstMove.getItems().addAll(moveNames);
        secondMove.getItems().addAll(moveNames);
        thirdMove.getItems().addAll(moveNames);
        fourthMove.getItems().addAll(moveNames);
        status.setText("Program initialized.");
    }

    /**
     * Display an alert with an About message.
     * @param event Event of user clicking "About" in the menu.
     */
    @FXML
    void showAboutMessage(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("""
                A program for tracking and viewing Pokemon data.
                By Amin Elnasri.
                """);
        alert.show();
    }


    /**
     * Fill in the TextFields in the Team page with random battle pokemon nicknames.
     * @param event
     */
    @FXML
    void createRandomTeam(MouseEvent event) {
        if (battlePokemon.size() < 6) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("""
                You need at least 6 Pokemon in the program to start creating random teams.
                """);
            alert.show();
            return;
        }
        ArrayList<String> pokemonTeam = getRandomBattlePokemon();
        teamPokemon1.setText(pokemonTeam.get(0));
        teamPokemon2.setText(pokemonTeam.get(1));
        teamPokemon3.setText(pokemonTeam.get(2));
        teamPokemon4.setText(pokemonTeam.get(3));
        teamPokemon5.setText(pokemonTeam.get(4));
        teamPokemon6.setText(pokemonTeam.get(5));
        status.setText("Random team generated");

    }


    /**
     * Load a file the user selects.
     * @param event User clicking the "Load File" option in the menu.
     */
    @FXML
    void loadFile(ActionEvent event) {
        // open file chooser to select file to load
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            // if user exited, dont do anything
            if (file == null || !file.exists()) { // if user didnt pick a file, dont do anything
                return;
            }
            ArrayList<Object> dataFromCSV = Reader.readData(file);
            addDataToProgram(dataFromCSV);
        } catch (Exception e) {
            // if there was an error reading the file, display an alert stating what happened.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error loading file.");
            alert.setContentText(e.getMessage());
            status.setText("Error reading file.");
            alert.show();
            return;
        }
        // if user didnt pick a file, return early
        if (!file.exists()) {
            return;
        }
        ArrayList<String> moveNames = new ArrayList<>();
        for (int i = 0; i < moves.size(); i++) {
            moveNames.add(moves.get(i).name());
        }
        // clear old choicebox for moves, set to the new list of moves
        firstMove.getItems().clear();
        secondMove.getItems().clear();
        thirdMove.getItems().clear();
        fourthMove.getItems().clear();

        firstMove.getItems().addAll(moveNames);
        secondMove.getItems().addAll(moveNames);
        thirdMove.getItems().addAll(moveNames);
        fourthMove.getItems().addAll(moveNames);

        // update the tables after adding data to the program
        populateTeamsTable();
        populateMovesTable(moves);
        populatePokemonTable(pokemon);
        populateBattlePokemonTable(battlePokemon);
        // set a new global file (user can now save to this file quickly by just clicking "Save")
        globalFile = file;
        status.setText("File has been loaded successfully.");
    }

    /**
     * Save the current state of the program to the global file.
     * @param event User clicking "Save" in the menu.
     * @throws IOException
     */
    @FXML
    void save(ActionEvent event) throws IOException {
        if (globalFile == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cannot Save File");
            alert.setContentText("Please use the 'Save As' or 'Load' option first to select " +
                    "a file to save to.");
            alert.show();
            status.setText("Cannot save to file.");
            return;
        }
        Writer.writeToCSV(globalFile, pokemon, moves, battlePokemon, teams);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("File Saved");
        alert.setContentText("File has been saved to " + globalFile.getName() + ".");
        alert.show();
        status.setText("File successfully saved.");
    }

    /**
     * Save current state of the program to a location/file the user selects.
     * @param event User clicking "Save As" in the menu.
     * @throws IOException
     */
    @FXML
    void saveAs(ActionEvent event) throws IOException {
        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null) {
            return;
        }
        globalFile = file;
        Writer.writeToCSV(file, pokemon, moves, battlePokemon, teams);
        status.setText("File saved.");
    }

    /**
     * Add data from the CSV to the program.
     * @param dataFromCSV The list of objects to add to the program.
     */
    private void addDataToProgram(ArrayList<Object> dataFromCSV) {
        // clear old data
        moves.clear();
        battlePokemon.clear();
        pokemon.clear();
        teams.clear();

        // read data from the csv file, add objects to their respective lists
        for (int i = 0; i < dataFromCSV.size(); i++) { // add data from csv file to program
            Object obj  = dataFromCSV.get(i);
            if (obj instanceof Move) {
                moves.add((Move) obj);
            } else if (obj instanceof BattleReadyPokemon) {
                battlePokemon.add((BattleReadyPokemon) obj);
            } else if (obj instanceof Pokemon) {
                pokemon.add((Pokemon) obj);
            } else if (obj instanceof Team) {
                teams.add((Team) obj);
            }
        }
    }

    /**
     * Fill the Pokemon table in the GUI with a given list of Pokemon.
     * @param pokemonList Pokemon to display in the list.
     */
    private void populatePokemonTable(ArrayList<Pokemon> pokemonList) {
        // clear table, and update based off given list; the next 3 functions also work exactly the same way.
        pokemonTableView.getItems().clear();
        for (int i=0; i < pokemonList.size(); i++) {
            String newPokemon = pokemonList.get(i).name();
            pokemonTableView.getItems().add(newPokemon);
        }
    }
    /**
     * Fill the Battle Pokemon table in the GUI with a given list of Battle Pokemon.
     * @param pokemonList Battle Pokemon to display in the list.
     */
    private void populateBattlePokemonTable(ArrayList<BattleReadyPokemon> pokemonList) {
        battlePokemonTableView.getItems().clear();
        for (int i=0; i < pokemonList.size(); i++) {
            String newPokemon = pokemonList.get(i).nameAndNickName();
            battlePokemonTableView.getItems().add(newPokemon);
        }
    }

    /**
     * Fill the Moves table in the GUI with a given list of moves.
     * @param moveList Moves to display in the list.
     */
    private void populateMovesTable(ArrayList<Move> moveList) {
        moveTableView.getItems().clear();
        for (int i=0; i < moveList.size(); i++) {
            String moveName = moveList.get(i).name();
            moveTableView.getItems().add(moveName);
        }
    }
    /**
     * Fill the Teams table in the GUI with the global teams list.
     */
    private void populateTeamsTable() {
        teamTableView.getItems().clear();
        for (int i=0; i < teams.size(); i++) {
            String teamName = teams.get(i).name();
            teamTableView.getItems().add(teamName);
        }
    }

    /**
     * Update the Pokemon Details section in the GUI for when a user clicks a Pokemon in the table.
     */
    private void updatePokemonDetails() {
        int selectedID = pokemonTableView.getSelectionModel().getSelectedIndex();
        // if the index is invalid, dont update the gui
        if (selectedID == -1) {
            return;
        }
        String name = pokemonTableView.getItems().get(selectedID);
        Pokemon poke = getPokemonWithName(name);
        pokemonDetails.setText(poke.toString());
    }
    /**
     * Update the Battle Pokemon details section in the GUI for when a user clicks a Battle Pokemon in the table.
     */
    private void updateBattlePokemonDetails() {
        int selectedID = battlePokemonTableView.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) {
            return;
        }
        String name = battlePokemonTableView.getItems().get(selectedID);
        BattleReadyPokemon poke = getBRPWithName(name);
        battlePokemonDetails.setText(poke.toString());
    }

    /**
     * Update the Move details section in the GUI for when a user clicks a Move in the table.
     */
    private void updateMoveDetails() {
        int selectedID = moveTableView.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) {
            return;
        }
        String name = moveTableView.getItems().get(selectedID);
        Move move = getMoveWithName(name);
        moveDetails.setText(move.toString());
    }

    /**
     * Update the Team details section in the GUi for when a user clicks a Team in the table.
     */
    private void updateTeamDetails() {
        int selectedID = teamTableView.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) {
            return;
        }
        String name = teamTableView.getItems().get(selectedID);
        Team team = getTeamWithName(name);
        teamDetails.setText(team.toString());
    }

    /**
     * Get a Pokemon with a specific name (used to get a Pokemon's details based off its name alone)
     * @param name The name of the Pokemon to retrieve.
     * @return Pokemon with name 'name'.
     */
    private Pokemon getPokemonWithName(String name) {
        for (int i = 0; i < pokemon.size(); i++) {
            if (pokemon.get(i).name().equals(name)) {
                return pokemon.get(i);
            }
        }
        return null;
    }

    /**
     * Get a team with a specific name.
     * @param name Name of team to retrieve.
     * @return Team with name 'name'.
     */
    private Team getTeamWithName(String name) {
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).name().equals(name)) {
                return teams.get(i);
            }
        }
        return null;
    }

    /**
     * Get a Battle Pokemon with a specific name and nickname (a string in the form "<name> (<nickname>)")
     * @param name .nameAndNickname() field of Battle Pokemon to retrieve.
     * @return Battle Pokemon with name 'name'.
     */
    private BattleReadyPokemon getBRPWithName(String name) {
        for (int i = 0; i < battlePokemon.size(); i++) {
            if (battlePokemon.get(i).nameAndNickName().equals(name)) {
                return battlePokemon.get(i).clone();
            }
        }
        return null;
    }

    /**
     * Get a Battle Pokemon with a specific nickname (nickname only).
     * @param nickname .nickname() field of BattlePokemon to retrieve.
     * @return
     */
    private BattleReadyPokemon getBRPWithExactName(String nickname) {
        for (int i=0; i < battlePokemon.size(); i++) {
            if (battlePokemon.get(i).nickname().equals(nickname)) {
                return battlePokemon.get(i).clone();
            }
        }
        return null;
    }

    /**
     * Get a move with a specific name.
     * @param name Name of move to retrieve.
     * @return  Move with name 'name'.
     */
    private Move getMoveWithName(String name) {
        for (int i = 0; i < moves.size(); i++) {
            if (moves.get(i).name().equals(name)) {
                return moves.get(i);
            }
        }
        return null;
    }

    /**
     * Search for a list of Pokemon.
     * @param query Query to filter Pokemon with.
     * @return A list of Pokemon that contain the string 'query' in their nickname.
     */
    private ArrayList<Pokemon> searchForPokemon(String query) {
        if (query.equals("")) { return pokemon; }
        query = query.toLowerCase();
        ArrayList<Pokemon> pokemonList = new ArrayList<Pokemon>();
        for (Pokemon possiblePokemon : pokemon) {
            if (possiblePokemon.name().toLowerCase().contains(query)) {
                pokemonList.add(possiblePokemon);
            }
        }
        return pokemonList;
    }

    /**
     * Search for a list of Battle Pokemon.
     * @param query Query to filter Battle Pokemon with.
     * @return A list of Battle Pokemon that contain the string 'query' in their name or nickname.
     */
    private ArrayList<BattleReadyPokemon> searchForBattlePokemon(String query) {
        if (query.equals("")) { return battlePokemon; }
        query = query.toLowerCase();
        ArrayList<BattleReadyPokemon> pokemonList = new ArrayList<>();
        for (BattleReadyPokemon possiblePokemon : battlePokemon) {
            if (possiblePokemon.nameAndNickName().toLowerCase().contains(query)) {
                pokemonList.add(possiblePokemon);
            }
        }
        return pokemonList;
    }

    /**
     * Search for a list of moves.
     * @param query Query to filter moves with.
     * @return A list of moves that contain the string 'query' in their name.
     */
    public ArrayList<Move> searchForMoves(String query) {
        if (query.equals("")) { return moves; }
        query = query.toLowerCase();
        ArrayList<Move> moveList = new ArrayList<>();
        for (Move move: moves) {
            if (move.name().toLowerCase().contains(query)) {
                moveList.add(move);
            }
        }
        return moveList;
    }

    /***
     * Get an array of 6 random Pokemon names.
     * @return An Array, size 6, of 6 different Pokemon names.
     */
    public static ArrayList<String> getRandomBattlePokemon() {
        int numPokemon = battlePokemon.size();
        Random random = new Random();
        HashSet<Integer> randomNums = new HashSet<>();

        // get 6 nums between 0 and num of brp
        while (randomNums.size() < 6) {
            int n = random.nextInt(numPokemon);
            randomNums.add(n);
        }

        int n = 0; // will be num of iterations up to a certain point in the following loop

        ArrayList<String> randomPokemon = new ArrayList<>();

        // loop through all battlePokemon while keeping track of the count; if the curcount is one of the
        // 6 random numbers, add the pokemon to the team.
        for (BattleReadyPokemon poke : battlePokemon){
            if (randomNums.contains(n)) {
                randomPokemon.add(poke.nickname());
            }
            n++;
        }
        Collections.shuffle(randomPokemon); // shuffle the random pokemon so theyre not ordered by
        // index anymore
        return randomPokemon;
    }

}
