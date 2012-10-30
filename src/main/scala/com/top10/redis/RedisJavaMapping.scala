package com.top10.redis

import scala.collection.JavaConversions._

object RedisJavaMapping {
  val STRING     = manifest[String]
  val OPT_STRING = manifest[Option[String]]
  val SEQ_STRING = manifest[Seq[String]]
  val MAP_STRING = manifest[Map[String, String]]
  val MAP_DOUBLE = manifest[Map[String, Double]]
  val LONG       = manifest[Long]
  val BOOLEAN    = manifest[Boolean]
  val OPT_DOUBLE = manifest[Option[Double]]
  val DOUBLE     = manifest[Double]
  
  def as[M : Manifest](value: Any): M = {
    manifest[M] match {
      case STRING     => value.asInstanceOf[String].asInstanceOf[M]
      case OPT_STRING => Option(value.asInstanceOf[java.lang.String]).asInstanceOf[M]
      case LONG       => Option(value.asInstanceOf[java.lang.Long]).map(_.toLong).getOrElse(0).asInstanceOf[M]
      case BOOLEAN    => Option(value.asInstanceOf[java.lang.Long]).map(_ > 0).getOrElse(false).asInstanceOf[M]
      case DOUBLE     => value.asInstanceOf[java.lang.Double].toDouble.asInstanceOf[M]
      case OPT_DOUBLE => Option(value.asInstanceOf[java.lang.Double]).map(_.toDouble).asInstanceOf[M]
      case SEQ_STRING => Option(value.asInstanceOf[java.util.LinkedHashSet[String]]).map(_.toSeq).getOrElse(Seq[String]()).asInstanceOf[M]
      case MAP_STRING => Option(value.asInstanceOf[java.util.Map[String, String]]).map(_.toMap).getOrElse(Map[String, String]()).asInstanceOf[M]
      case MAP_DOUBLE => Option(value.asInstanceOf[java.util.LinkedHashSet[_root_.redis.clients.jedis.Tuple]]).map(_.toSeq.map(e => e.getElement() -> e.getScore()).toMap).getOrElse(Map[String, Double]()).asInstanceOf[M]
      case m          => throw new InvalidMappingException(m)
    }
  }
  
}

case class InvalidMappingException(valueType: Manifest[_]) extends RuntimeException("Cannot map type of "+valueType)