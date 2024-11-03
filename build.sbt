ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.10.7"

lazy val root = (project in file("."))
  .settings(
    name := "nian-beast",
    idePackagePrefix := Some("com.wenkrang.nian_beast")
  )

javacOptions ++= Seq(
  "-source", "21",
  "-target", "21"
)

assembly / assemblyOption := (assembly / assemblyOption).value.withIncludeScala(false)

resolvers ++= Seq(
  //"spigotmc" at "https://hub.spigotmc.org/nexus/content/repositories/snapshots/",
  "papermc" at "https://repo.papermc.io/repository/maven-public/"
)

libraryDependencies ++= Seq(
  "io.papermc.paper" % "paper-api" % "1.21.3-R0.1-SNAPSHOT" % "provided"
)
