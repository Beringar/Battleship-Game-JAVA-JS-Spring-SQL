package beringar.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
public class Ship {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String shipType;



    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="shipLocation")
    private List<String> shipLocations = new ArrayList<>();

    public Ship (){}

    public Ship (String type, GamePlayer gamePlayer, List<String> shipLocations) {
        this.shipType = type;
        this.gamePlayer = gamePlayer;
        this.shipLocations = shipLocations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public GamePlayer getGamePlayerPlay() {
        return gamePlayer;
    }

    public void setGamePlayerPlay(GamePlayer gamePlayerPlay) {
        this.gamePlayer = gamePlayerPlay;
    }

    public List<String> getShipLocations() {
        return shipLocations;
    }

    public void setShipLocations(List<String> shipLocations) {
        this.shipLocations = shipLocations;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
                ", shipType='" + shipType + '\'' +
                ", gamePlayerPlay=" + gamePlayer +
                ", shipLocations=" + shipLocations +
                '}';
    }
}
