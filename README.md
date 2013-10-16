# QCast - InfoQ presentation podcast

A podcast feed for InfoQ presentations.

## Installation

Either using Leiningen:

    $ lein run

Or using Java:

    $ java -jar qcast-0.1.0-standalone.jar

## Usage

The Overview page can be reached at:
[http://qcast.herokuapp.com/](remote) or
[http://localhost:8080/](local).

The Feed can be reached at: [http://qcast.herokuapp.com/feed](remote)
 or [http://localhost:8080/feed](local).

## Components

The application currently uses
[HTTP-Kit](https://github.com/http-kit/http-kit) as server instead of
[Ring](https://github.com/ring-clojure/ring), but there is not really reason
for either choice.

For HTML scraping is done using [Enlive](https://github.com/cgrand/enlive)
and the XML feeds are produced using
[Hiccup](https://github.com/weavejester/hiccup).

For logging, [Timbre](https://github.com/ptaoussanis/timbre) is used; it's
nice as it doesn't require XML or property files.

The data store can be a SQLite database for testing or PostgreSQL for
production and hosting.

## Design

                                  :              :
                     +---------+  :              : +---------+
                     |  Feed   |  :              : |  InfoQ  |
                     +---------+  :              : +---------+
                          ^       :              :      ^
                          |       :              :      |
                          v       :              :      v
    +----------+     +---------+  : +---------+  : +---------+    +---------+
    |  Client  |<--->| Server  |<---|  Cache  |<---| Catcher |<---| Ticker  |
    +----------+     +---------+  : +---------+  : +---------+    +---------+
                                  :      ^       :
                                  :      |       :
                                  :      v       :
                                  : +---------+  :
                                  : |   DB    |  :
                                  : +---------+  :
                                  :              :

## License

Copyright © 2013 Uwe Dauernheim <uwe@dauernheim.net>

Distributed under the Eclipse Public License either version 1.0 or (at your
option) any later version.
