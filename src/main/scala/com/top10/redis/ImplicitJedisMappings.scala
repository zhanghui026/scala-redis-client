package com.top10.redis


import scala.collection.JavaConverters._
import redis.clients.jedis.{Response => JedisResponse}
import java.lang.{String => JavaString}
import redis.clients.jedis.{Tuple => JedisTuple}

object ImplicitJedisMappings {

  implicit def optionConverter[X, Y](x: X)(implicit converter: X => Y): Option[Y] =
    Option(x).map(converter)

  implicit def longToOption(l: java.lang.Long)(implicit toScalaLong: java.lang.Long => Long): Option[Long] =
    Option(l).map(toScalaLong)

  implicit def stringToOption(x: JavaString)(implicit converter: JavaString => String): Option[String] =
    Option(x).map(converter)

  implicit def jedisToScalaResponse[ST, JT](response: JedisResponse[JT])(implicit converter: JT => ST): ScalaResponse[ST] =
    ScalaResponse.wrapResponse[ST, JT](response, converter)

  implicit def jedisTupleToMap(s: java.util.Set[JedisTuple])(implicit f: JavaString => String): Map[String, Double] =
    s.asScala.map{t:JedisTuple => (f(t.getElement) -> t.getScore)}.toMap

  implicit def setToScalaSet[T](s: java.util.Set[T]): Set[T] =
    s.asScala.toSet

  implicit def mapToScalaMap[K, V](s: java.util.Map[K, V]): Map[K, V] =
    s.asScala.toMap

  implicit def listToSeq[T](s: java.util.List[T]): Seq[T] =
    s.asScala
}
