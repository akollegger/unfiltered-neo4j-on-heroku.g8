package $package$

import unfiltered.netty._
import unfiltered.request._
import unfiltered.response._
import util.Properties
import unfiltered.request

class App(val neo4j:Neo4jServer) extends async.Plan with ServerErrorResponse {

   def intent = {
     case req @ GET(_) =>  req.respond(PlainTextContent ~> ResponseString("Unfiltered Neo4j on Heroku!"))
     case req @ POST(_) =>
       val cql = Body.string(req)
       for (graph <- neo4j.query(cql)) {
         req.respond(PlainTextContent ~> ResponseString(graph.getOrElse("Neo4j is not available.")))
       }
   }
}

class Neo4jServer(val neo4jUrl:String) {
   import dispatch._

   val Neo4jUrlRE = """^(https?):\/\/([^@]*@)?([^:]*):?(\d*)?\$""".r
   val CredentialsRE = """^([^:]*):?(.+)@""".r

   val h = Http
   def neo4j = neo4jUrl match {
     case Neo4jUrlRE(scheme, null, host, "") => :/(host)
     case Neo4jUrlRE(scheme, null, host, port) => :/(host, Integer.parseInt(port))
     case Neo4jUrlRE(scheme, credentials, host, port) =>
       Console.out.println(scheme + " :: " + credentials + " :: " + host + " :: " + port)
       val CredentialsRE(userid, password) = credentials
       (:/(host, Integer.parseInt(port))) as (userid, password)
   }
   def g = neo4j / "db" / "data"
   def cypher = g / "cypher"

   def queryNeo4j(cql:String) = Http(cypher << Map("query" -> cql) > as.String).option

   def query(cql:String) = {
     for (graphOpt <- queryNeo4j(cql))
       yield graphOpt
   }
}

object Web {
   def main(args: Array[String]) = {
     val port = Properties.envOrElse("PORT", "8080").toInt
     val neo4j_url = Properties.envOrElse("NEO4J_URL", "http://localhost:7474")
     val neo4j = new Neo4jServer(neo4j_url)

     unfiltered.netty.Http(port).plan(new App(neo4j)).run
   }
}


