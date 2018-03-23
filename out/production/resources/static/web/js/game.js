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
            shipLocationsInRed(gamePlayerData);
        }
    });
}

function shipLocationsInRed (gamePlayerData) {
    var you = "";
    var viewer = "";

    gamePlayerData.gamePlayers.forEach(function(gamePlayer) {
        if (gamePlayer.id == getParameterByName("gp")) {
            you = gamePlayer.player.email;
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

}

function createTable(player) {
    var prova = 0;
    var l = 0;
    var mytable = $('<table></table>').attr({
        id: "basicTable",
        class: "table table-hover"
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

    mytable.appendTo("#p1Grid");
}



