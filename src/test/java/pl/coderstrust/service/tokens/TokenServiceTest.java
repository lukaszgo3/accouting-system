package pl.coderstrust.service.tokens;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Token;
import pl.coderstrust.service.users.UserService;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class TokenServiceTest {

  private TokenService tokenService = new TokenService();

  @Test
  public void shouldGenerateToken() {
    //given
    final String UUIDpattern = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    //when
    String tokenNumber = tokenService.generateToken();
    //then
    assertTrue(tokenNumber.matches(UUIDpattern));
    assertTrue(tokenService.validateToken(tokenNumber));
    assertTrue(tokenService.tokenExist(tokenNumber));
  }

  @Test
  public void shouldValidateToken() {
    //given
    String tokenNumber = tokenService.generateToken();
    String notValidToken = "1234-bla-bla";
    //when
    assertTrue(tokenService.validateToken(tokenNumber));
    assertFalse(tokenService.validateToken(notValidToken));
  }

  @Test
  public void tokenExist() {
    //given
    String tokenNumber = tokenService.generateToken();
    String notValidToken = "1234-bla-bla";
    //when
    assertTrue(tokenService.tokenExist(tokenNumber));
    assertFalse(tokenService.tokenExist(notValidToken));
  }
}