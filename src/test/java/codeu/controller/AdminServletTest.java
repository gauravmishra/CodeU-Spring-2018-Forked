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
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class AdminServletTest {

  private AdminServlet adminServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;

  @Before
  public void setup() {
    adminServlet = new AdminServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/admin.jsp"))
        .thenReturn(mockRequestDispatcher);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_IncorrectPassword() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("password")).thenReturn("anything");

    AdminStore mockAdminStore = Mockito.mock(AdminStore.class);
    Mockito.when(mockAdminStore.checkPassword("anything")).thenReturn(false);
    adminServlet.setAdminStore(mockAdminStore);
    
    adminServlet.doPost(mockRequest, mockResponse);
    Mockito.verify(mockRequest).setAttribute("error", "Invalid Admin password.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
  
  @Test
  public void testDoPost_CorrectPassword() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("password")).thenReturn("anything");
    
    AdminStore mockAdminStore = Mockito.mock(AdminStore.class);
    UserStore mockUserStore = Mockito.mock(UserStore.class);
    ConversationStore mockConversationStore = Mockito.mock(ConversationStore.class);
    
    List<Conversation> fakeConversationList = new ArrayList<>();
    List<User> fakeUserList = new ArrayList<>();
    fakeConversationList.add(
        new Conversation(
            UUID.randomUUID(), UUID.randomUUID(), "test_conversation", Instant.now()));
    fakeUserList.add(
        new User(
            UUID.randomUUID(), "test_user", "test_password", Instant.now()));
    Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);
    Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);
    Mockito.when(mockAdminStore.checkPassword("anything")).thenReturn(true);
    
    adminServlet.setAdminStore(mockAdminStore);
    adminServlet.setUserStore(mockUserStore);
    adminServlet.setConversationStore(mockConversationStore);
    
    adminServlet.doPost(mockRequest, mockResponse);
    Mockito.verify(mockRequest).setAttribute("users", fakeUserList);
    Mockito.verify(mockRequest).setAttribute("conversations", fakeConversationList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
}
