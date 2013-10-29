<%@ page contentType="textxhtml;charset=UTF-8" language="java" %>

<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>Reversi Stadium</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <script type="text/javascript" src="${requestScope['contextPath']}/public/javascripts/jquery-2.0.3.min.js"></script>
    <script type="text/javascript" src="${requestScope['contextPath']}/public/javascripts/reversi-stadium.js"></script>
    <link type="text/css" rel="stylesheet" href="${requestScope['contextPath']}/public/stylesheets/reversi-stadium.css" />
    <script type="text/javascript">
      $(document).ready(function() {
          updateView('${requestScope['contextPath']}');

          setInterval(
            function() {
              updateView('${requestScope['contextPath']}');
            },
            5000
          );    
      });
    </script>
  </head>
  <body>
    <div id="main_container">
      <h1>Reversi Stadium</h1>
      <br />
      <div id="board_section">
        <table id="reversi_board" cellpadding="0" cellspacing="1" border="1">
          <tr>
            <td>&nbsp;</td>
            <td>a</td>
            <td>b</td>
            <td>c</td>
            <td>d</td>
            <td>e</td>
            <td>f</td>
            <td>g</td>
            <td>h</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td>1</td>
            <td class="board_cell"><span id="cell_a1">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_b1">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_c1">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_d1">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_e1">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_f1">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_g1">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_h1">&nbsp;</span></td>
            <td>1</td>
          </tr>
          <tr>
            <td>2</td>
            <td class="board_cell"><span id="cell_a2">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_b2">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_c2">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_d2">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_e2">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_f2">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_g2">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_h2">&nbsp;</span></td>
            <td>2</td>
          </tr>
          <tr>
            <td>3</td>
            <td class="board_cell"><span id="cell_a3">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_b3">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_c3">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_d3">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_e3">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_f3">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_g3">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_h3">&nbsp;</span></td>
            <td>3</td>
          </tr>
          <tr>
            <td>4</td>
            <td class="board_cell"><span id="cell_a4">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_b4">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_c4">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_d4">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_e4">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_f4">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_g4">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_h4">&nbsp;</span></td>
            <td>4</td>
          </tr>
          <tr>
            <td>5</td>
            <td class="board_cell"><span id="cell_a5">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_b5">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_c5">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_d5">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_e5">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_f5">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_g5">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_h5">&nbsp;</span></td>
            <td>5</td>
          </tr>
          <tr>
            <td>6</td>
            <td class="board_cell"><span id="cell_a6">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_b6">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_c6">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_d6">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_e6">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_f6">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_g6">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_h6">&nbsp;</span></td>
            <td>6</td>
          </tr>
          <tr>
            <td>7</td>
            <td class="board_cell"><span id="cell_a7">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_b7">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_c7">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_d7">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_e7">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_f7">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_g7">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_h7">&nbsp;</span></td>
            <td>7</td>
          </tr>
          <tr>
            <td>8</td>
            <td class="board_cell"><span id="cell_a8">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_b8">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_c8">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_d8">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_e8">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_f8">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_g8">&nbsp;</span></td>
            <td class="board_cell"><span id="cell_h8">&nbsp;</span></td>
            <td>8</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>a</td>
            <td>b</td>
            <td>c</td>
            <td>d</td>
            <td>e</td>
            <td>f</td>
            <td>g</td>
            <td>h</td>
            <td>&nbsp;</td>
          </tr>
        </table>
      </div>

      <br />

      <div id="form_section" />
    </div>
  </body>
</html>
