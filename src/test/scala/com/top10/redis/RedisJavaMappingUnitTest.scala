package com.top10.redis

import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test
import java.security.InvalidParameterException
import redis.clients.jedis.Tuple

class RedisJavaMappingUnitTest extends JUnitSuite with ShouldMatchersForJUnit {

  @Test def mapString {
    RedisJavaMapping.as[String](new java.lang.String("some string")) should be ("some string")
    RedisJavaMapping.as[Option[String]](new java.lang.String("some string")) should be (Some("some string"))
    RedisJavaMapping.as[Option[String]](null) should be (None)
  }
  
  @Test def mapDouble {
    RedisJavaMapping.as[Double](new java.lang.Double(0.2d)) should be (0.2d)
    RedisJavaMapping.as[Option[Double]](new java.lang.Double(0.2d)) should be (Some(0.2d))
    RedisJavaMapping.as[Option[Double]](null) should be (None)
  }
  
  @Test def mapBoolean {
    RedisJavaMapping.as[Boolean](new java.lang.Long(1L)) should be (true)
    RedisJavaMapping.as[Boolean](new java.lang.Long(0L)) should be (false)
    RedisJavaMapping.as[Boolean](null) should be (false)
  }
  
  @Test def mapSeq {
    val input = new java.util.LinkedHashSet[String]()
    input.add("hello")
    input.add("you")
    RedisJavaMapping.as[Seq[String]](input) should be (Seq("hello", "you"))
    RedisJavaMapping.as[Seq[String]](null) should be (Seq())
  }
  
  @Test(expected=classOf[InvalidMappingException]) def mapSomethingInvalid {
    RedisJavaMapping.as[Redis](new java.lang.String("some string"))
  }
  
  @Test(expected=classOf[InvalidMappingException]) def mapToWrongType {
    RedisJavaMapping.as[String](new java.lang.Long(1L))
  }
  
  @Test def mapMap {
    val input = new java.util.HashMap[String, String]()
    input.put("some", "thing")
    RedisJavaMapping.as[Map[String, String]](input) should be (Map("some" -> "thing"))
  }
  
  @Test def mapMapDouble {
    val input = new java.util.LinkedHashSet[Tuple]()
    input.add(new Tuple("some", 1.1d))
    RedisJavaMapping.as[Map[String, Double]](input) should be (Map("some" -> 1.1d))
  }
}