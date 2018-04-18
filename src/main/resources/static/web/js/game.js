// var player1 = "p1_";
// var player2 = "p2_";
var gamePlayerData = {};
var errorMsg;
var you = "";
var viewer = "";
var youID = "";
var salvoPositions = [];
var salvoJSON;

doAjax(makeUrl());

$('#logout-form').on('submit', function (event) {
    event.preventDefault();
    $.post("/api/logout")
        .done(function () {
            console.log("logout ok");
            $('#logoutSuccess').show("slow").delay(2000).hide("slow");
            setTimeout(
                function()
                {
                    location.href = "/web/games.html";
                }, 3000);
        })
        .fail(function () {
            console.log("logout fails");
        })
        .always(function () {

        });
});

$('#postSalvo').click(function () {
    makeSalvoJSON();
    if (salvoPositions.length == 0){
        console.log("No salvos to shoot!");
    } else {
        postSalvo(makePostUrlSalvoes());
    }
});

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

function makeUrl() {
    var gamePlayerID =  getParameterByName("gp");
    return '/api/game_view/' + gamePlayerID;
}

function makePostUrl() {
    var gamePlayerID =  getParameterByName("gp");
    return '/api/games/players/' + gamePlayerID + '/ships';
}

function makePostUrlSalvoes() {
    var gamePlayerID =  getParameterByName("gp");
    return '/api/games/players/' + gamePlayerID + '/salvoes';
}

function doAjax(_url) {
    $.ajax({
        url: _url,
        type: 'GET',
        success: function (data) {
            gamePlayerData = data;
            // console.log(gamePlayerData);
            // createTable(player1);
            // createTable(player2);
            showSelf(gamePlayerData);
            if (gamePlayerData.gameState == "PLACESHIPS"){
                $('#placingShipsBoard').show('puff', 'slow');
            }
            makeGameRecordTable(gamePlayerData.hits.opponent, "gameRecordOppTable");
            makeGameRecordTable(gamePlayerData.hits.self, "gameRecordSelfTable");

        },
        error: function(e){
            console.log(e);
            errorMsg = e.responseJSON.error;
            console.log(errorMsg);
            $('#errorMsg').text("Error: " + errorMsg);
            $('#errorMsg').show( "slow" ).delay(5000).hide( "slow" );
        }
    });
}

function showSelf (gamePlayerData) {
    you = "";
    viewer = "";
    youID = "";

    gamePlayerData.gamePlayers.forEach(function(gamePlayer) {
        if (gamePlayer.id == getParameterByName("gp")) {
            you = gamePlayer.player.email;
            youID = gamePlayer.player.id;
        } else {
            viewer = gamePlayer.player.email;
            $('#OpponentPlayerName').removeClass('waitingPlayer');
        }
    });

    if (viewer === "") {
        viewer = "Waiting for player!";
        $('#OpponentPlayerName').addClass('waitingPlayer');
    }

    let DateCreated = new Date(gamePlayerData.created);
    DateCreated = DateCreated.getMonth() + 1 + "/" + DateCreated.getDate() + " " + DateCreated.getHours() + ":" + DateCreated.getMinutes();
    $('#gamePlayerDetails').html('<span class="labelGame">Game ID: </span><span class="labelGameBig">' + gamePlayerData.id + '</span><span class="labelGame"> Created: </span><span class="labelGameBig">' + DateCreated + '</span>');
    $('#currentPlayerName').text(you);
    $('#OpponentPlayerName').text(viewer);


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
                cellID = "#" + location;
                $(cellID).addClass("salvoCell");

                console.log("Your salvo on " + location);
                $(cellID).text(salvo.turn);
            } else {
                cellID = "#p1_" + location;
                if ($(cellID).hasClass("shipCell")) {
                    $(cellID).addClass("hitCell");

                    console.log("Opponent Hits Ship on " + location);
                } else {
                    $(cellID).addClass("salvoCell");
                    $(cellID).text(salvo.turn);
                    console.log("Opponent salvo on " + location);
                }
            }

        });
    });

    gamePlayerData.hits.opponent.forEach(function(playTurn) {
        playTurn.hitLocations.forEach(function (hitCell) {
            cellID = "#" + hitCell;
            $(cellID).addClass("hitCell");
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

function postShipLocations (postUrl) {
    $.post({
        url: postUrl,
        data: shipsJSON,
        dataType: "text",
        contentType: "application/json"
    })
        .done(function (response) {
            console.log(response);
            $('#okShips').text(JSON.parse(response).OK);
            $('#okShips').show( "slow" ).delay(4000).hide( "slow" );
        })
        .fail(function (response) {
            console.log(response);
            $('#errorShips').text(JSON.parse(response.responseText).error);
            $('#errorShips').show( "slow" ).delay(4000).hide( "slow" );
        })
}

function postSalvo (postUrl) {
    $.post({
        url: postUrl,
        data: salvoJSON,
        dataType: "text",
        contentType: "application/json"
    })
        .done(function (response) {
            console.log(response);
            $('#okShips').text(JSON.parse(response).OK);
            $('#okShips').show( "slow" ).delay(4000).hide( "slow" );
        })
        .fail(function (response) {
            console.log(response);
            $('#errorShips').text(JSON.parse(response.responseText).error);
            $('#errorShips').show( "slow" ).delay(4000).hide( "slow" );
        })
}

function displayOverlay(text) {
    $("<table id='overlay'><tbody><tr><td>" + text + "<br><button class='btn btn-info' onclick='removeOverlay()'>Ok! I got it.</button> </td></tr></tbody></table>").css({
        "position": "absolute",
        "top": "0px",
        // "left": "0px",
        "width": "451px",
        "height": "451px",
        "background-color": "rgba(133,43,0,.82)",
        "z-index": "10000",
        "vertical-align": "middle",
        "text-align": "center",
        "color": "#fff",
        "font-size": "40px",
        "font-weight": "bold",
        "cursor": "wait"
    }).appendTo(".gridShips").effect( "bounce", { times: 5 }, { distance: 20 }, "slow" );
}

function removeOverlay() {

    $("#overlay").hide('puff', 'slow', function(){ $("#overlay").remove(); });
}

function makeSalvoJSON() {
    salvoPositions = [];
    salvoObject = {};
    if (salvo1cellID !== "salvoout1" && salvo1cellID !== "salvoout2" && salvo1cellID !== "salvoout3" && salvo1cellID !== "salvoout4" && salvo1cellID !== "salvoout5") {
        salvoPositions.push(salvo1cellID);
    }
    if (salvo2cellID !== "salvoout1" && salvo2cellID !== "salvoout2" && salvo2cellID !== "salvoout3" && salvo2cellID !== "salvoout4" && salvo2cellID !== "salvoout5") {
        salvoPositions.push(salvo2cellID);
    }
    if (salvo3cellID !== "salvoout1" && salvo3cellID !== "salvoout2" && salvo3cellID !== "salvoout3" && salvo3cellID !== "salvoout4" && salvo3cellID !== "salvoout5") {
        salvoPositions.push(salvo3cellID);
    }
    if (salvo4cellID !== "salvoout1" && salvo4cellID !== "salvoout2" && salvo4cellID !== "salvoout3" && salvo4cellID !== "salvoout4" && salvo4cellID !== "salvoout5") {
        salvoPositions.push(salvo4cellID);
    }
    if (salvo5cellID !== "salvoout1" && salvo5cellID !== "salvoout2" && salvo5cellID !== "salvoout3" && salvo5cellID !== "salvoout4" && salvo5cellID !== "salvoout5") {
        salvoPositions.push(salvo5cellID);
    }
    salvoObject = {
        salvoLocations : salvoPositions
    }

    salvoJSON = JSON.stringify(salvoObject);
    console.log(salvoJSON);
}

function makeGameRecordTable (hitsArray, gameRecordTableId) {

    var tableId = "#" + gameRecordTableId + " tbody";
    let shipsAfloat = 5;
    hitsArray.forEach(function (playTurn) {
        let hitsReport = "";
        if (playTurn.damages.carrierHits > 0){
            hitsReport += "Carrier " + addDamagesIcons(playTurn.damages.carrierHits) + " ";
            if (playTurn.damages.carrier === 5){
                hitsReport += "Sunk!! ";
                shipsAfloat--;
            }
        }
        if (playTurn.damages.battleshipHits > 0){
            hitsReport += "Battleship " + addDamagesIcons(playTurn.damages.battleshipHits) + " ";
            if (playTurn.damages.battleship === 4){
                hitsReport += "Sunk!! ";
                shipsAfloat--;
            }
        }
        if (playTurn.damages.submarineHits > 0){
            hitsReport += "Submarine " + addDamagesIcons(playTurn.damages.submarineHits) + " ";
            if (playTurn.damages.submarine === 3){
                hitsReport += "Sunk!! ";
                shipsAfloat--;
            }
        }
        if (playTurn.damages.destroyerHits > 0){
            hitsReport += "Destroyer " + addDamagesIcons(playTurn.damages.destroyerHits) + " ";
            if (playTurn.damages.destroyer === 3){
                hitsReport += "Sunk!! ";
                shipsAfloat--;
            }
        }
        if (playTurn.damages.patrolboatHits > 0){
            hitsReport += "Patrol Boat " + addDamagesIcons(playTurn.damages.patrolboatHits) + " ";
            if (playTurn.damages.patrolboat === 2){
                hitsReport += "Sunk!! ";
                shipsAfloat--;
            }
        }

        if (hitsReport === ""){
            hitsReport = "All salvoes missed! No damages!"
        }
        $('<tr><td class="textCenter">' + playTurn.turn + '</td><td>' + hitsReport + '</td><td class="textCenter">' + shipsAfloat + '</td></tr>').prependTo(tableId);
    });
}

function addDamagesIcons (numberOfHits) {
    let damagesIcons = "";
    for (var i = 0; i < numberOfHits; i++) {
        damagesIcons += "<img class='hitblast' src='img/hitblast.png'>"
    }
    return damagesIcons;
}





