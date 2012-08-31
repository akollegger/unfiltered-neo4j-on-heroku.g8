Unfiltered Heroku Neo4j
=======================

An example Scala/sbt project.

Install Neo4j for the Project
-----------------------------

    neo4j install
    neo4j start

Discover the Neo4j REST API
---------------------------

  sbt> console

  scala>

    // create an _executor_ for HTTP
    val h = new Http
    
    // generate the base URL for the Neo4j Server
    def neo4j = :/("localhost", 7474).addHeader("Accept", "application/json")

    // query the base url, using `for` to apply the promise
    for (json <- h(neo4j OK as.String)) println(json)
    
    // define a reference to the "graph" base URL, and get it
    // note the awkward addition of a blank string to force the URL to include '/'
    def g = neo4j / "db/data/"
    for (json <- h(g OK as.String)) println(json)
    
    // define Node base URL, create a node with a POST
    def node = g / "node"
    h(node << Map() > as.String)

    // create another node, with property "foo" set to "bar"
    h(node << Map("foo" -> "bar") > as.String)

    // use node's id to get its properties
    h(node / "1/properties" OK as.String)

    // query with Cypher
    def cypher = g / "cypher"
    h(cypher << Map("query" -> "start n=node(*) return n") OK as.String)

Demo Web Application
--------------------

The sample web application provides a simple endpoint for accepting Cypher queries
which are passed on to Neo4j, returning the text result of the query.

To try it: `sbt run`

You can post queries from the command line using the include `bin\cypher` bash-based tool.

    # pass cypher query as a string argument
    ./bin/cypher "start n=node(*) return n"
    
    # pipe in a cypher query
    echo "start n=node(*) return n" | ./bin/cypher

    # use cypher as a simple repl, with history
    ./bin/cypher


To the cloud!
-------------

THe project generates a start script using the [xsbt-start-script-plugin](https://github.com/typesafehub/xsbt-start-script-plugin),
which is referenced by the included `Procfile`. With [foreman](http://ddollar.github.com/foreman/) installed, you can run the web
app by staging the start script, then running with foreman like this:

    sbt stage
    foreman start

The `foreman start` is essentially how Heroku will start the web application when you've done this:

    heroku apps:create --stack cedar
    heroku addons:add neo4j

    git push heroku master
   
# Now go enjoy a nice hot graph, unfiltered on Heroku

