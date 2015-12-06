package hirondelle.movies.login;

import hirondelle.movies.main.MainWindow;
import java.util.logging.Logger;
import hirondelle.movies.util.Util;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**  Allow only authenticated users to log in to the application. 
  
  <P>This implementation is only a toy. It doesn't perform realistic validation of user name and password.
  For your own applications, you will need to supply your own implementation.*/
public final class LoginController implements ActionListener {

  /**   Listen for button clicks on the {@link LoginView} screen - {@link #LOGIN} or {@link #CANCEL}.  */
  @Override public void actionPerformed(ActionEvent aEvent) {
    String command = aEvent.getActionCommand();
    if ( LOGIN.equals(command) ) {
      validateUserCredentials();
    }
    else if( CANCEL.equals(command) ){
      shutDownApplication();
    }
    else {
      throw new AssertionError("Unexpected command: " + command);
    }
  }

  /**
  Ask the end user for their user name and password.
  
  <P>This is the first screen displayed to the user upon startup.
  */
  public void askUserForCredentials(){
    fView = new LoginView(this);
    fView.showLoginScreen();
  }
  
  /**   Validate the user name and password entered by the user.
   
   <P>If the validation succeeds, close the login screen and display the {@link MainWindow}.
   If the validation fails, allow the user to make a few more attempts at login.
   If the validation fails repeatedly, close the application.
  */
  void validateUserCredentials(){
    fNumAttempts++;
    String userName = fView.getUserName();
    String password = fView.getPassword();
    if (isValid(userName, password)){
      fView.close();
      showMainWindow(userName);
    }
    else {
      if(fNumAttempts < MAX_NUM_ATTEMPTS) {
        fView.tryAgain();
      }
      else {
        fLogger.config("Shutting down. User credentials not valid for more than the max number of tries.");
        shutDownApplication();
      }
    }
  }
  
  /** Action command string. */
  static final String LOGIN = "Login";
  
  /** Action command string. */
  static final String CANCEL = "Cancel";

  //  PRIVATE 
  private LoginView fView;
  private int fNumAttempts = 0;
  private static final int MAX_NUM_ATTEMPTS = 3;
  private static final Logger fLogger = Util.getLogger(LoginController.class);
  
  /** 
   Toy implementation. 
   Login is always valid, <em>except</em> the user name is <tt>'failme'</tt>. This is 
   to allow testing of the failure branch. 
  */
  private boolean isValid(String aUserName, String aPassword){
    return Util.textHasContent(aUserName) && ! "failme".equals(aUserName);
  }
  
  /** Display the app's main window to the user.  */
  private void showMainWindow(String aUserName){
    fLogger.config("Showing the main window.");
    MainWindow.getInstance().buildAndShow(aUserName);
  }

  /** Close the login screen and exit the app. */
  private void shutDownApplication() {
    fView.close();
    System.exit(0);
  }  
}
