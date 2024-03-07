const net = require('net');

// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
  connection.on('data', () => {
    let content = `<!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8" />
        </head>
        <body>
            WebSocket test page
            <script>
            let ws = new WebSocket('ws://localhost:3001');
            ws.onmessage = event => alert('Message from server: ' + event.data);
            ws.onopen = () => ws.send('hello');
            </script>
        </body>
        </html>
        `;
    connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content);
  });
});

httpServer.listen(3000, () => {
  console.log('HTTP server listening on port 3000');
});

// Incomplete WebSocket server
const wsServer = net.createServer((connection) => {
  console.log('\nClient connected!\n');

  //TODO: handle WebSocket connections
  wsServer.connections = [];
    wsServer.on('connection', (connection) => {
        wsServer.connections.push(connection);
    });

    connection.on('data', (data) => {
        console.log('Data received from client: \n' + data.toString());
        const dataString = data.toString();
        if (dataString.includes('GET')) {
            const response = 
            "HTTP/1.1 101 Switching Protocols\n" + 
            "Upgrade: websocket\n" +
            "Connection: Upgrade\n";

            connection.write(response);
        } else {
            wsServer.connections.forEach((connection) => {
                if (connection !== wsServer) {
                    connection.write(buffer);
                }
            });
        }
    });

  connection.on('end', () => {
    console.log('Client disconnected\n');
    index = wsServer.connections.indexOf(connection);
    if (index > -1) {
      wsServer.connections.splice(index, 1);
    }
  });
});

wsServer.on('error', (error) => {
  console.error('Error: ', error);
});

wsServer.listen(3001, () => {
  console.log('WebSocket server listening on port 3001');
});




