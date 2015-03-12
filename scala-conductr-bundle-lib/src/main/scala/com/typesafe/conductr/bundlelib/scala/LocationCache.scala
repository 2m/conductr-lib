/*
 * Copyright © 2014-2015 Typesafe, Inc. All rights reserved.
 * No information contained herein may be reproduced or transmitted in any form
 * or by any means without the express written permission of Typesafe, Inc.
 */

package com.typesafe.conductr.bundlelib.scala

import java.util.{ TimerTask, Timer }

import scala.collection.concurrent.TrieMap
import scala.concurrent.ExecutionContext.Implicits
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.util.Success

/**
 * A structure that describes what we require from a cache.
 */
trait CacheLike {
  def getOrElseUpdate(serviceName: String)(op: => Future[Option[(String, Option[FiniteDuration])]]): Future[Option[String]]
  def remove(serviceName: String): Option[Future[Option[String]]]
}

object LocationCache {
  def apply() = new LocationCache
}

/**
 * A cache specifically for locations in relation to service names. This is an expiring
 * entry cache inspired by Spray Cache in its elements being captured as Futures, thus
 * being able to cope with the thundering herds issue:
 * http://ehcache.org/documentation/2.8/recipes/thunderingherd.
 *
 * Entries that provide a max age duration are scheduled to be removed at that time. The
 * expectation is that this cache is used with such durations. Where there is no duration
 * (this should be rare) then the cache entry is quickly removed after it has been determined.
 * This removal also occurs when the entry cannot be established successfully.
 */
class LocationCache extends CacheLike {

  private val cache = TrieMap.empty[String, Future[Option[String]]]

  val reaperTimer = new Timer()

  override def getOrElseUpdate(serviceName: String)(op: => Future[Option[(String, Option[FiniteDuration])]]): Future[Option[String]] = {
    def dualOp: Future[Option[String]] = {
      val locationAndMaxAge = op

      import Implicits.global
      locationAndMaxAge.andThen {
        case Success(Some((_, Some(maxAge)))) =>
          reaperTimer.schedule(new TimerTask {
            override def run(): Unit = {
              cache.remove(serviceName)
            }
          }, maxAge.toMillis)
        case _ =>
          cache.remove(serviceName)
      }

      locationAndMaxAge.map {
        case Some((location, _)) => Some(location)
        case None                => None
      }
    }
    cache.getOrElseUpdate(serviceName, dualOp)
  }

  override def remove(serviceName: String): Option[Future[Option[String]]] =
    cache.remove(serviceName)
}