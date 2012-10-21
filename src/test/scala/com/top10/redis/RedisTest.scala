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
    if(!redisIsRunning) {
      printf("Redis is not running, passing test anyway")
      return
    }
    
    redis.flushAll
    
    redis.set("testKey", "testValue")
    redis.get("testKey") should be (Some("testValue"))
    
    redis.del("testKey")
    redis.get("testKey") should be (None)
  }
  
  @Test def testMultiSuccess() {
    if(!redisIsRunning) {
      printf("Redis is not running, passing test anyway")
      return
    }
    
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
    if(!redisIsRunning) {
      printf("Redis is not running, passing test anyway")
      return
    }
    
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
    if(!redisIsRunning) {
      printf("Redis is not running, passing test anyway")
      return
    }
    
    redis.zscore("whatevs", "yourmum") should be (None)
  }
  
  @Test def testNullGet() {
    if(!redisIsRunning) {
      printf("Redis is not running, passing test anyway")
      return
    }
    
    redis.get("whatevs") should be (None)
  }
  
  @Test def testNullZrank() {
    if(!redisIsRunning) {
      printf("Redis is not running, passing test anyway")
      return
    }
    
    redis.zrank("whatevs", "whatevs") should be (None)
  }
  
  @Test def testZRangeWithScores() {
    if(!redisIsRunning) {
      printf("Redis is not running, passing test anyway")
      return
    }
    
    redis.zadd("zrangeTest", 0, "0")
    redis.zadd("zrangeTest", 1, "1")
    redis.zadd("zrangeTest", 2, "2")
    
    redis.zrange("zrangeTest", 0, -1) should be (List("0", "1", "2"))
    redis.zrangeWithScores("zrangeTest", 0, -1) should be (List(("0", 0.0), ("1", 1.0), ("2", 2.0)))
    
    redis.zrevrange("zrangeTest", 0, -1) should be (List("2", "1", "0"))
    redis.zrevrangeWithScores("zrangeTest", 0, -1) should be (List(("2", 2.0), ("1", 1.0), ("0", 0.0)))
    
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
}