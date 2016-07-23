package com.winning.sx.common.dao.redis;

/**
 * Created by smq on 16/6/30.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.winning.sx.common.util.common;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.SortingParams;

public class RedisOpDao {


    private Jedis jedis;  //非切片额客户端连接
    private JedisPool jedisPool;  //非切片连接池
    private ShardedJedis sharedJedis;  //切片额客户端连接
    private ShardedJedisPool shardedJedisPool;  //切片连接池

    public RedisOpDao() throws IOException {
        initialPool();
        initialShardedPool();
        this.sharedJedis = this.shardedJedisPool.getResource();
        this.jedis = this.jedisPool.getResource();
    }


    /**
     * 初始化非切片连接池
     */
    private void initialPool() throws IOException {
        //池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(20);
        config.setMaxIdle(5);
        config.setMaxWait(1000l);
        config.setTestOnBorrow(false);

        this.jedisPool = new JedisPool(config, common.GetConfigValue("redis.hostname"), 6379);
    }

    /**
     * 初始化片池
     */
    private void initialShardedPool() throws IOException {
        //池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(20);
        config.setMaxIdle(5);
        config.setMaxWait(1000l);
        config.setTestOnBorrow(false);
        //slave连接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();



//        System.out.println(pro.getProperty("baseFilePath"));
//        pro.setProperty("shuzi", "1111");//往属性文件插值
//        pro.setProperty("shuzi", "222");//更改属性值
//        System.out.println(pro.getProperty("shuzi"));

//        shards.add(new JedisShardInfo("127.0.0.1", 6379, "master"));
        shards.add(new JedisShardInfo(common.GetConfigValue("redis.hostname"), 6379, "master"));
        //构造池
        this.shardedJedisPool = new ShardedJedisPool(config, shards);
    }

    public void show() {
        //keyOperate();
        //stringOperate();
        //listOperate();
        //setOperate();
        //sortedSetOperate();
        hashOperate();
        jedisPool.returnResource(this.jedis);
        this.shardedJedisPool.returnResource(this.sharedJedis);

    }

    private void keyOperate() {
        System.out.println("==========key==========");
        //清空数据
        System.out.println("清空库中的所有数据:" + this.jedis.flushDB());
        //
        System.out.println("判断key999键是否存在:" + this.jedis.exists("key999"));
        System.out.println("新增key001,value001键值对:" + this.sharedJedis.set("key001", "value001"));
        System.out.println("判断key001键是否存在:" + this.jedis.exists("key001"));
        System.out.println("新增key002,value002键值对:" + this.sharedJedis.set("key002", "value002"));
        //输出所有的key
        Set<String> keys = this.jedis.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }
        //删除某个key，如果该key不存在，则忽略该命令
        System.out.println("删除掉key002键:" + this.jedis.del("key002"));
        System.out.println("判断key002键是否存在:" + this.sharedJedis.exists("key002"));
        //设置key001的过期时间为5s
        System.out.println("设置key001的过期时间为5s:" + this.jedis.expire("key001", 5));
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //查看某个key的生存时间，永久生存或者不存在返回-1
        System.out.println("查看key001的剩余生存时间:" + this.jedis.ttl("key001"));
        //移除某个key的生存时间
        System.out.println("移除key001键的生存时间:" + this.jedis.persist("key001"));
        System.out.println("查看key001的剩余生存时间:" + this.jedis.ttl("key001"));
        //查看key所存储值的类型
        System.out.println("查看key001存储值的类型:" + this.jedis.type("key001"));
    }

    public void stringOperate() {
        //清空数据
        System.out.println("清空数据:" + this.jedis.flushDB());
        //增
        System.out.println("=======增========");
        this.jedis.set("key001", "value001");
        this.jedis.set("key002", "value002");
        this.jedis.set("key003", "value003");
        //已增加的三个值入下
        System.out.println(this.jedis.get("key001"));
        System.out.println(this.jedis.get("key002"));
        System.out.println(this.jedis.get("key003"));
        //删除
        System.out.println("删除key003键值对：" + this.jedis.del("key003"));
        System.out.println("获得key003的键值" + this.jedis.get("key003"));

        //改
        //1、直接覆盖原来的数据
        System.out.println("直接覆盖key001原来的数据：" + this.jedis.set("key001", "value-update"));
        System.out.println("获取key001对应的新值：" + this.jedis.get("key001"));
        //2.在原来的值后面追加
        System.out.println("在key002原来值后面追加：" + this.jedis.append("key002", " appended value"));
        System.out.println("获取key002对应的新值:" + this.jedis.get("key002"));
        System.out.println("=============增，删，查（多个）=============");
        /**
         * mset,mget同时新增，修改，查询多个键值对
         * 等价于：
         * jedis.set("name","ssss");
         * jedis.set("jarorwar","xxxx");
         */
        System.out.println("一次性新增key201,key202,key203,key204及其对应值：" +
                this.jedis.mset("key201", "value201", "key202", "value202", "key203", "value203",
                        "key204", "value204")
        );
        System.out.println("一次性获取key201,key202,key203,key204各自对应的值：" +
                this.jedis.mget("key201", "key202", "key203", "key204")
        );
        System.out.println("一次性删除key201,key202：" + this.jedis.del(new String[]{"key201", "key202"}));
        System.out.println("一次性获取key201,key202,key203,key204各自对应的值：" +
                this.jedis.mget("key201", "key202", "key203", "key204")
        );
    }

    //链表操作
    public void listOperate() {
        this.jedis.flushDB();
        System.out.println("=============增=============");
        this.jedis.lpush("stringlists", "vector");
        this.jedis.lpush("stringlists", "ArrayList");
        this.jedis.lpush("stringlists", "vector");
        this.jedis.lpush("stringlists", "vector");
        this.jedis.lpush("stringlists", "LinkedList");
        this.jedis.lpush("stringlists", "MapList");
        this.jedis.lpush("stringlists", "SerialList");
        this.jedis.lpush("stringlists", "HashList");

        this.jedis.lpush("numberlists", "3");
        this.jedis.lpush("numberlists", "1");
        this.jedis.lpush("numberlists", "5");
        this.jedis.lpush("numberlists", "2");

        System.out.println("所有元素-stringlists:" + this.sharedJedis.lrange("stringlists", 0, -1));
        System.out.println("所有元素-numberlists:" + this.sharedJedis.lrange("numberlists", 0, -1));

        System.out.println("=============删=============");
        //删除列表指定的值 ，第二个参数为删除的个数（有重复时），后add进去的值先被删，类似于出栈
        System.out.println("成功删除指定元素个数-stringlists："
                + this.sharedJedis.lrem("stringlists", 2, "vector"));
        System.out.println("删除指定元素之后-stringlists："
                + this.sharedJedis.lrange("stringlists", 0, -1));
        // 删除区间以外的数据
        System.out.println("删除下标0-3区间之外的元素："
                + this.sharedJedis.ltrim("stringlists", 0, 3));
        System.out.println("删除指定区间之外元素后-stringlists："
                + this.sharedJedis.lrange("stringlists", 0, -1));
        // 列表元素出栈
        System.out.println("出栈元素：" + this.sharedJedis.lpop("stringlists"));
        System.out.println("元素出栈后-stringlists："
                + this.sharedJedis.lrange("stringlists", 0, -1));
        System.out.println("=============改=============");
        // 修改列表中指定下标的值
        this.sharedJedis.lset("stringlists", 0, "hello list!");
        System.out.println("下标为0的值修改后-stringlists："
                + this.sharedJedis.lrange("stringlists", 0, -1));
        System.out.println("=============查=============");
        // 数组长度
        System.out.println("长度-stringlists：" + this.sharedJedis.llen("stringlists"));
        System.out.println("长度-numberlist：" + this.sharedJedis.llen("numberlists"));
        // 排序
        /*
         * list中存字符串时必须指定参数为alpha，如果不使用SortingParams，而是直接使用sort("list")，
         * 会出现"ERR One or more scores can't be converted into double"
         */
        SortingParams sortingParameters = new SortingParams();
        sortingParameters.alpha();
        sortingParameters.limit(0, 3);
        System.out.println("返回排序后的结果-stringlists："
                + this.sharedJedis.sort("numberlists"));
        // 子串：  start为元素下标，end也为元素下标；-1代表倒数一个元素，-2代表倒数第二个元素
        System.out.println("子串-第二个开始到结束："
                + this.sharedJedis.lrange("stringlists", 1, -1));
        //获取列表指定下标的值
        System.out.println("获取下标为2的元素："
                + this.sharedJedis.lindex("stringlists", 2));
    }

    //集合操作
    public void setOperate() {
        System.out.println("======================set==========================");
        // 清空数据
        this.jedis.flushDB();
        System.out.println("=============增=============");
        System.out.println("向sets集合中加入元素element001：" +
                this.jedis.sadd("sets", "element001"));
        System.out.println("向sets集合中加入元素element002：" +
                this.jedis.sadd("sets", "element002"));
        System.out.println("向sets集合中加入元素element003：" +
                this.jedis.sadd("sets", "element003"));
        System.out.println("向sets集合中加入元素element004：" +
                this.jedis.sadd("sets", "element004"));
        System.out.println("查看sets集合中的所有元素:" +
                this.jedis.smembers("sets"));
        System.out.println("=============删=============");
        System.out.println("集合sets中删除元素element003：" +
                this.jedis.srem("sets", "element003"));
        System.out.println("查看sets集合中的所有元素:" +
                this.jedis.smembers("sets"));
        System.out.println("=============查=============");
        System.out.println("判断element001是否在集合sets中：" +
                this.jedis.sismember("sets", "element001"));
        Set<String> sets = this.jedis.smembers("sets");
        if (sets != null && sets.size() > 0) {
            for (String str : sets) {
                System.out.println(str);
            }
        }
        System.out.println("=============集合运算=============");
        System.out.println("sets1中添加元素element001："+jedis.sadd("sets1", "element001"));
        System.out.println("sets1中添加元素element002："+jedis.sadd("sets1", "element002"));
        System.out.println("sets1中添加元素element003："+jedis.sadd("sets1", "element003"));
        System.out.println("sets1中添加元素element002："+jedis.sadd("sets2", "element002"));
        System.out.println("sets1中添加元素element003："+jedis.sadd("sets2", "element003"));
        System.out.println("sets1中添加元素element004："+jedis.sadd("sets2", "element004"));
        System.out.println("查看sets1集合中的所有元素:"+jedis.smembers("sets1"));
        System.out.println("查看sets2集合中的所有元素:"+jedis.smembers("sets2"));
        System.out.println("sets1和sets2交集：" + this.jedis.sinter("sets1", "sets2"));
        System.out.println("sets1和sets2并集：" + this.jedis.sunion("sets1", "sets2"));
        System.out.println("sets1和sets2差集：" + this.jedis.sdiff("sets1", "sets2"));
    }

    //有序集合操作
    public void sortedSetOperate() {
        // 清空数据
        this.jedis.flushDB();
        System.out.println("=============增=============");
        System.out.println("zset中添加元素element001：" +
                this.jedis.zadd("zset", 7.0, "element001"));
        System.out.println("zset中添加元素element002：" +
                this.jedis.zadd("zset", 8.0, "element002"));
        System.out.println("zset中添加元素element003：" +
                this.jedis.zadd("zset", 2.0, "element003"));
        System.out.println("zset中添加元素element004：" +
                this.jedis.zadd("zset", 3.0, "element004"));
        System.out.println("zset集合中的所有元素：" +
                this.sharedJedis.zrange("zset", 0, -1));
        System.out.println("=============删=============");
        System.out.println("zset中删除元素element002：" +
                this.sharedJedis.zrem("zset", "element002"));
        System.out.println("zset集合中的所有元素：" +
                this.sharedJedis.zrange("zset", 0, -1));
        System.out.println("=============查=============");
        System.out.println("统计zset集合中的元素中个数：" +
                this.jedis.zcard("zset"));
        System.out.println("统计zset集合中权重某个范围内（1.0——5.0），元素的个数：" +
                this.jedis.zcount("zset", 1.0, 5.0));
        System.out.println("查看zset集合中element004的权重：" +
                this.jedis.zscore("zset", "element004"));
        System.out.println("查看下标1到2范围内的元素值：" + this.jedis.zrange("zset", 1, 2));
    }

    //哈希表操作
    public void hashOperate() {
        //清空数据
        this.jedis.flushDB();
        System.out.println("=============增=============");
        System.out.println("hashs中添加key001和value001键值对：" +
                this.sharedJedis.hset("hashs", "key001", "value001"));
        System.out.println("hashs中添加key002和value002键值对：" +
                this.sharedJedis.hset("hashs", "key002", "value002"));
        System.out.println("hashs中添加key003和value003键值对：" +
                this.sharedJedis.hset("hashs", "key003", "value003"));
        System.out.println("新增key004和4的整型键值对：" +
                this.sharedJedis.hincrBy("hashs", "key004", 4));
        System.out.println("hashs中的所有值：" + this.jedis.hvals("hashs"));
        System.out.println("=============删=============");
        System.out.println("hashs中删除key002键值对："
                + this.sharedJedis.hdel("hashs", "key002"));
        System.out.println("hashs中的所有值：" + this.jedis.hvals("hashs"));
        System.out.println("=============改=============");
        System.out.println("key004整型键值的值增加100：" +
                this.jedis.hincrBy("hashs", "key004", 1001));
        System.out.println("=============查=============");
        System.out.println("判断key003是否存在："
                + this.jedis.hexists("hashs", "key003"));
        System.out.println("获取key004对应的值：" +
                this.jedis.hget("hashs", "key004"));
        System.out.println("批量获取key001和key003对应的值：" +
                this.jedis.hmget("hashs", "key001", "key003"));
        System.out.println("获取hashs中所有的key：" + this.jedis.hkeys("hashs"));
        System.out.println("获取hashs中所有的value：" + this.jedis.hvals("hashs"));
        System.out.println();
    }


}