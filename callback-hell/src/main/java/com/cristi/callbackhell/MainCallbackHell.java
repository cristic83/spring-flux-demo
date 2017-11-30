package com.cristi.callbackhell;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

public class MainCallbackHell {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        JsonObject config = new JsonObject()
                .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
                .put("driver_class", "org.hsqldb.jdbcDriver")
                .put("max_pool_size", 30);
        SQLClient client = JDBCClient.createShared(vertx, config);
        initDatabase(client);

        vertx.createHttpServer().requestHandler(req -> {
            client.getConnection(res -> {
                if (res.succeeded()) {

                    SQLConnection connection = res.result();

                    connection.query("SELECT * FROM person", res2 -> {
                        if (res2.succeeded()) {

                            ResultSet rs = res2.result();
                            // Do something with results
                            req.response()
                                    .putHeader("content-type", "text/plain")
                                    .end(rs.getResults().get(0).encodePrettily());
                        } else {
                            req.response()
                                    .putHeader("content-type", "text/plain")
                                    .end(res2.toString());
                        }
                    });
                } else {
                    // Failed to get connection - deal with it
                    req.response()
                            .putHeader("content-type", "text/plain")
                            .end(res.toString());
                }
            });

        }).listen(80, "0.0.0.0");

    }

    private static void initDatabase(SQLClient client) {
        client.getConnection(res -> {
           if (res.succeeded()) {
               String createTableCommand = "CREATE TABLE person (\n" +
                       "            id INT NOT NULL,\n" +
                       "            name VARCHAR(50) NOT NULL,\n" +
                       "            PRIMARY KEY (id)\n" +
                       ");";
               System.out.println(createTableCommand);
               res.result().execute(createTableCommand, event -> {
                   if (event.succeeded()) {
                       client.getConnection(res2 -> {
                          if (res2.succeeded()) {
                              res2.result().execute("INSERT INTO person(id, name) VALUES(1, 'john doe')", res3 -> {
                                 if (res3.succeeded()) {
                                     System.out.println("Init db succeeded.");
                                 }
                                 else {
                                     System.out.println("Init db failed: " + res3);
                                 }
                              });
                          } else {
                              System.out.println("Error connecting to the db for insert: " + res2);
                          }
                       });
                   } else {
                       System.out.println("Error creating table: " + event);
                   }
               });
           } else {
               System.out.println("Error connecting to the db for initializing it: " + res);
           }
        });
    }
}
