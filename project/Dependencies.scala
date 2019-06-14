import sbt._

object Dependencies {

  lazy val decline = "com.monovore" %% "decline" % "0.5.0"
  lazy val attoCore = "org.tpolecat" %% "atto-core"    % "0.6.5"
  lazy val cats = "org.typelevel" %% "cats-core" % "1.6.0"
  lazy val catsEffect = "org.typelevel" %% "cats-effect" % "1.3.0"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.14.0"
}
