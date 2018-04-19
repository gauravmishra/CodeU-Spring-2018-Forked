<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.User" %>

<!DOCTYPE html>
<html>
<head>
  <title>Admin</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <a href="/login">Login</a>
    <a href="/register">Register</a>
    <a href="/about.jsp">About</a>
  </nav>

  <div id="container">
    <h1>Admin</h1>

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <% if(request.getAttribute("conversations") != null){ %>

    <h1>Conversations</h1>

    <%
    List<Conversation> conversations =
      (List<Conversation>) request.getAttribute("conversations");
    if(conversations == null || conversations.isEmpty()){
    %>
      <h2 style="color:red">No Conversations in Chatty :(</h2>
    <%
    }
    else{
    %>
      <ul class="mdl-list">
    <%
      for(Conversation conversation : conversations){
    %>
      <li><a href="/chat/<%= conversation.getTitle() %>">
        <%= conversation.getTitle() %></a></li>
    <%
      }
    %>
      </ul>
    <%
    }
    %>
    <hr/>


    <h1>Users</h1>

    <%
    List<User> users =
      (List<User>) request.getAttribute("users");
    if(users == null || users.isEmpty()){
    %>
      <h2 style="color:red">No Users in Chatty :(</h2>
    <%
    }
    else{
    %>
      <ul class="mdl-list">
    <%
      for(User user : users){
    %>
      <li><%= user.getName() %></li>
    <%
      }
    %>
      </ul>
    <%
    }
    %>
    <hr/>

    <% }
    else { %>
      <form action="/admin" method="POST">
      <label for="password">Admin Password: </label>
      <input type="password" name="password" id="password">
      <br/><br/>
      <button type="submit">Submit</button>
      </form>
    <% } %>

  </div>
</body>
</html>
