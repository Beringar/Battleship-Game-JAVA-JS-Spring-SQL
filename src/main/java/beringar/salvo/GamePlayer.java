package beringar.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class GamePlayer {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private Date joinDate;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Player playerPlay;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Game gamePlay;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Ship> ships = new ArrayList<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Salvo> salvoes = new ArrayList<>();


    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public List<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(List<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    public GamePlayer() {}

    public GamePlayer(Game game, Player player) {
        this.joinDate = new Date();
        this.gamePlay = game;
        this.playerPlay = player;
    }


    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }


    public Game getGame() {
        return gamePlay;
    }

    public void setGame(Game game) {
        this.gamePlay = game;
    }


    public Player getPlayer() {
        return playerPlay;
    }

    public void setPlayer(Player player) {
        this.playerPlay = player;
    }

    public long getId() {
        return id;
    }

    public void addSalvo(Salvo salvo) {this.salvoes.add(salvo);}
}
