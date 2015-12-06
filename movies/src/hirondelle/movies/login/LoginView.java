package hirondelle.movies.login;

import hirondelle.movies.util.ui.OnClose;
import hirondelle.movies.util.ui.StandardDialog;
import java.util.*;
import hirondelle.movies.util.ui.UiUtil;
import javax.swing.*;

/**
  Show a screen asking the user for their user name and password.
  
  <P>This class wires its actions to call the {@link LoginController} when a button 
   is clicked.
   
  <P>This implementation of a login screen can be improved. Currently, it uses 
  {@link StandardDialog}, in order to demonstrate reuse of a standard dialog class. 
  However, a login screen should be a {@link javax.swing.JFrame}, <em>not a dialog</em>.
  This results in some issues :
  <ul>   <li>the close button may not work in older JRE's    <li>there's no image in the title bar  </ul>
   
  You can fix these issues by not using {@link StandardDialog}, and just using a <tt>JFrame</tt>
  instead.
*/
final class LoginView {

  /**  Constructor.  */
  LoginView(LoginController aController){
    fController = aController;    
  }
  
  /** Show the login screen. */
  void showLoginScreen(){
    JFrame NO_OWNER = null;
    fStandardDialog = new StandardDialog(
      NO_OWNER, "Login", 
      true, OnClose.DISPOSE, getBody(), getButtons()
    );
    fStandardDialog.setDefaultButton(fLogin);
    fStandardDialog.display();
  }
  
  /** Allow the user to attempt to log in again. */
  void tryAgain(){
    fMessage.setText("Failed. Please try again.");
  }
  
  /** Return the user name entered by the user. */
  String getUserName(){
    return fUserName.getText();
  }
  
  /** Return the password entered by the user. */
  String getPassword(){
    return fPassword.getPassword().toString();
  }
  
  /** Remove the login screen. */
  void close(){
    fStandardDialog.dispose();
  }
  
  // PRIVATE
  private LoginController fController;
  private StandardDialog fStandardDialog;
  private JTextField fUserName;  
  private JPasswordField fPassword;
  private JLabel fMessage;
  private JButton fLogin;
  
  private JPanel getBody(){
    JPanel result = new JPanel();
    result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
    
    fMessage = new JLabel("Please log in.");
    result.add(fMessage);
    result.add(Box.createVerticalStrut(5));
    
    result.add(new JLabel("User Name"));
    fUserName = new JTextField();
    fUserName.setColumns(15);
    result.add(fUserName);
    
    result.add(new JLabel("Password"));
    fPassword = new JPasswordField();
    result.add(fPassword);
    
    UiUtil.alignAllX(result, UiUtil.AlignX.LEFT);
    return result;
  }

  private List<JButton> getButtons(){
    List<JButton> result = new ArrayList<>();
    fLogin = new JButton("Login");
    fLogin.setActionCommand(LoginController.LOGIN);
    fLogin.addActionListener(fController);
    result.add(fLogin);
    
    JButton cancel = new JButton("Cancel");
    cancel.setActionCommand(LoginController.CANCEL);
    cancel.addActionListener(fController);
    result.add(cancel);
    
    return result;
  }
}

