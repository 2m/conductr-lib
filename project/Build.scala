import bintray.Plugin.bintrayPublishSettings
import bintray.Keys._
import com.typesafe.sbt.SbtScalariform._
import sbtrelease.ReleasePlugin._
import sbt._
import sbt.Keys._
import scalariform.formatter.preferences._

object Build extends AutoPlugin {

  override def requires =
    plugins.JvmPlugin

  override def trigger =
    allRequirements

  override def projectSettings =
    scalariformSettings ++
    releaseSettings ++
    bintrayPublishSettings ++
    List(
      // Core settings
      organization := "com.typesafe.conductr",
      scalaVersion := Version.scala,
      crossScalaVersions := List(scalaVersion.value, "2.10.4"),
      scalacOptions ++= List(
        "-unchecked",
        "-deprecation",
        "-feature",
        "-language:_",
        "-target:jvm-1.6",
        "-encoding", "UTF-8"
      ),
      licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
      // Scalariform settings
      ScalariformKeys.preferences := ScalariformKeys.preferences.value
        .setPreference(AlignSingleLineCaseStatements, true)
        .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
        .setPreference(DoubleIndentClassDeclaration, true)
        .setPreference(PreserveDanglingCloseParenthesis, true),
      // Bintray settings
      bintrayOrganization in bintray := Some("typesafe"),
      repository in bintray := "maven-releases",
      // Release settings
      ReleaseKeys.versionBump := sbtrelease.Version.Bump.Minor
    )
}
