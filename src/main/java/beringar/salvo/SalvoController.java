package beringar.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private SalvoRepository salvoRepository;






    @RequestMapping("/games")
    public Map<String, Object> getAllGamesLogged(Authentication authentication) {

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if(isGuest(authentication)){
            System.out.println("Guest in !");
            dto.put("player", "Guest");
        } else {
            System.out.println("User authenticaded !");
            dto.put("player", loggedInToDTO(getLoggedPlayer(authentication)));
        }

        dto.put("games", getAllGames());
        return dto;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public List<Map> getAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> GameToDTO(game))
                .collect(Collectors.toList());
    }

    private Map<String, Object> GameToDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", GamePlayerList(game.getGamePlayers()));
        dto.put("scores", GamePlayerScores(game.getScores()));
        return dto;
    }

    private List<Map> GamePlayerList(List<GamePlayer> gamePlayers) {
        return gamePlayers.stream()
                .map(gamePlayer -> GamePlayerToDTO(gamePlayer))
                .collect(Collectors.toList());
    }

    private Map<String, Object> GamePlayerToDTO(GamePlayer gameplayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();

        dto.put("id", gameplayer.getId());
        dto.put("player", PlayerToDTO(gameplayer.getPlayer()));
        dto.put("joinDate", gameplayer.getJoinDate());
        return dto;
    }

    private Map<String, Object> PlayerToDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getId());
        dto.put("email", player.getEmail());
        return dto;
    }

    private List<Map> GamePlayerScores(List<Score> scores) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        return scores.stream()
                .map(score -> ScoreToDTO(score))
                .collect(Collectors.toList());
    }

    private Map<String, Object> ScoreToDTO(Score score) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
            dto.put("playerID", score.getPlayer().getId());
            dto.put("score", score.getScore());
            dto.put("finishDate", score.getFinishDate());
        return dto;
    }


    @RequestMapping("/players")
    public List<Map> getAllPlayers() {
        return playerRepository
                .findAll()
                .stream()
                .map(player -> player.toDTO())
                .collect(Collectors.toList());
    }


/*    public List<Long> getAllGames() {
        List<Game> games = gameRepository.findAll();
        return
                games.stream()
                        .map(Game::getId)
                        .collect(toList());
    }


    public List<Long> getAllPlayers() {
        List<Player> players = playerRepository.findAll();
        return
                players.stream()
                        .map(Player::getId)
                        .collect(toList());
    }*/



    @RequestMapping("/gameplayers")
    public List<Long> getAllGamePlayers() {
        List<GamePlayer> gameplayers = gamePlayerRepository.findAll();
        return
                gameplayers.stream()
                        .map(GamePlayer::getId)
                        .collect(toList());
    }


    @RequestMapping("/game_view/{gamePlayerID}")
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable Long gamePlayerID, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerID);
        if (authentication == null){
            return new ResponseEntity<>(makeMap("error", "You're Not Logged In!"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.getPlayer().getId() == getLoggedPlayer(authentication).getId()){
            return new ResponseEntity<> (GameViewToDTO(gamePlayer.getGame(), gamePlayer), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(makeMap("error", "Are you trying to cheat? You are not allowed to access to this Game page data!"), HttpStatus.UNAUTHORIZED);
        }
    }


    private Map<String, Object> GameViewToDTO(Game game, GamePlayer gamePlayer) {

        List<Ship> ships = gamePlayer.getShips();
        Long currentPlayerID = gamePlayer.getPlayer().getId();
        Map<String, Object> hits = new LinkedHashMap<>();
        Map<String, Object> dto = new LinkedHashMap<>();


        hits.put("self", getHits(gamePlayer, getOpponent(game, currentPlayerID)));
        hits.put("opponent", getHits(getOpponent(game, currentPlayerID), gamePlayer));

        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gameState", getGameState(gamePlayer));
        dto.put("gamePlayers", GamePlayerList(game.getGamePlayers()));
        dto.put("ships", ShipLocationsList(ships));
        dto.put("salvoes", GetSalvosFromAllGamePlayers(game));
        dto.put("hits", hits);
        return dto;
    }

    private List<Map> GetSalvosFromAllGamePlayers(Game game) {
        List<Map> finalList = new ArrayList<>();
        game.getGamePlayers().forEach(gamePlayer -> finalList.addAll(SalvoLocationsList(gamePlayer.getSalvoes())));
        return finalList;
    }

    private Map<String, Object> ShipToDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getShipType());
        dto.put("locations", ship.getShipLocations());
        return dto;
    }

    private List<Map> ShipLocationsList(List<Ship> ships) {
        return ships.stream()
                .map(ship -> ShipToDTO(ship))
                .collect(Collectors.toList());
    }

    private Map<String, Object> SalvoToDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", salvo.getTurn());
        dto.put("player", salvo.getGamePlayer().getPlayer().getId());
        dto.put("locations", salvo.getSalvoLocations());
        return dto;
    }

    private List<Map> SalvoLocationsList(List<Salvo> salvos) {
        return salvos.stream()
                .map(salvo -> SalvoToDTO(salvo))
                .collect(Collectors.toList());
    }

    private Player getLoggedPlayer(Authentication authentication) {
        return playerRepository.findByEmail(authentication.getName());
    }

    private Map<String, Object> loggedInToDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getId());
        dto.put("name", player.getEmail());
        return dto;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        if (authentication == null){
            return new ResponseEntity<>(makeMap("error", "You can't create a New Game if You're Not Logged In! Please Log in or Sign Up for a new player account."), HttpStatus.UNAUTHORIZED);
        } else {
            Game newGame = gameRepository.save(new Game());
            GamePlayer newGameplayer = gamePlayerRepository.save(new GamePlayer(newGame, getLoggedPlayer(authentication)));
            return new ResponseEntity<>(makeMap("gpid", newGameplayer.getId()), HttpStatus.CREATED);
        }
    }

    @RequestMapping(path = "/game/{gameID}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gameID, Authentication authentication) {
        if (authentication == null){
            return new ResponseEntity<>(makeMap("error", "You can't join a Game if You're Not Logged In! Please Log in or Sign Up for a new player account."), HttpStatus.UNAUTHORIZED);
        }

        if (gameRepository.getOne(gameID) == null) {
                return new ResponseEntity<>(makeMap("error", "No such game."), HttpStatus.FORBIDDEN);
        }

        Game gameToJoin = gameRepository.getOne(gameID);
        Integer gamePlayersCount = gameToJoin.getGamePlayers().size();

        if (gamePlayersCount == 1) {
            GamePlayer newGameplayer = gamePlayerRepository.save(new GamePlayer(gameToJoin, getLoggedPlayer(authentication)));
            return new ResponseEntity<>(makeMap("gpid", newGameplayer.getId()), HttpStatus.CREATED);
        }
        if (gamePlayersCount == 2) {
            return new ResponseEntity<>(makeMap("error", "Game is full!"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(makeMap("error", "Something went wrong! Try again!"), HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "/games/players/{gamePlayerID}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> setShips(@PathVariable Long gamePlayerID, @RequestBody List <Ship> ships, Authentication authentication) {
        if (authentication == null){
            return new ResponseEntity<>(makeMap("error", "You can't place ships if You're Not Logged In! Please Log in or Sign Up for a new player account."), HttpStatus.UNAUTHORIZED);
        }
        GamePlayer gamePlayerToPlaceShips = gamePlayerRepository.getOne(gamePlayerID);
        if (gamePlayerToPlaceShips.getShips().size() >= 5) {
            return new ResponseEntity<>(makeMap("error", "Error: Your ships are already placed!"), HttpStatus.UNAUTHORIZED);
        }
        for (Ship item : ships) {
            item.setGamePlayerPlay(gamePlayerToPlaceShips);
            shipRepository.save(item);
        }
        return new ResponseEntity<>(makeMap("OK", "Ship positions saved successfully! "), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games/players/{gamePlayerID}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> setSalvoes(@PathVariable Long gamePlayerID, @RequestBody Salvo salvo, Authentication authentication) {
        if (authentication == null){
            return new ResponseEntity<>(makeMap("error", "You can't fire a Salvo if You're Not Logged In! Please Log in or Sign Up for a new player account."), HttpStatus.UNAUTHORIZED);
        }
        GamePlayer gamePlayerToFireSalvo = gamePlayerRepository.getOne(gamePlayerID);
        /*if (gamePlayerToPlaceShips.getShips().size() >= 5) {
            return new ResponseEntity<>(makeMap("error", "Error: Your ships are already placed!"), HttpStatus.UNAUTHORIZED);
        }*/
        salvo.setTurn(gamePlayerToFireSalvo.getSalvoes().size()+1);
        salvo.setGamePlayer(gamePlayerToFireSalvo);
        salvoRepository.save(salvo);

        return new ResponseEntity<>(makeMap("OK", "Salvo fired and saved successfully! "), HttpStatus.CREATED);
    }


    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String email, String password) {
        if (email.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
        }


        Player player = playerRepository.findByEmail(email);
        if (player != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.CONFLICT);
        }
        Player newPlayer = playerRepository.save(new Player(email, password));
        return new ResponseEntity<>(makeMap("id", newPlayer.getEmail()), HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private GamePlayer getOpponent(Game game, Long currentPlayerID){
        GamePlayer opponentGameplayer = new GamePlayer();
        for (GamePlayer item : game.getGamePlayers()) {
            Long playerIDtoCompare = item.getPlayer().getId();
            if (playerIDtoCompare != currentPlayerID) {
                opponentGameplayer = item;
            }
            }
            return opponentGameplayer;
    }

    private List<Map> getHits(GamePlayer gamePlayer, GamePlayer opponentGameplayer) {

        List<Map> hits = new ArrayList<>();

        Integer carrierDamage = 0;
        Integer battleshipDamage = 0;
        Integer submarineDamage = 0;
        Integer destroyerDamage = 0;
        Integer patrolboatDamage = 0;

        List <String> carrierLocation = new ArrayList<>();
        List <String> battleshipLocation = new ArrayList<>();
        List <String> submarineLocation = new ArrayList<>();
        List <String> destroyerLocation = new ArrayList<>();
        List <String> patrolboatLocation = new ArrayList<>();

        gamePlayer.getShips().forEach(ship -> {
                    switch (ship.getShipType()) {
                        case "carrier":
                            carrierLocation.addAll(ship.getShipLocations());
                            break;
                        case "battleship":
                            battleshipLocation.addAll(ship.getShipLocations());
                            break;
                        case "submarine":
                            submarineLocation.addAll(ship.getShipLocations());
                            break;
                        case "destroyer":
                            destroyerLocation.addAll(ship.getShipLocations());
                            break;
                        case "patrolboat":
                            patrolboatLocation.addAll(ship.getShipLocations());
                            break;
                    }
                });

        for (Salvo salvo : opponentGameplayer.getSalvoes()) {
            Integer carrierHitsInTurn = 0;
            Integer battleshipHitsInTurn = 0;
            Integer submarineHitsInTurn = 0;
            Integer destroyerHitsInTurn = 0;
            Integer patrolboatHitsInTurn = 0;

            Map<String, Object> hitsMapPerTurn = new LinkedHashMap<>();
            Map<String, Object> damagesPerTurn = new LinkedHashMap<>();

            List<String> salvoLocationsList = new ArrayList<>();
            List<String> hitCellsList = new ArrayList<>();
            salvoLocationsList.addAll(salvo.getSalvoLocations());
            for (String salvoShot : salvoLocationsList) {
                if (carrierLocation.contains(salvoShot)) {
                    carrierDamage++;
                    carrierHitsInTurn++;
                    hitCellsList.add(salvoShot);
                }
                if (battleshipLocation.contains(salvoShot)) {
                    battleshipDamage++;
                    battleshipHitsInTurn++;
                    hitCellsList.add(salvoShot);
                }
                if (submarineLocation.contains(salvoShot)) {
                    submarineDamage++;
                    submarineHitsInTurn++;
                    hitCellsList.add(salvoShot);
                }
                if (destroyerLocation.contains(salvoShot)) {
                    destroyerDamage++;
                    destroyerHitsInTurn++;
                    hitCellsList.add(salvoShot);
                }
                if (patrolboatLocation.contains(salvoShot)) {
                    patrolboatDamage++;
                    patrolboatHitsInTurn++;
                    hitCellsList.add(salvoShot);
                }
            }

            damagesPerTurn.put("carrierHits", carrierHitsInTurn);
            damagesPerTurn.put("battleshipHits", battleshipHitsInTurn);
            damagesPerTurn.put("submarineHits", submarineHitsInTurn);
            damagesPerTurn.put("destroyerHits", destroyerHitsInTurn);
            damagesPerTurn.put("patrolboatHits", patrolboatHitsInTurn);
            damagesPerTurn.put("carrier", carrierDamage);
            damagesPerTurn.put("battleship", battleshipDamage);
            damagesPerTurn.put("submarine", submarineDamage);
            damagesPerTurn.put("destroyer", destroyerDamage);
            damagesPerTurn.put("patrolboat", patrolboatDamage);

            hitsMapPerTurn.put("turn", salvo.getTurn());
            hitsMapPerTurn.put("hitLocations", hitCellsList);
            hitsMapPerTurn.put("damages", damagesPerTurn);
            hits.add(hitsMapPerTurn);
        }

        return hits;

    }

    private Enum getGameState (GamePlayer player) {

        if (player.getShips().size() == 0) {
            return gameState.PLACESHIPS;
        }
        if (player.getGame().getGamePlayers().size() == 1){
            return gameState.WAITINGFOROPP;
        }
        if (player.getGame().getGamePlayers().size() == 2) {
            GamePlayer opponentGp = getOpponent(player.getGame(), player.getPlayer().getId());
            if (player.getSalvoes().size() == opponentGp.getSalvoes().size() && player.getId() < opponentGp.getId()) {
                return gameState.PLAY;
            }
            if (player.getSalvoes().size() > opponentGp.getSalvoes().size()){
                return gameState.WAIT;
            }
        }
        return gameState.UNDEFINED;
    }



}