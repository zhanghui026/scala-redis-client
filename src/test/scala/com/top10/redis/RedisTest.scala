package com.top10.redis

import org.scalatest.junit.{ShouldMatchersForJUnit, JUnitSuite}
import org.junit.{Test, Before}
import scala.collection.JavaConversions._
import org.joda.time.DateTime
import scala.io.Source
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.Protocol
import com.top10.redis.test.RedisTestHelper
import redis.clients.jedis.Transaction

class RedisTest extends JUnitSuite with ShouldMatchersForJUnit with RedisTestHelper {

  @Before def setup() {
    ensureRedis
  }

  @Test def testSetGetDelete() {
    redis.flushAll

    redis.set("testKey", "testValue")
    redis.get("testKey") should be (Some("testValue"))

    redis.del("testKey")
    redis.get("testKey") should be (None)
  }

  @Test def testMultiSuccess() {
    redis.set("testKey", "testValue")
    val result = redis.watch(List("testKey"), watched => {
      val t = watched.multi()
      t.set("testKey", "testValue2")
      Option.apply(t.exec())
    })


    result should not be (None)
    redis.get("testKey") should be (Some("testValue2"))
  }

  @Test def testMultiFail() {
    redis.set("testKey", "testValue")
    val result = redis.watch(List("testKey"), watched => {
      val t = watched.multi()
      redis.set("testKey", "testValue3")
      t.set("testKey", "testValue2")
      Option.apply(t.exec())
    })

    result should be (None)
    redis.get("testKey") should be (Some("testValue3"))
  }


  @Test def testNullZscore() {
    redis.zscore("whatevs", "yourmum") should be (None)
  }

  @Test def testNullGet() {
    redis.get("whatevs") should be (None)
  }

  @Test def testNullZrank() {
    redis.zrank("whatevs", "whatevs") should be (None)
  }

  @Test def testZRangeWithScores() {
    redis.zadd("zrangeTest", 0, "0")
    redis.zadd("zrangeTest", 1, "1")
    redis.zadd("zrangeTest", 2, "2")

    redis.zrange("zrangeTest", 0, -1) should be (List("0", "1", "2"))
    redis.zrangeWithScores("zrangeTest", 0, -1) should be (Map("0" -> 0.0, "1" -> 1.0, "2" -> 2.0))

    redis.zrevrange("zrangeTest", 0, -1) should be (List("2", "1", "0"))
    redis.zrevrangeWithScores("zrangeTest", 0, -1) should be (Map("2"-> 2.0, "1" -> 1.0, "0" -> 0.0))

    val results = redis.syncAndReturnAll(pipeline => {
      pipeline.zrange("zrangeTest", 0, -1)
      pipeline.zrangeWithScores("zrangeTest", 0, -1)

      pipeline.zrevrange("zrangeTest", 0, -1)
      pipeline.zrevrangeWithScores("zrangeTest", 0, -1)
    })

    results(0).asInstanceOf[java.util.LinkedHashSet[String]].toList should be (List("0", "1", "2"))
    results(1).asInstanceOf[java.util.LinkedHashSet[_root_.redis.clients.jedis.Tuple]].toList.map(t => (t.getElement(), t.getScore())) should be (List(("0", 0.0), ("1", 1.0), ("2", 2.0)))
    results(2).asInstanceOf[java.util.LinkedHashSet[String]].toList should be (List("2", "1", "0"))
    results(3).asInstanceOf[java.util.LinkedHashSet[_root_.redis.clients.jedis.Tuple]].toList.map(t => (t.getElement(), t.getScore())) should be (List(("2", 2.0), ("1", 1.0), ("0", 0.0)))
  }

  @Test def testZRangeWithScoresSync {
    redis.zadd("zrangeTest", 0, "0")
    redis.zadd("zrangeTest", 1, "1")
    redis.zadd("zrangeTest", 2, "2")

    redis.zrange("zrangeTest", 0, -1) should be (List("0", "1", "2"))
    redis.zrangeWithScores("zrangeTest", 0, -1) should be (Map("0" -> 0.0, "1" -> 1.0, "2" -> 2.0))

    redis.zrevrange("zrangeTest", 0, -1) should be (List("2", "1", "0"))
    redis.zrevrangeWithScores("zrangeTest", 0, -1) should be (Map("2" -> 2.0, "1" -> 1.0, "0" -> 0.0))

    val results = redis.syncAndReturn[Seq[String], Map[String, Double]](pipeline => {
      pipeline.zrange("zrangeTest", 0, -1)
      pipeline.zrangeWithScores("zrangeTest", 0, -1)
    })

    results._1 should be (Seq("0", "1", "2"))
    results._2 should be (Map("0" -> 0.0, "1" -> 1.0, "2" -> 2.0))
  }

  @Test def get9results {
    redis.set("some", "thing")

    val results = redis.syncAndReturn[String, String, String, String, String, String, String, String, String](pipeline => {
      (0 until 9).foreach(i => pipeline.get("some"))
    })

    results should be (("thing", "thing", "thing", "thing", "thing", "thing", "thing", "thing", "thing"))
  }

  @Test def testIncr {
    redis.flushAll

    redis.incr("test_value") should be (1l)
    redis.get("test_value") should be (Some("1"))
    redis.incrby("test_value", 2) should be (3l)
    redis.get("test_value") should be (Some("3"))

    redis.syncAndReturn[Long, String, Long, String](pipeline => {
      pipeline.incr("test_value")
      pipeline.get("test_value")
      pipeline.incrby("test_value", 5)
      pipeline.get("test_value")
    }) should be (4l, "4", 9l, "9")

    redis.get("test_value") should be (Some("9"))
  }

  @Test def testMsetMget {
    redis.flushAll

    redis.mset(Seq(
      ("test1", "a"),
      ("test2", "b"),
      ("test4", "d")
    ))

    redis.mget(Seq("test1", "test2", "test3", "test4")) should be (Seq(Some("a"), Some("b"), None, Some("d")))
  }
}
