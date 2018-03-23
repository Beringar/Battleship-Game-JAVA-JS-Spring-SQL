var player1 = "p1_";
var player2 = "p2_";
var gamePlayerData = {};

doAjax(makeUrl());


function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

function makeUrl() {
    var gamePlayerID =  getParameterByName("gp");
    return '/api/game_view/' + gamePlayerID;
}

function doAjax(_url) {
    $.ajax({
        url: _url,
        type: 'GET',
        success: function (data) {
            gamePlayerData = data;
            console.log(gamePlayerData);
            createTable(player1);
            createTable(player2);
            showSelf(gamePlayerData);
        }
    });
}

function showSelf (gamePlayerData) {
    var you = "";
    var viewer = "";
    var youID = "";

    gamePlayerData.gamePlayers.forEach(function(gamePlayer) {
        if (gamePlayer.id == getParameterByName("gp")) {
            you = gamePlayer.player.email;
            youID = gamePlayer.player.id;
        } else {
            viewer = gamePlayer.player.email;
        }
    });

    $('#gamePlayerDetails').html("Game created: " + new Date(gamePlayerData.created) + "<br>Players:<br>" + you + " (you) vs. " + viewer);

    gamePlayerData.ships.forEach(function(ship) {

        console.log(ship.type);
        ship.locations.forEach(function(location) {
            var cellID = "#p1_" + location;
            $(cellID).addClass("shipCell");
            console.log(location);
        });
    });

    gamePlayerData.salvoes.forEach(function(salvo) {

        console.log("Turn: " + salvo.turn);
        salvo.locations.forEach(function(location) {
            var cellID;
            if (salvo.player == youID){
                cellID = "#p2_" + location;
                $(cellID).addClass("salvoCell");

                console.log("Your salvo on " + location);
            } else {
                cellID = "#p1_" + location;
                if ($(cellID).hasClass("shipCell")) {
                    $(cellID).addClass("hitCell");

                    console.log("Opponent Hits Ship on " + location);
                } else {
                    $(cellID).addClass("salvoCell");

                    console.log("Opponent salvo on " + location);
                }
            }
            $(cellID).text(salvo.turn);
        });
    });
}

function createTable(player) {
    var prova = 0;
    var l = 0;
    var gridLabel;
    var gridId;
    if (player == "p1_") {
        gridLabel = $('<p class="gridLabel">Self grid</p>');
        gridId = "#p1Grid";
    } else {
        gridLabel = $('<p class="gridLabel">Opponent grid</p>');
        gridId = "#p2Grid";
    }
    var mytable = $('<table></table>').attr({
        id: "basicTable",
        class: ""
    });
    var rows = 10;
    var cols = 10;
    var tr = [];

    for (var i = 0; i <= rows; i++) {
        var row = $('<tr></tr>').attr({
            class: ["class1"].join(' ')
        }).appendTo(mytable);
        if (i == 0) {
            for (var j = 0; j < cols + 1; j++) {
                $('<th></th>').text(j).attr({
                    class: ["info"]
                }).appendTo(row);
            }
        } else {
            for (var j = 0; j < cols; j++) {
                if (j == 0) {
                    $('<th></th>').text(String.fromCharCode(65+(l++))).attr({
                        class: ["info"]
                    }).appendTo(row);
                }
                $('<td></td>').attr('id', player + String.fromCharCode(65+(i-1)) + "" + (j+1)).appendTo(row);

            }
        }
    }

    gridLabel.appendTo(gridId);
    mytable.appendTo(gridId);
}



