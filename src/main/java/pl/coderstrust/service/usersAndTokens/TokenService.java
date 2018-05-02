package pl.coderstrust.service.usersAndTokens;

import org.springframework.stereotype.Service;
import pl.coderstrust.model.Token;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TokenService {

  private HashMap<String, Token> tokens = new HashMap<>();
  private Random random = new Random();

  public String generateToken() {
    String number;
    do {
      int randomNum = ThreadLocalRandom.current().nextInt();
      number = String.format("%10d", randomNum);
    } while (tokenExist(number));
    Token tokenToAdd = new Token(number, LocalDateTime.now());
    tokens.put(tokenToAdd.getNumber(), tokenToAdd);
    return number;
  }

  public boolean validateToken(String number) {
    Token tokenToValidate = tokens.get(number);
    if (tokenToValidate == null) {
      return false;
    }
    if (tokenToValidate.getDateTime().isBefore(LocalDateTime.now().minusMinutes(15))) {
      tokens.remove(number);
      return false;
    }
    return true;
  }

  public boolean tokenExist(String number) {
    return tokens.containsKey(number);
  }

  public List<Token> getTokens() {
    return new ArrayList<>(tokens.values());
  }

}
