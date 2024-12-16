package de.hsrm.mi.swt.projekt.snackman.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsrm.mi.swt.projekt.snackman.communication.events.Event;
import de.hsrm.mi.swt.projekt.snackman.communication.websocket.WebSocketHandler;
import de.hsrm.mi.swt.projekt.snackman.configuration.GameConfig;
import de.hsrm.mi.swt.projekt.snackman.model.level.SnackManMap;

/**
 * The GameManager manages all of the current games.
 * It passes on incoming events to the game with the respective id.
 * 
 */
public class GameManager {

    Logger logger = LoggerFactory.getLogger(GameManager.class);
    
    public HashMap<Long, Game> allGames;
    public HashMap<Long, Lobby> allLobbies;
    private WebSocketHandler webSocketHandler;
    private GameConfig gameConfig = new GameConfig();

    public GameManager(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
        this.allGames = new HashMap<Long, Game>();
        this.allLobbies = new HashMap<Long, Lobby>();

        //createGame(gameConfig, IDGenerator.getInstance().getUniqueID()); // Game Creation in websocket by MapRquest Event
    }

    public Game getGameById(Long id) {
        if (allGames.containsKey(id)) {
            return allGames.get(id);
        } else {
            return null;
        }
    }

    /**
     * Passes on the received event to the game with the matching game objectId
     * 
     * @param event
     */
    public void handleEvent(Event event) {
        logger.info("handleEvent\n");
        if (allGames.containsKey(event.getGameID())) {
            allGames.get(event.getGameID()).receiveEvent(event);
        }
    }

    /**
     * Notifies the frontend of the changes in the backend
     * 
     * @param event
     */
    public void notifyChange(Event event) {
        webSocketHandler.notifyFrontend(event);
    }

    /**
     * Creates a new game with a unique objectId, the specified gameConfig and Moveables,
     * and a randomly generated map with the in gameConfig specified width and
     * height for it
     * 
     * @param gameConfig
     * @param id
     */
    public void createGame(GameConfig gameConfig, long id, SnackManMap map) {

        logger.info("Create Game \n");

        // SnackManMap map = new SnackManMap(gameConfig.mapWidth, gameConfig.mapHeight);
        //SnackManMap map = new SnackManMap("map_2024-11-26_19_17_39.csv", true);
        // SnackManMap map = new SnackManMap(MapGenerationConfig.SAVED_MAPS_PATH + "testFile.csv", true);
        
        Game newGame = new Game(id, gameConfig, map, this);
        newGame.init(); // Add Objects
        allGames.put(newGame.id, newGame);
    }

    /**
     * Creates a new game with a unique objectId, the specified gameConfig and Moveables,
     * and creates a map from the given csv file
     * 
     * @param gameConfig
     * @param id
     * @param mapFile
     */
    public void createGame(GameConfig gameConfig, long id, String mapFile) {

        SnackManMap map = new SnackManMap(mapFile, true);

        Game newGame = new Game(id, gameConfig, map, this);
        allGames.put(newGame.id, newGame);
    }

    public void setGameConfig(GameConfig gameConfig, long gameID) {
        allGames.get(gameID).setGameConfig(gameConfig);
    }

    public GameConfig getGameConfig(long gameID) {
        if (allGames.get(gameID) == null) {
            return this.gameConfig;
        }
        return allGames.get(gameID).getGameConfig(); 
    }

    public Lobby createLobby(){
        Lobby lobby = new Lobby();
        allLobbies.put(lobby.getId(), lobby);
        //createGame(gameConfig, lobby.getId());
        return lobby;
    }

    public List<Lobby> getAllLobbies() {
        return new ArrayList<>(allLobbies.values());
    }
}
