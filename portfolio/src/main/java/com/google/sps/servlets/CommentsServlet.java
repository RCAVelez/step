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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/comments")
public final class CommentsServlet extends HttpServlet {
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String numRequest = getRequestParameter(request, "num", "0");
    int num = Integer.parseInt(numRequest);
    String json = getXNumOfComments(datastore, num);
    response.setContentType("text/html");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {}

  private String getRequestParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  private static String getXNumOfComments(DatastoreService datastore, int num) {
    JsonObject results = new JsonObject();
    JsonArray comments = new JsonArray();
    Query query = new Query("Comments").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery preparedQuery = datastore.prepare(query);
    List<Entity> entities;
    if (num == -1) {
      entities = Lists.newArrayList(preparedQuery.asIterable());
    } else {
      entities = preparedQuery.asList(FetchOptions.Builder.withLimit(num));
    }

    // PreparedQuery preparedQuery = datastore.prepare(query);
    // for (Entity entity : preparedQuery.asIterable())

    // List<Entity> entities = preparedQuery.asList(FetchOptions.Builder.withLimit(num));

    for (Entity entity : entities) {
      System.out.println(entity);
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
