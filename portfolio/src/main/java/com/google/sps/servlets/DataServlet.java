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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = "";
    response.setContentType("text/html;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity commentsEntity = new Entity("Comments");
    String body = request.getReader().lines().reduce("", String::concat); // grabs request body
    JsonElement jelement = new JsonParser().parse(body);
    JsonObject jobject = jelement.getAsJsonObject();
    String name = jobject.get("name").getAsString();
    String comment = jobject.get("comment").getAsString();
    long timestamp = System.currentTimeMillis();

    commentsEntity.setProperty("name", name);
    commentsEntity.setProperty("comment", comment);
    commentsEntity.setProperty("timestamp", timestamp);

    datastore.put(commentsEntity);

    Query query = new Query("Comments").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      name = (String) entity.getProperty("name");
      comment = (String) entity.getProperty("comment");
      System.out.println(name);
      System.out.println(comment);
    }

    response.setContentType("text/html");
    response.getWriter().println(body);
  }
}
