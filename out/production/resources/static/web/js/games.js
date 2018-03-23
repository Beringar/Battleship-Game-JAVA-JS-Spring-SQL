let gamesData;

function fetchJson(url, init) {
    return fetch(url, init).then(function(response) {
        if (response.ok) {
            return response.json();
        }
        throw new Error(response.statusText);
    });
}

fetchJson('/api/games').then(function(json) {
    // do something with the JSON
gamesData = json;
renderList(gamesData);
}).catch(function(error) {
    // do something getting JSON fails
});

function getItemHtml(game) {
    var gamePlayerItemHtml = getListGamePlayersHtml(game.gamePlayers);
    var gameItemCreationDate = new Date(game.created);
    return "<li>Game Id: " + game.id + " Creation date: " + gameItemCreationDate + "<ol>" + gamePlayerItemHtml + "</ol></li>";
}

function getListGamesHtml(gamesData) {
    return gamesData.map(getItemHtml).join("");
}

function getListGamePlayersHtml(gamePlayersData) {
    return gamePlayersData.map(getItemGamePlayerHtml).join("");
}

function getItemGamePlayerHtml(gamePlayer) {
    return "<li>GamePlayer Id: " + gamePlayer.id + " Join Date: " + gamePlayer.joinDate + " Player Id: " + gamePlayer.player.id + " email: " + gamePlayer.player.email + "</li>";
}

function renderList(gamesData) {
    var html = getListGamesHtml(gamesData);
    document.getElementById("gameList").innerHTML = html;
}

