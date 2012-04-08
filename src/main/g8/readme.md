$name$
======

This [g8](https://github.com/n8han/giter8) generated project features:

- [unfiltered](http://unfiltered.databinder.net/Unfiltered.html) to serve a web application
- [dispatch](http://dispatch.databinder.net/Dispatch.html) to interact with [Neo4j](http://neo4j.org)
- ready to deploy to Heroku

Install Neo4j for the Project
-----------------------------
The project uses the [xsbt-neo4j-plugin](https://github.com/akollegger/xsbt-neo4j-plugin) to
install and control a project-local Neo4j server from sbt.

    sbt> neo4j install
    sbt> neo4j start
    sbt> help neo4j
    sbt> run

Don't forget that Neo4j is running. You can stop it with `neo4j stop`.

Trying the App
--------------
The application includes just enough code to get an `unfiltered` service running
with a simple connection to Neo4j. The endpoints are:

- `GET /` - returns a plain text message
- `POST /` - passes the body through as a [Cypher](http://docs.neo4j.org/chunked/snapshot/cypher-query-lang.html) query to Neo4j

A simple query that works with newly created Neo4j databases is to get `Node` 0, the reference `Node`.

    curl --data "start n=node(0) return n" http://localhost:8080


Discover the Neo4j REST API
---------------------------

To get familiar with the Neo4j REST API, just start the scala console from within sbt...

    sbt> console

Then use dispatch to work with the Neo4j graph...

    // create an _executor_ for HTTP
    val h = new Http
    
    // define the base URL for the Neo4j Server, and check it out
    val neo4j = :/("localhost", 7474)
    h(neo4j >>> System.out)
    
    // define a reference to the "graph" base URL, and get it
    val g = neo4j / "db" / "data"
    h(g >>> System.out)
    
    // define Node base URL, create a node with a POST
    val node = g / "node"
    h((node POST) >>> System.out)

    // create another node, with property "foo" set to "bar"
    h((node << ("{\"foo\" : \"bar\"}", "application/json")) >>> System.out)

    // query with Cypher
    h((cypher << ("""{"query": "start n=node(0) return n"}""", "application/json")) >>> System.out)

Become a Hero(ku)
-----------------
You'll need an account on [Heroku](http://heroku.com) and the [Heroku Toolbelt](https://toolbelt.herokuapp.com/).
Then...

    // initialize the project with a git repository
    git init .
    
    // provision a Heroku app
    heroku apps:create --stack cedar
    heroku addons:add neo4j
    
    // check the config, noting the NEO4J values (to use below)
    heroku config

Then get back into `sbt`, start the `console`, then connect to the provisioned Neo4j server...

    val h = new Http
    val neo4j = (:/(NEO4J_HOST, NEO4J_PORT)) as (NEO4J_LOGIN, NEO4J_PASSWORD)


