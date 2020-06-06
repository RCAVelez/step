// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = fetchAllCommentsJson(datastore);

    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity commentsEntity = new Entity("Comments", "comm");
    String body = request.getReader().lines().reduce("", String::concat); // grabs request body
    JsonObject commentJson = new JsonParser().parse(body).getAsJsonObject();
    String name = commentJson.get("name").getAsString();
    String comment = commentJson.get("comment").getAsString();
    long timestamp = System.currentTimeMillis();

    commentsEntity.setProperty("name", name);
    commentsEntity.setProperty("comment", comment);
    commentsEntity.setProperty("timestamp", timestamp);

    datastore.put(commentsEntity);

    response.setContentType("application/json");
    response.getWriter().println(body);
  }

  private static String fetchAllCommentsJson(DatastoreService datastore) {
    JsonObject results = new JsonObject();
    JsonArray comments = new JsonArray();
    Query query = new Query("Comments").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery preparedQuery = datastore.prepare(query);

    for (Entity entity : preparedQuery.asIterable()) {
      String name = (String) entity.getProperty("name");
      String comment = (String) entity.getProperty("comment");
      JsonObject commentObject = new JsonObject();
      commentObject.addProperty("name", name);
      commentObject.addProperty("comment", comment);
      comments.add(commentObject);
    }
    results.add("comments", comments);

    return results.toString();
  }
}
