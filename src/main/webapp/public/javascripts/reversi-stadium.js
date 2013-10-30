
var gameStarted = -1;

//~ --------------------------------------------------------------------------------------------------------------------

function showGameStartForm(contextPath, started) {
  
  if (gameStarted == -1 || gameStarted != started) {
    var text = '';
  
    text += '<form id="game_start_form" onsubmit="return startGame(\'' + contextPath + '\');">';
    text += '  <div><input type="submit" class="button" value="Start a New Game" /></div>';
    text += '</form>';
  
    $("div#form_section").html(text);
    $('.button').button();
    
    gameStarted = started;
  }
}

//~ --------------------------------------------------------------------------------------------------------------------

function showGameCancelForm(contextPath, started) {

  if (gameStarted == -1 || gameStarted != started) {
    var text = '';

    text += '<form id="game_cancel_form" onsubmit="return cancelGame(\'' + contextPath + '\');">';
    text += '  <div class="frame"><div class="label">Cancellation Code</div><div class="input"><input id="cancellation_code" class="ui-widget ui-state-default" type="text" value="" /></div></div>';
    text += '  <div style="margin-top: 20px;"><input type="submit" class="button" value="Cancel Running Game" /></div>';
    text += '</form>';
    text += '<br /><br />';
    text += '<form id="piece_move_form" onsubmit="return movePiece(\'' + contextPath + '\');">';
    text += '  <div class="frame"><div class="label">Player Code</div><div class="input"><input id="player_code" class="ui-widget ui-state-default" type="text" value="" /></div></div>';
    text += '  <div class="frame"><div class="label">Move Place</div><div class="input"><input id="move_place" class="ui-widget ui-state-default" type="text" value="" /></div></div>';
    text += '  <div style="margin-top: 20px;"><input type="submit" class="button" value="Move Piece" /></div>';
    text += '</form>';
  
    $("div#form_section").html(text);
    $('.button').button();

    gameStarted = started;
  }
}

//~ --------------------------------------------------------------------------------------------------------------------

function updateView(contextPath) {

  $.ajax({
      url       : contextPath + "/status",
      success   : function(data, textStatus, jqXHR) {
        var started       = data['started'];
        var cancelled     = data['cancelled'];
        var currentPlayer = data['currentPlayer'];
        var boardState    = data['boardState'];
        var blackCounter  = 0;
        var whiteCounter  = 0;

        if (started) {
          showGameCancelForm(contextPath, started);
        } else {
          showGameStartForm(contextPath, started);
        }       
        
        var alphabet = 'abcdefgh';
        
        for (var i = 0; i < boardState.length; i++) {
          var row = boardState[i];
          
          for (var j = 0; j < row.length; j++) {
            var cell     = boardState[i][j];
            var cellText = '&nbsp;'; 
            
            if (cell == 1) {
              blackCounter++;
              cellText = '<img alt="" src="' + contextPath + '/public/images/black.png" />' 
            } else if (cell == 2) {
              whiteCounter++;
              cellText = '<img alt="" src="' + contextPath + '/public/images/white.png" />'
            }

            $('#cell_' + alphabet.charAt(j) + (i + 1)).html(cellText);
          }
        }
        
        if (currentPlayer == 1) {
          $('#current_player').html("Black");
        } else if (currentPlayer == 2) {
          $('#current_player').html("White");
        } else {
          $('#current_player').html("No Active Game");
        }

        $('#game_cancelled').html(cancelled ? 'Yes' : 'No');
        $('#player_black').html(blackCounter);
        $('#player_white').html(whiteCounter);
      }
    }
  );  
}

//~ --------------------------------------------------------------------------------------------------------------------

function startGame(contextPath) {
  
  $.ajax({
      url       : contextPath + "/start",
      method    : "POST",
      statusCode: {
        400: function() {
          alert("Game already started!");
        },
        200: function(data) {        
          var alertText = '';

          showGameCancelForm(contextPath);
          
          alertText += 'Game Codes\n';
          alertText += '_______________________________________________\n';
          alertText += 'Black Player Auth Code\t\t\t: ' + data['playerBlackAuthCode'] + '\n';
          alertText += 'White Player Auth Code\t\t\t: ' + data['playerWhiteAuthCode'] + '\n';
          alertText += 'Game Cancellation Code\t\t\t: ' + data['cancellationCode'] + '\n';
          
          alert(alertText);
        }
      }
    }
  );

  return false;
}

//~ --------------------------------------------------------------------------------------------------------------------

function cancelGame(contextPath) {

  var cancellationCode = $('#cancellation_code').val();
  
  if (cancellationCode == null || cancellationCode.length != 16) {
    alert('Cancellation code is required!');
  } else {
    $.ajax({
          url       : contextPath + "/cancel/" + cancellationCode,
          method    : "DELETE",
          statusCode: {
            404: function() {
              alert("No active game play!");
            },
            403: function() {
              alert("Wrong cancellation code!");
            },
            200: function(data) {
              showGameStartForm(contextPath, data['started']);
            }
          }
        }
    );
  }

  return false;
}

//~ --------------------------------------------------------------------------------------------------------------------

function movePiece(contextPath) {

  var playerCode = $('#player_code').val();
  var movePlace  = $('#move_place').val();

  if (playerCode == null || playerCode.length != 8) {
    alert('Player code is required and should be in desired format!');
  } else if (movePlace == null || movePlace.length != 2) {
    alert('Move place is required and should be in desired format!');
  } else {
    $.ajax({
          url       : contextPath + "/move/" + playerCode + "/" + movePlace,
          method    : "PUT",
          statusCode: {
            404: function() {
              alert("No active game play!");
            },
            403: function() {
              alert("Wrong player code or wrong player order!");
            },
            400: function() {
              alert("Illegal move!");
            },
            200: function(data) {
              updateView(contextPath);
            }
          }
        }
    );
  }

  return false;
}