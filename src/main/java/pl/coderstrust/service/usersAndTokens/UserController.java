package pl.coderstrust.service.usersAndTokens;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Messages;
import pl.coderstrust.model.User;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(value = "", method = RequestMethod.POST)
  @ApiOperation(value = "Add user")
  public ResponseEntity addUser(@RequestBody User user) {
    List<String> userState = user.validate();
    if (!userState.isEmpty()) {
      return ResponseEntity.badRequest().body(userState);
    }
    if (userService.usernameExist(user.getUsername())) {
      return ResponseEntity.ok(Messages.USER_ALREADY_EXIST);
    }
    userService.addUser(user);
    return ResponseEntity.ok(Messages.USER_ADDED);
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public List<User> getUsers() {
    return userService.getUsers();
  }

  //  @RequestMapping(value = "/{companyId}/{paymentId}", method = RequestMethod.GET)
//  @ApiOperation(value = "Get payment by id.")
//  public ResponseEntity getPaymentById(
//      @PathVariable("companyId") long companyId,
//      @PathVariable("paymentId") long paymentId) {
//    if (!companyService.idExist(companyId)) {
//      return ResponseEntity.badRequest().body(Messages.COMPANY_NOT_EXIST);
//    }
//    if (!paymentService.idExist(companyId, paymentId)) {
//      String companyName = companyService.findEntry(companyId).getName();
//      return ResponseEntity.badRequest().body("Payment under id " + paymentId + " doesn't exist "
//          + "in company " + companyName + " payments list.");
//    }
//    return ResponseEntity.ok().body(paymentService.getPayment(companyId, paymentId));
//  }
//
//  @RequestMapping(value = "/{companyId}/{paymentId}", method = RequestMethod.PUT)
//  @ApiOperation(value = "Updates the payment by id.")
//  public ResponseEntity updatePayment(
//      @PathVariable("companyId") long companyId,
//      @PathVariable("paymentId") long paymentId,
//      @RequestBody Payment payment) {
//    if (!companyService.idExist(companyId)) {
//      return ResponseEntity.badRequest().body(Messages.COMPANY_NOT_EXIST);
//    }
//    if (!paymentService.idExist(companyId, paymentId)) {
//      String companyName = companyService.findEntry(companyId).getName();
//      return ResponseEntity.badRequest().body("Payment under id " + paymentId + " doesn't exist "
//          + "in company " + companyName + " payments list.");
//    }
//    List<String> entryState = payment.validate();
//    if (!entryState.isEmpty()) {
//      return ResponseEntity.badRequest().body(entryState);
//    }
//    paymentService.updatePayment(companyId, payment);
//    return ResponseEntity.ok().build();
//  }
//

  @RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
  @ApiOperation(value = "Remove user by username.")
  public ResponseEntity removeUser(
      @PathVariable("username") String username) {
    if (!userService.usernameExist(username)) {
      return ResponseEntity.badRequest().body(Messages.USER_NOT_EXIST);
    }
    userService.deleteUser(username);
    return ResponseEntity.ok().body(Messages.USER_DELETED);
  }


}