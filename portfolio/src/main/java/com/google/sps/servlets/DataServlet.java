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

import java.io.IOException;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private static final String[] COMMENTS = {
    "Wow good job on the first portfolio website",
    "You should find some help, the design is wacky",
    "Where am I?"
  };

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = convertArrayToJson(COMMENTS);
    response.setContentType("text/html;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String body = request.getReader().lines().reduce("", String::concat); // grabs request body
    response.setContentType("text/html");
    response.getWriter().println(body);
  }

  private static String convertArrayToJson(String[] comments) {
    JsonArrayBuilder jsonComments = Json.createArrayBuilder();
    for (int index = 0; index < comments.length; index++) {
      jsonComments.add(Json.createObjectBuilder().add("comment", comments[index]).build());
    }
    JsonObject json = Json.createObjectBuilder().add("comments", jsonComments.build()).build();
    return json.toString();
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
