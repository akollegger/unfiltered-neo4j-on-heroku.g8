package $package$

import unfiltered.request._
import unfiltered.response._
import util.Properties

class App(val neo4j:Neo4jServer) extends unfiltered.filter.Plan {

  def intent = {
    case GET(_) => Ok ~> ResponseString("Unfiltered Neo4j on Heroku!")
    case req @ POST(_) =>
      val cql = Body.string(req)
      Ok ~> ResponseString(neo4j.query(cql))
  }
}

class Neo4jServer(val neo4jUrl:String) {
  import dispatch._

  val Neo4jUrlRE = """^(https?):\/\/([^@]*@)?([^:]*):?(\d*)?\$""".r
  val CredentialsRE = """^([^:]*):?(.+)@""".r

  val h = Http
  val neo4j = neo4jUrl match {
    case Neo4jUrlRE(scheme, null, host, "") => :/(host)
    case Neo4jUrlRE(scheme, null, host, port) => :/(host, Integer.parseInt(port))
    case Neo4jUrlRE(scheme, credentials, host, port) =>
      Console.out.println(scheme + " :: " + credentials + " :: " + host + " :: " + port)
      val CredentialsRE(userid, password) = credentials
      (:/(host, Integer.parseInt(port))) as (userid, password)
  }
  val g = neo4j / "db" / "data"
  val cypher = g / "cypher"

  def query(cql:String) = {
    h((cypher << ("{\"query\": \"" + cql + "\"}", "application/json")) as_str)
  }
}

object Web {
  def main(args: Array[String]) = {
    val port = Properties.envOrElse("PORT", "8080").toInt
    val neo4j_url = Properties.envOrElse("NEO4J_URL", "http://localhost:7474")
    val neo4j = new Neo4jServer(neo4j_url)

    unfiltered.jetty.Http(port).filter(new App(neo4j)).run
  }
}
