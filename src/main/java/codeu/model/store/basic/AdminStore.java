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

package codeu.model.store.basic;

import org.mindrot.jbcrypt.BCrypt;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class AdminStore {

  /** Singleton instance of AdminStore. */
  private static AdminStore instance;

  /**
   * Returns the singleton instance of AdminStore that should be shared between all servlet classes.
   * Do not call this function from a test; use getTestInstance() instead.
   */
  public static AdminStore getInstance() {
    if (instance == null) {
      instance = new AdminStore();
    }
    return instance;
  }

  /** The Admin Password. */
  private String hashedAdminPassword;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private AdminStore() {
    hashedAdminPassword = BCrypt.hashpw("thefiveguys", BCrypt.gensalt());
  }
  
  public boolean checkPassword(String password) {
    return BCrypt.checkpw(password, hashedAdminPassword);
  }
}
