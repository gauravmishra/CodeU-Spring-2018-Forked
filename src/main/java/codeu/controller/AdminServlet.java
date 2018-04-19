// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.data.User;
import codeu.model.data.Conversation;
import codeu.model.store.basic.AdminStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ConversationStore;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the admin page. */
public class AdminServlet extends HttpServlet {

  private AdminStore adminStore;
  private UserStore userStore;
  private ConversationStore conversationStore;

  /**
   * Set up state for handling admin-related requests. This method is only called when running in a
   * server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setAdminStore(AdminStore.getInstance()); // Important for Unit Tests!
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
  }
  
  public void setAdminStore(AdminStore adminStore) {
    this.adminStore = adminStore;
  }
  
  public void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }
  
  public void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * This function fires when a user requests the /admin URL. It simply forwards the request to
   * admin.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the admin password form. It gets the password from
   * the submitted form data, checks that it's valid, and either shows the user the admin page
   * or shows an error to the user.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String password = request.getParameter("password");

    if (adminStore.checkPassword(password)) {
      // Build admin page and return to View.
      List<Conversation> allConversations = conversationStore.getAllConversations();
      List<User> allUsers = userStore.getAllUsers();
      request.setAttribute("conversations", allConversations);
      request.setAttribute("users", allUsers);
    } else {
      request.setAttribute("error", "Invalid Admin password.");
    }
    
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }
}
