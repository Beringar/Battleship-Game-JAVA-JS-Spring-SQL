package beringar.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
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




    @RequestMapping("/games")
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
    public Map<String, Object> getGameView(@PathVariable Long gamePlayerID) {
        GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerID);
        return GameViewToDTO(gamePlayer.getGame(), gamePlayer);
    }

    private Map<String, Object> GameViewToDTO(Game game, GamePlayer gamePlayer) {
        List<Ship> ships = gamePlayer.getShips();
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", GamePlayerList(game.getGamePlayers()));
        dto.put("ships", ShipLocationsList(ships));
        dto.put("salvoes", GetSalvosFromAllGamePlayers(game));
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










        /*Map<String, Object> gameViewMap = new LinkedHashMap<String, Object>();

        gameViewMap.put("id", gameplayer.getId());
        dto.put("player", PlayerToDTO(gameplayer.getPlayer()));
        dto.put("joinDate", gameplayer.getJoinDate());
        return dto;


        }*/






}