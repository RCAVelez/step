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
    String numRequest = getRequestParameter(request, "num", /* defaultValue= */ "0");
    int num = Integer.parseInt(numRequest);
    String json = getXLimitOfComments(datastore, num);
    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {}

  /**
   * Returns the value matching `name` from the request parameter. Returns defaultValue if no value
   * is found.
   */
  private String getRequestParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  /**
   * Returns comments from the provided Datestore in chronological order up to the specified limit.
   *
   * @param limit The number of comments to retrieve. Returns all comments if {@code limit} is -1 or
   *     {@code limit} is larger than the number of comments in the datastore.
   * @return The retrieved comments as a JSON string.
   */
  private static String getXLimitOfComments(DatastoreService datastore, int limit) {
    JsonObject results = new JsonObject();
    JsonArray comments = new JsonArray();
    Query query = new Query("Comments").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery preparedQuery = datastore.prepare(query);
    List<Entity> entities;
    if (limit == -1) {
      entities = Lists.newArrayList(preparedQuery.asIterable());
    } else {
      entities = preparedQuery.asList(FetchOptions.Builder.withLimit(limit));
    }
    for (Entity entity : entities) {
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
