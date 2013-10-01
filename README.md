# InfoQ podcast

A podcast feed for InfoQ presentations.

## Installation

Either using Leiningen:

    $ lein run

Or using Java:

    $ java -jar infoq-podcast-0.1.0-standalone.jar

## Usage

[http://app.heroku.com/infoq-podcast](remote) or
[http://localhost:8000/](local).

## Design

                                  :              :
                     +---------+  :              : +---------+
                     |  Feed   |  :              : |  InfoQ  |
                     +---------+  :              : +---------+
                          ^       :              :      ^
                          |       :              :      |
                          v       :              :      v
    +----------+     +---------+  : +---------+  : +---------+    +---------+
    |  Client  |<--->| Server  |<---|  Cache  |<---| Catcher |<---|  Timer  |
    +----------+     +---------+  : +---------+  : +---------+    +---------+
                                  :      ^       :
                                  :      |       :
                                  :      v       :
                                  : +---------+  :
                                  : |   DB    |  :
                                  : +---------+  :
                                  :              :

## License

Copyright © 2013 Uwe Dauernheim

Distributed under the Eclipse Public License either version 1.0 or (at your
option) any later version.
