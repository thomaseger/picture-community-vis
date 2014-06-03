name := "picture-community-vis"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.3",
  javaJdbc,
  javaEbean,
  cache
)     

play.Project.playJavaSettings
