# QCast - InfoQ presentation podcast

A podcast feed (audio & video) for InfoQ presentations.

![Landing site](https://raw.githubusercontent.com/djui/qcast/master/screenshot.png)


## Installation

Either using Leiningen:

    $ ./start.sh

or

    $ lein run

Or using Java:

    $ lein uberjar
    $ java -jar qcast-standalone.jar


## Usage

The Overview page can be reached at:
[remote](https://infoqcast.herokuapp.com/) or
[local](http://localhost:8080/).

The Feed can be reached at: [remote feed](https://infoqcast.herokuapp.com/feed)
or [local feed](https://localhost:8080/feed).


## Components

The application quite small and currently has just under 900 LoC currently uses
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
    |  Client  |<--->| Server  |<---|  Cache  |<---| Scraper |<---| Ticker  |
    +----------+     +---------+  : +---------+  : +---------+    +---------+
                                  :      ^       :
                                  :      |       :
                                  :      v       :
                                  : +---------+  :
                                  : |   DB    |  :
                                  : +---------+  :
                                  :              :


## Credits

Thanks to [InfoQ](http://www.infoq.com) for their great service!


## Contributors

Thanks to the following set of people that helped to find, debug, and fix bugs:

- [i-s-o-g-r-a-m](https://github.com/i-s-o-g-r-a-m)
- [JKesMc9tqIQe9M](https://github.com/JKesMc9tqIQe9M)


## License

Copyright © 2013-2015 Uwe Dauernheim <uwe@dauernheim.net>

Distributed under the Eclipse Public License either version 1.0 or (at your
option) any later version.
