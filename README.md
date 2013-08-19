Scala Redis Client
==================

Idiomatic Scala [Redis](http://redis.io) client developed and used by [Top10](http://top10.com). This is a work-in-progress and, while it is used in production, should be treated as such.

Currently this client wraps the Java [Jedis](https://github.com/xetorthio/jedis) client and in places that still leaks out. We've had to patch Jedis in a few ways and have left the patched files in src/main/java. When the pull requests get merged in and released as a new Jedis version we'll get rid of them, but that doesn't look like it's going to happen any time soon.

Usage
-----

* Current 'stable' version: 1.16.0
* Unstable version: 1.17.0-SNAPSHOT
* Scala versions: 2.9.1, 2.9.2, 2.10.0

Published to the Sonatype [release](https://oss.sonatype.org/content/repositories/releases/) and [snapshot](https://oss.sonatype.org/content/repositories/snapshots/) repositories.

    resolvers += "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"

    libraryDependencies ++= Seq(
      "com.top10" %% "scala-redis-client" % "1.3.0" withSources()
    )

Because we have had to bundle some patched files from Jedis, if you are using the assembly plugin you'll need to set a MergeStategy that chooses them:

    mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
      {
        case PathList("redis", "clients", xs @ _*) => MergeStrategy.first
        case x => old(x)
      }
    }

Please let us know if there's a better way of doing this.

You can instantiate SingleRedis and ShardedRedis using the Jedis config objects, but I guess you'd rather not. Optional arguments have (not particularly scientific) sensible defaults:

    val redis = new SingleRedis("localhost", 6379, [Some("password")], [timeout], [poolSize])
    
    val shards = Seq(Shard("localhost", 6379, [Some("password")], [timeout]),
                     Shard("localhost", 6380, [Some("password")], [timeout])
    val shardedRedis = new ShardedRedis(shards, [poolSize], [hashing algorithm])

For the full API it's worth having a look through [Redis.scala](https://github.com/top10/scala-redis-client/blob/master/src/main/scala/com/top10/redis/Redis.scala). If you find any operations that haven't been mapped from Jedis yet, just raise an issue (preferably, pull request).

It's all pretty straight-forward, using Seq and Option where appropriate. The pipelines are worth pointing out though. If you want to perform multiple operations that don't return any results, pass a PartialFunction to exec:

    redis.exec(pipeline => {
      pipeline.set("some", "value")
      pipeline.sadd("set", "value")
    })

If you want to get stuff, use the typed syncAndReturn function which returns a TupleN (up to 9) of the type that you expect:

    val results = redis.syncAndReturn[Option[String], Map[String, Double], Seq[String], Set[String]](pipeline => {
      pipeline.get("somekey")
      pipeline.zrevrangeWithScores("sortedsetkey", 0, -1)
      pipeline.zrevrange("sortedsetkey", 0, -1)
      pipeline.smembers("setkey")
    })

Unfortunately this only provides RUNTIME exceptions if you get the type parameters wrong (either the number or number of them). Will try an will out how to make this a compile time problem (pull requests welcome).

In a similar fashion, if you want to get a load of results of the same type, you can use syncAndReturnAllAs:

    val results = redis.syncAndReturnAll[Map[String,Double]](pipeline => {
      (0 until 100).foreach(i => pipeline.zrevrangeWithScores("sortedsetkey:"+i, 0, -1))
    })

Anyway, if you find any problems or ommissions, please let us know.
