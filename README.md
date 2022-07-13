# mutiny-stackoverflow

## goal:
I want to get a certain amount of items from a stream and calculate an average from them.

## problem:
The streaming works fine with `Multi` but somehow the `Uni` in the `consumer` doesn't resolve and blocks the thread indefinitely.

## start:
```
./mvnw install
docker-compose up --build
```

- The `producer` starts with port 8080
- The `consumer` starts with port 8081
- You can send 10000 items via `http://localhost:8080/send/10000` in the browser to the `consumer`.
- To see the stream from the `consumer` call `http://localhost:8081/stream`
- The problem occurs with `http://localhost:8081/avg`
