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
Content-Length: 370
Server: Jetty(8.1.8.v20121106)

{
  "cancellationCode": "qxcr2615fxqn6056",
  "playerBlackAuthCode": "gigq9736",
  "playerWhiteAuthCode": "jeqw5369",
  "reversiGame": {
    "boardState":[
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
    "started":true,
    "availableMoves": ["d3","c4","e6","f5"]
  }
}
```

**cancellationCode** is a mechanism for safely game cancellation. By using this code people that do not know code cannot cancel a running a game. And also player authentication codes are security codes for player moves. Only clients that know the appropriate player code can play.

`400` for already started games.

```
curl -i -H "Accept: application/json" -X POST http://localhost:8080/reversi-stadium/start

HTTP/1.1 400 Bad Request
Date: Wed, 06 Nov 2013 13:50:23 GMT
Content-Type: text/html;charset=ISO-8859-1
Cache-Control: must-revalidate,no-cache,no-store
Content-Length: 1390
Server: Jetty(8.1.8.v20121106)

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Error 400 Bad Request</title>
</head>
<body><h2>HTTP ERROR 400</h2>
<p>Problem accessing /reversi-stadium/start. Reason:
<pre>    Bad Request</pre></p><hr /><i><small>Powered by Jetty://</small></i><br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                

</body>
</html>
```

`405` for wrong HTTP methods.

```
curl -i -H "Accept: application/json" -X GET http://localhost:8080/reversi-stadium/start

HTTP/1.1 405 Method Not Allowed
Date: Wed, 06 Nov 2013 13:49:33 GMT
Content-Type: text/html;charset=ISO-8859-1
Cache-Control: must-revalidate,no-cache,no-store
Content-Length: 1404
Server: Jetty(8.1.8.v20121106)

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Error 405 Method Not Allowed</title>
</head>
<body><h2>HTTP ERROR 405</h2>
<p>Problem accessing /reversi-stadium/start. Reason:
<pre>    Method Not Allowed</pre></p><hr /><i><small>Powered by Jetty://</small></i><br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                

</body>
</html>
```

Running Game Cancellation
-------------------------

**Reversi Stadium** web application provides a securily game cancellation by using application main page. Type `http://IP_ADDRESS:PORT/revesi-stadium/` to your favouite browser's address bar. Then click **"Cancel Running Game"** button.


```
DELETE http://IP_ADDRESS:PORT/cancel/cancellationCode
```

`200` game successfully cancelled. Returns JSON status data.

```
curl -i -H "Accept: application/json" -X DELETE http://localhost:8080/reversi-stadium/cancel/abcd1234efgh5678

HTTP/1.1 200 OK
Date: Wed, 06 Nov 2013 13:51:01 GMT
Content-Type: application/json
Content-Length: 233
Server: Jetty(8.1.8.v20121106)

{
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
  "cancelled": true,
  "currentPlayer": 1,
  "started": false,
  "availableMoves":null
}
```

`403` Wrong cancellation code.

```
curl -i -H "Accept: application/json" -X DELETE http://localhost:8080/reversi-stadium/cancel/abcd1234efgh5678

HTTP/1.1 403 Forbidden
Date: Wed, 06 Nov 2013 13:53:42 GMT
Content-Length: 0
Server: Jetty(8.1.8.v20121106)
```

`404` No active running game.

```
curl -i -H "Accept: application/json" -X DELETE http://localhost:8080/reversi-stadium/cancel/abcd1234efgh5678

HTTP/1.1 404 Not Found
Date: Wed, 06 Nov 2013 13:53:06 GMT
Content-Length: 0
Server: Jetty(8.1.8.v20121106)
```

`405` for wrong HTTP methods.

```
curl -i -H "Accept: application/json" -X GET http://localhost:8080/reversi-stadium/cancel/abcd1234efgh5678

HTTP/1.1 405 Method Not Allowed
Date: Wed, 06 Nov 2013 14:01:39 GMT
Content-Type: text/html;charset=ISO-8859-1
Cache-Control: must-revalidate,no-cache,no-store
Content-Length: 1422
Server: Jetty(8.1.8.v20121106)

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Error 405 Method Not Allowed</title>
</head>
<body><h2>HTTP ERROR 405</h2>
<p>Problem accessing /reversi-stadium/cancel/abcd1234efgh5678. Reason:
<pre>    Method Not Allowed</pre></p><hr /><i><small>Powered by Jetty://</small></i><br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                

</body>
</html>
```


Moving Pieces
-------------

```
PUT http://IP_ADDRESS:PORT/move/playerAuthenticationCode/location
```

`200` Piece succesfully moved.

```
curl -i -H "Accept: application/json" -X PUT http://localhost:8080/reversi-stadium/move/yyei8136/d3

HTTP/1.1 200 OK
Date: Wed, 06 Nov 2013 13:54:46 GMT
Content-Type: application/json
Content-Length: 245
Server: Jetty(8.1.8.v20121106)

{
  "boardState": [
    [0,0,0,0,0,0,0,0],
    [0,0,0,0,0,0,0,0],
    [0,0,0,1,0,0,0,0],
    [0,0,0,1,1,0,0,0],
    [0,0,0,1,2,0,0,0],
    [0,0,0,0,0,0,0,0],
    [0,0,0,0,0,0,0,0],
    [0,0,0,0,0,0,0,0]
  ],
  "cancelled": false,
  "currentPlayer": 2,
  "started": true,
  "availableMoves": ["e3","c5","c3"]
}
```

`400` Illegal move

```
curl -i -H "Accept: application/json" -X PUT http://localhost:8080/reversi-stadium/move/zood2291/d8

HTTP/1.1 400 Bad Request
Date: Wed, 06 Nov 2013 13:58:53 GMT
Content-Length: 0
Server: Jetty(8.1.8.v20121106)
```

`403` Wrong authentication code or wrong player order


```
curl -i -H "Accept: application/json" -X PUT http://localhost:8080/reversi-stadium/move/yyei8136/d3

HTTP/1.1 403 Forbidden
Date: Wed, 06 Nov 2013 13:58:06 GMT
Content-Length: 0
Server: Jetty(8.1.8.v20121106)
```


`404` No active running game.


```
curl -i -H "Accept: application/json" -X PUT http://localhost:8080/reversi-stadium/move/yyei8136/d3

HTTP/1.1 404 Not Found
Date: Wed, 06 Nov 2013 13:56:52 GMT
Content-Length: 0
Server: Jetty(8.1.8.v20121106)
```

`405` Wrong http method

```
curl -i -H "Accept: application/json" -X GET http://localhost:8080/reversi-stadium/move/yyei8136/d3

HTTP/1.1 405 Method Not Allowed
Date: Wed, 06 Nov 2013 14:00:02 GMT
Content-Type: text/html;charset=ISO-8859-1
Cache-Control: must-revalidate,no-cache,no-store
Content-Length: 1415
Server: Jetty(8.1.8.v20121106)

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Error 405 Method Not Allowed</title>
</head>
<body><h2>HTTP ERROR 405</h2>
<p>Problem accessing /reversi-stadium/move/yyei8136/d3. Reason:
<pre>    Method Not Allowed</pre></p><hr /><i><small>Powered by Jetty://</small></i><br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                
<br/>                                                

</body>
</html>
```

Getting Game Status
-------------------

```
GET http://IP_ADDRESS:PORT/status
```

`200` Returns json status

```
curl -i -H "Accept: application/json" -X GET http://localhost:8080/reversi-stadium/status

HTTP/1.1 200 OK
Date: Wed, 06 Nov 2013 14:03:41 GMT
Content-Type: application/json
Content-Length: 250
Server: Jetty(8.1.8.v20121106)


{
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
  "started": true,
  "availableMoves" :["d3","c4","e6","f5"]
}
```

`405` Wrong HTTP method


```
curl -i -H "Accept: application/json" -X PUT http://localhost:8080/reversi-stadium/status

HTTP/1.1 405 Method Not Allowed
Date: Wed, 06 Nov 2013 14:05:02 GMT
Content-Length: 0
Server: Jetty(8.1.8.v20121106)
```
