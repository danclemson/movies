package hirondelle.movies.about;

import hirondelle.movies.LaunchApplication;
import hirondelle.movies.util.Util;
import static hirondelle.movies.util.Consts.NEW_LINE;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
  Show a simple 'About' box, displaying the application name and version.

  <P>This implementation is very simple and plain, and uses a {@link javax.swing.JOptionPane}.
*/
public final class AboutAction extends AbstractAction {

  /** Constructor. */
  public AboutAction(JFrame aFrame) {
    super("About", null);
    fFrame = aFrame;
    putValue(SHORT_DESCRIPTION, "About the application");
    putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
  }

  /** Show an 'about' box. */
  @Override public void actionPerformed(ActionEvent aActionEvent) {
    fLogger.config("Showing About box.");
    JOptionPane.showMessageDialog(fFrame, getMessageText(), "About",
    JOptionPane.INFORMATION_MESSAGE);
  }

  // PRIVATE 
  private final JFrame fFrame;
  private static final Logger fLogger = Util.getLogger(AboutAction.class);

  private String getMessageText() {
    StringBuilder result = new StringBuilder(LaunchApplication.APP_NAME + " ");
    result.append(LaunchApplication.APP_VERSION);
    result.append(" - a simple example Java GUI application.");
    result.append(NEW_LINE);
    result.append(NEW_LINE);
    result.append("Please see javapractices.com for more information.");
    return result.toString();
  }
}
