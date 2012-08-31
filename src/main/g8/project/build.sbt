resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.typesafe.startscript" % "xsbt-start-script-plugin" % "0.5.0")

resolvers += "Kollegger Maven Repository" at "https://github.com/akollegger/mvn-repo/raw/master/snapshots/"

addSbtPlugin("org.neo4j.tools" %% "xsbt-neo4j-plugin" % "0.1.0-SNAPSHOT")
