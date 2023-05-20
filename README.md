# Getting Started

### How to run
```shell
cd url-shortener
./gradlew bootJar
docker-compose up
```

### API details

1. http://localhost:9000/api/v1/shorten

API which allows to get a short version of a particular URL

**Request POST**

```json
{
  "url": "https://github.com"
}
```

**Response**

```json
{
  "longUrl": "https://github.com",
  "shortUrl": "http://localhost:9000/If3MU"
}
```

2. http://localhost:9000/api/v1/original

API which allows to get the original version of a short URL

**Request POST**

```json
{
"shortUrl": "http://localhost:9000/If3MU"
}
```

**Response**

```json
{
  "longUrl": "https://github.com",
  "shortUrl": "http://localhost:9000/If3MU"
}
```

3. API which allows to get a redirect to the full URL (302)

http://localhost:9000/If3MU


### To think

- More robust Unique id generator [currentTime + host + seed]
- Cache