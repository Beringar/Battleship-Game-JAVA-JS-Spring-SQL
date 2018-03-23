package beringar.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String email;

    @OneToMany(mappedBy="playerPlay", fetch=FetchType.EAGER)
    private List<GamePlayer> gamePlayers = new ArrayList<>();

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private List<Score> scores = new ArrayList<>();

    public long getId() {
        return id;
    }
    @JsonIgnore
    public List<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGamePlayers(List<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Player() {}

    public Player(String email) {
        this.email = email;
    }

    public String toString() {
        return email;
    }

    public void addGamePlay(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    public Map<String, Object> toDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("email", this.getEmail());
        return dto;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }
}
