
var gameStarted = -1;

//~ ----------------------------------------------------------------------------------------------------------------

function showGameStartForm(contextPath, started) {
  
  if (gameStarted == -1 || gameStarted != started) {
    var text = '';
  
    text += '<form id="game_start_form" onsubmit="return startGame(\'' + contextPath + '\');">';
    text += '  <input type="submit" value="Start Game" />';
    text += '</form>';
  
    $("div#form_section").html(text);
    
    gameStarted = started;
  }
}

//~ ----------------------------------------------------------------------------------------------------------------

function showGameCancelForm(contextPath, started) {

  if (gameStarted == -1 || gameStarted != started) {
    var text = '';
    
    text += '<form id="game_cancel_form" onsubmit="return cancelGame(\'' + contextPath + '\');">';
    text += '  <b>Cancellation Code:</b>&nbsp;<input id="cancellation_code" type="text" value="" /><br />';
    text += '  <input type="submit" value="Cancel Game" />';
    text += '</form>';
  
    $("div#form_section").html(text);

    gameStarted = started;
  }
}

//~ ----------------------------------------------------------------------------------------------------------------

function updateView(contextPath) {

  $.ajax({
      url       : contextPath + "/status",
      success   : function(data, textStatus, jqXHR) {
        var started       = data['started'];
        var currentPlayer = data['currentPlayer'];
        var boardState    = data['boardState'];

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
              cellText = '<img alt="" src="' + contextPath + '/public/images/black.png" />' 
            } else if (cell == 2) {
              cellText = '<img alt="" src="' + contextPath + '/public/images/white.png" />'
            }

            $('#cell_' + alphabet.charAt(j) + (i + 1)).html(cellText);
          }
        }

        //text += '  <b>Current Player: </b>' + (currentPlayer == 1 ? "Black" : "White") + '<br />';
      }
    }
  );  
}

//~ ----------------------------------------------------------------------------------------------------------------

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

//~ ----------------------------------------------------------------------------------------------------------------

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