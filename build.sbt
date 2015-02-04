/*
 * Copyright © 2014-2015 Typesafe, Inc. All rights reserved. No information contained herein may be reproduced or
 * transmitted in any form or by any means without the express written permission of Typesafe, Inc.
 */

lazy val root = project
  .in(file("."))
  .aggregate(conductRBundleLib, scalaConductRBundleLib)

lazy val conductRBundleLib = project
  .in(file("conductr-bundle-lib"))
  .dependsOn(testLib % "test->compile")

lazy val scalaConductRBundleLib = project
  .in(file("scala-conductr-bundle-lib"))
  .dependsOn(conductRBundleLib)
  .dependsOn(testLib % "test->compile")

lazy val testLib = project
  .in(file("test-lib"))
  
name := "root"

publishArtifact := false
