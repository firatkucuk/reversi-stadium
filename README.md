Reversi Stadium
===============
**Reversi Stadium** is simple reversi game server for hobby applications. It provides plain ReST API for client applications. Programmers can develop their own autonomous/manual programs that fight with the others. For now **Reversi Stadium** supports only one match at one time.

Compilation
-----------
**Reversi Stadium** is a typical maven web application. Therefore the compilation procedure is same as the other maven applications. After you clone/download the project source you may compile from command line. But before please ensure you have installed maven. Otherwise you should install maven for compilation.

Check the maven existence with `mvn --version` console command. If maven command exist you may compile **Reversi Stadium** easily. Please use `mvn clean compile` console command for compilation.

Running
-------
**Reversi Stadium** is standards based simple J2EE application. It uses basic Servlets and JSP files. So you can create a war package by using `mvn clean compile package`. Then you can easily deploy a J2EE application server.

An other easy running method is using embedded jetty maven plugin. You may run project easily by using `mvn jetty:run` command.

Game Creation
-------------
By default you can create a single game from index page of the application. Type `http://IP_ADDRESS:PORT/revesi-stadium/` to your favouite browser's address bar. Then click **"Start a New Game"** button.

### Game Creation Using ReST API

You can create a game using ReST API.

```
POST http://IP_ADDRESS:PORT/start/
```

`200` status code for succesful game creation. And a json response for additional authentication details about game. Test for game creation:


```
curl -i -H "Accept: application/json" -X POST http://localhost:8080/reversi-stadium/start

HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 330
Server: Jetty(9.0.0.M4)

{
  "cancellationCode": "yrlj6652jldt5583",
  "playerBlackAuthCode": "unhn9550",
  "playerWhiteAuthCode": "jkxg705",
  "reversiGame": {
    "boardState": [
      [0,0,0,0,0,0,0,0],
      [0,0,0,0,0,0,0,0],
      [0,0,0,0,0,0,0,0],
      [0,0,0,2,1,0,0,0],
      [0,0,0,1,2,0,0,0],
      [0,0,0,0,0,0,0,0],
      [0,0,0,0,0,0,0,0],
      [0,0,0,0,0,0,0,0]
    ],
    "cancelled": false,
    "currentPlayer": 1,
    "started": true
  }
}
```

**cancellationCode** is a mechanism for safely game cancellation. By using this code people that do not know code cannot cancel a running a game. And also player authentication codes are security codes for player moves. Only clients that know the appropriate player code can play.

`405` for wrong HTTP methods.

```
curl -i -H "Accept: application/json" -X GET http://localhost:8080/reversi-stadium/start

HTTP/1.1 405 Method Not Allowed
Content-Type: text/html; charset=ISO-8859-1
Cache-Control: must-revalidate,no-cache,no-store
Content-Length: 328
Server: Jetty(9.0.0.M4)

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Error 405 Method Not Allowed</title>
</head>
<body><h2>HTTP ERROR 405</h2>
<p>Problem accessing /reversi-stadium/start. Reason:
<pre>    Method Not Allowed</pre></p><hr><i><small>Powered by Jetty://</small></i><hr/>

</body>
</html>
```

`400` for already started games.

```
curl -i -H "Accept: application/json" -X POST http://localhost:8080/reversi-stadium/start

HTTP/1.1 400 Bad Request
Content-Type: text/html; charset=ISO-8859-1
Cache-Control: must-revalidate,no-cache,no-store
Content-Length: 314
Server: Jetty(9.0.0.M4)

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Error 400 Bad Request</title>
</head>
<body><h2>HTTP ERROR 400</h2>
<p>Problem accessing /reversi-stadium/start. Reason:
<pre>    Bad Request</pre></p><hr><i><small>Powered by Jetty://</small></i><hr/>

</body>
</html>
```

Running Game Cancellation
-------------------------

**Reversi Stadium** web application provides a securily game cancellation by using application main page. Type `http://IP_ADDRESS:PORT/revesi-stadium/` to your favouite browser's address bar. Then click **"Cancel Running Game"** button.



