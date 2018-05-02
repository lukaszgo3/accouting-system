package pl.coderstrust.service.users;


import org.springframework.stereotype.Service;
import pl.coderstrust.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

  private HashMap<String, User> users;

  public void addUser(User user) {
    users.put(user.getUsername(), user);
  }

  public void updateUser(User user) {
    users.replace(user.getUsername(), user);
  }

  public void deleteUser(String username) {
    users.remove(username);
  }

  public List<User> getUsers() {
    return new ArrayList<>(users.values());
  }

  public boolean usernameExist(String username) {
    return users.keySet().contains(username);
  }

}

