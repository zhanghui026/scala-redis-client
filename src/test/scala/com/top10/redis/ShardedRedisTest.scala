package com.top10.redis

import scala.collection.JavaConversions._
import org.scalatest.junit.JUnitSuite
import org.junit.Before
import com.top10.redis.test.RedisTestHelper
import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test

class ShardedRedisTest extends JUnitSuite with ShouldMatchersForJUnit with RedisTestHelper {

  @Before def setup() {
    ensureRedis
  }
  
  @Test def testSetGetDelete() {
    shardedRedis.set("testKey", "testValue")
    shardedRedis.get("testKey") should be (Some("testValue"))
    
    shardedRedis.del("testKey")
    shardedRedis.get("testKey") should be (None)
  }

  @Test def testPipeline() {
    shardedRedis.exec(pipeline => {
      pipeline.set("foo", "bar")
      pipeline.set("bar", "foo")
      pipeline.zadd("ranked", 2, "hello")
      pipeline.zadd("ranked", 3, "you")
    })
    
    val results = shardedRedis.syncAndReturnAll(pipeline => {
      pipeline.get("foo")
      pipeline.get("bar")
      pipeline.zrange("ranked", 0, 10)
    })
    results.length should be (3)
    
    val foo = results(0).asInstanceOf[java.lang.String]
    val bar = results(1).asInstanceOf[java.lang.String]
    val ranked = results(2).asInstanceOf[java.util.Set[java.lang.String]].toList
    
    foo should be ("bar")
    bar should be ("foo")
    ranked should be (List("hello", "you"))
  }
  
  @Test def testNullZscore() {
    shardedRedis.zscore("whatevs", "yourmum") should be (None)
  }
  
  @Test def testNullGet() {
    shardedRedis.get("whatevs") should be (None)
  }
  
  @Test def testNullZrank() {
    shardedRedis.zrank("whatevs", "whatevs") should be (None)
  }
  
  @Test def testZRangeWithScores() {
    shardedRedis.zadd("zrangeTest", 0, "0")
    shardedRedis.zadd("zrangeTest", 1, "1")
    shardedRedis.zadd("zrangeTest", 2, "2")
    
    shardedRedis.zrange("zrangeTest", 0, -1) should be (List("0", "1", "2"))
    shardedRedis.zrangeWithScores("zrangeTest", 0, -1) should be (List(("0", 0.0), ("1", 1.0), ("2", 2.0)))
    
    shardedRedis.zrevrange("zrangeTest", 0, -1) should be (List("2", "1", "0"))
    shardedRedis.zrevrangeWithScores("zrangeTest", 0, -1) should be (List(("2", 2.0), ("1", 1.0), ("0", 0.0)))
    
    val results = shardedRedis.syncAndReturnAll(pipeline => {
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
  
  @Test def get9results {
    shardedRedis.set("some", "thing")
    
    val results = shardedRedis.syncAndReturn[String, String, String, String, String, String, String, String, String](pipeline => {
      (0 until 9).foreach(i => pipeline.get("some"))
    })
    
    results should be (("thing", "thing", "thing", "thing", "thing", "thing", "thing", "thing", "thing"))
  }
}