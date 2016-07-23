package com.winning.sx.common.dao.mongodb;

/**
 * Created by smq on 16/6/21.
 */
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
/*
MongoDB的数据

================================

{
  "_id" : ObjectId("54d3509f30c0ed0f308ed1ef"),
  "cust_id" : "A123",
  "amount" : 500.0,
  "status" : "A"
}
{
  "_id" : ObjectId("54d350a830c0ed0f308ed1f0"),
  "cust_id" : "A123",
  "amount" : 250.0,
  "status" : "A"
}
{
  "_id" : ObjectId("54d350b430c0ed0f308ed1f1"),
  "cust_id" : "A123",
  "amount" : 250.0,
  "status" : "A"
}
{
  "_id" : ObjectId("54d350be30c0ed0f308ed1f2"),
  "cust_id" : "B212",
  "amount" : 200.0,
  "status" : "A"
}
{
  "_id" : ObjectId("54d350c030c0ed0f308ed1f3"),
  "cust_id" : "B212",
  "amount" : 200.0,
  "status" : "A"
}
{
  "_id" : ObjectId("54d350d030c0ed0f308ed1f4"),
  "cust_id" : "A123",
  "amount" : 300.0,
  "status" : "D"
}
* */

public class MongoDBDao {
    private static MongoClient mongoClient;
    private static MongoDatabase db;
    static {
        mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDatabase("local");
    }


    public void MongoDBClientTest() {
        //super();
    }

/*
    public static void main(String[] args) {
        MongoDBClientTest clientTest = new MongoDBClientTest();
        clientTest.testQuery();
        clientTest.testInsert();
        clientTest.testDelete();
        clientTest.testToJsonObject();
        clientTest.testMapReduce();
    }
*/
//test search

    public void testQuery() {
        System.out.println("===========going to select==========");
        MongoCollection collection = db.getCollection("demo");
        BasicDBObject query = new BasicDBObject("cust_id", "B212");
        BasicDBObject returnField1 = new BasicDBObject("cust_id", 1);
        BasicDBObject returnField2 = new BasicDBObject("status", 1);
        FindIterable iterable = collection.find(query);
        MongoCursor cursor = iterable.iterator();

        while (cursor.hasNext()) {
            org.bson.Document user = (org.bson.Document)cursor.next();
            System.out.println(user.get("cust_id"));
            System.out.println(user.toString());
        }
        cursor.close();
    }

    public void mgQuery() {
        System.out.println("===========going to select==========");
        MongoCollection collection = db.getCollection("demo");
        BasicDBObject query = new BasicDBObject("cust_id", "B212");
        BasicDBObject returnField1 = new BasicDBObject("cust_id", 1);
        BasicDBObject returnField2 = new BasicDBObject("status", 1);
        FindIterable iterable = collection.find(query);
        MongoCursor cursor = iterable.iterator();

        while (cursor.hasNext()) {
            org.bson.Document user = (org.bson.Document)cursor.next();
            System.out.println(user.get("cust_id"));
            System.out.println(user.toString());
        }
        cursor.close();
    }

//test insert

    public String testInsert() {
        System.out.println("===========going to insert==========");
        String strList ="MongoDb-insert";

        try {
            System.out.println("===========going to insert==========");
            MongoCollection collection = db.getCollection("demo");


            Document doc = new Document();
            doc.put("cust_id", "A111");
            doc.put("amount", 2101);
            doc.put("status", "C");
            collection.insertOne(doc);


            FindIterable iterable = collection.find();
            MongoCursor cursor = iterable.iterator();
            while (cursor.hasNext()) {
                org.bson.Document user = (org.bson.Document) cursor.next();
                // System.out.println(user.get("cust_id"));
                System.out.println(user.toString());
                strList += user.toString();
            }

        }catch (Exception err){
            System.out.println(err.getMessage());
             strList = err.getMessage();
        }

        return  strList;

    }



//test delete

    public void testDelete() {
        System.err.println("===========going to delete==========");
        MongoCollection collection = db.getCollection("demo");
        BasicDBObject query = new BasicDBObject("status", "C");
        collection.deleteMany(query);
        FindIterable iterable = collection.find();
        MongoCursor cursor = iterable.iterator();
        while (cursor.hasNext()) {
            org.bson.Document user = (org.bson.Document)cursor.next();
            System.out.println(user.toString());
        }
    }



//test string to json object

    public void testToJsonObject() {
        System.out.println("===========to json object==========");
        String jsonString = "{  'title' : 'NoSQL Overview', 'description' : 'No sql database is very fast', 'by_user' : 'tutorials point', 'url' : 'http://www.tutorialspoint.com', 'tags' : [ 'mongodb', 'database', 'NoSQL' ], 'likes' : 10 }";
        BasicDBObject doc = (BasicDBObject) JSON.parse(jsonString);
        System.out.println(doc);
        System.out.println(doc.get("tags").getClass().getCanonicalName());
    }



//test the map reduce

    private void testMapReduce() {
        System.err.println("===========test map reduce==========");
        MongoCollection collection = db.getCollection("demo");

        String map = "function(){emit(this.cust_id,this.amount);}";
        String reduce = "function(key, values){return Array.sum(values)}";

        // MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce,
        // null, MapReduceCommand.OutputType.INLINE, null);

        MapReduceIterable out = collection.mapReduce(map, reduce);

        MongoCursor cursor = out.iterator();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }

//        BasicDBObject query=new BasicDBObject("status","C");
//
//        DBCollection dbcol = mongoClient.getDB("local").getCollection("demo");
//        MapReduceCommand cmd = new MapReduceCommand(dbcol, map, reduce,
//                "outputCollection", MapReduceCommand.OutputType.INLINE, query);
//        MapReduceOutput out2 = dbcol.mapReduce(cmd);
//
//        for (DBObject o : out2.results()) {
//            System.out.println(o.toString());
//        }
        System.out.println("Done");


    }
}