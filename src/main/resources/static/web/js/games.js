let gamesData;
let playersArray;

fetchJson('/api/games').then(function(json) {
    // do something with the JSON

    gamesData = json;
    showGamesTable(gamesData);
    addScoresToPlayersArray(getPlayers(gamesData));
    showScoreBoard(playersArray);

}).catch(function(error) {
    // do something getting JSON fails
});

function fetchJson(url) {
    return fetch(url, {
        method: 'GET',
            credentials: 'include'
    }).then(function(response) {
        if (response.ok) {
            return response.json();
        }
        throw new Error(response.statusText);
    });
}

function showGamesTable(gamesData){
    // var mytable = $('<table></table>').attr({id: "gamesTable", class: ""});
var table = "#gamesList";
    $('<tr><th>Game ID</th><th>Created</th><th>Player 1</th><th>Player 2</th><th>Game State</th></tr>').attr({class: ["class1"].join(' ')}).appendTo(table);
    for (var i = 0; i < gamesData.length; i++) {

        var DateCreated = new Date(gamesData[i].created);
        DateCreated = DateCreated.getMonth()+1 + "/" + DateCreated.getDate() + " " + DateCreated.getHours() + ":" + DateCreated.getMinutes();
        var row = $('<tr></tr>').attr({class: ["class1"].join(' ')}).appendTo(table);
        $('<td class="textCenter">' + gamesData[i].id + '</td>').appendTo(row);
        $('<td>' + DateCreated + '</td>').appendTo(row);


        for (var j = 0; j < gamesData[i].gamePlayers.length; j++) {

            if (gamesData[i].gamePlayers.length == 2) {
                $('<td>' + gamesData[i].gamePlayers[j].player.email + '</td>').appendTo(row);
            }
            if (gamesData[i].gamePlayers.length == 1) {
                $('<td>' + gamesData[i].gamePlayers[0].player.email + '</td><td>Waiting for player</td>').appendTo(row);
            }
        }
        $('<td>Game State (to do)</td>').appendTo(row);
    }
    // mytable.appendTo('#gamesList');
}

function getPlayers(gamesData) {

    playersArray = [];
    var playersIds = [];

    for (var i = 0; i < gamesData.length; i++) {

        for (var j = 0; j < gamesData[i].gamePlayers.length; j++) {

            if (!playersIds.includes(gamesData[i].gamePlayers[j].player.id)) {
                playersIds.push(gamesData[i].gamePlayers[j].player.id);
                var playerScoreData = {
                    "id": gamesData[i].gamePlayers[j].player.id,
                    "email": gamesData[i].gamePlayers[j].player.email,
                    "scores": [],
                    "total" : 0.0
                };
                playersArray.push(playerScoreData);
                console.log("player Added");
            }
        }
    }
    return playersArray;
}

function addScoresToPlayersArray(playersArray) {

    for (var i = 0; i < gamesData.length; i++) {

        for (var j = 0; j < gamesData[i].scores.length; j++) {

            var scorePlayerId = gamesData[i].scores[j].playerID;

            for (var k = 0; k < playersArray.length; k++) {

                if (playersArray[k].id == scorePlayerId) {
                    playersArray[k].scores.push(gamesData[i].scores[j].score);
                    playersArray[k].total += gamesData[i].scores[j].score;
                }
            }
        }
    }
}

function showScoreBoard (playersArray) {

    playersArray.sort(function(a, b) {
        return b.total - a.total;
    });

    var table = "#scoreBoard";
    $('<tr><th>Name</th><th>Total</th><th>Won</th><th>Lost</th><th>Tied</th></tr>').attr({class: ["class1"].join(' ')}).appendTo(table);

    for (var m = 0; m < playersArray.length; m++) {
        var countWon = 0;
        var countLost = 0;
        var countTied = 0;

        if(playersArray[m].scores.length > 0) {

            for (var n = 0; n < playersArray[m].scores.length; n++) {
                if (playersArray[m].scores[n] == 0.0) {
                    countLost++;
                } else if (playersArray[m].scores[n] == 0.5) {
                    countTied++;
                } else if (playersArray[m].scores[n] == 1.0) {
                    countWon++;
                }
            }

            var row = $('<tr></tr>').attr({class: ["class1"].join(' ')}).appendTo(table);
            $('<td>' + playersArray[m].email + '</td>').appendTo(row);
            $("<td class='textCenter'>" + playersArray[m].total.toFixed(1) + '</td>').appendTo(row);
            $("<td class='textCenter'>" + countWon + '</td>').appendTo(row);
            $("<td class='textCenter'>" + countLost + '</td>').appendTo(row);
            $("<td class='textCenter'>" + countTied + '</td>').appendTo(row);
        }
    }
}







