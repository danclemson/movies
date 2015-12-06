package hirondelle.movies.util.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
  <b>Standard dialog</b>, centralizing various display policies.
 
  <P>Using a standard class for all dialogs increases the uniformity of the application's
  appearance, and eliminates code repetition.
  
  <P>This standard dialog has the following characteristics :
  <ul>
  <li>it's centered on its owner
  <li>it can't be resized
  <li>it has a border of standard dimensions
  <li>it inherits the parent frame's icon
  <li>the title has standardized content
  <li>a row of buttons are placed at the bottom of the dialog, centered, 
  and with fixed spacing
  <li>the escape key performs the same operation as the {@link OnClose} value passed to
  the constructor
  <li>preserves the usual ALT+TAB behavior when switching between applications
  </ul>
  
  <P> Taken individually, such policies are relatively minor. Taken as a group, they form an
  effective way of establish the overall feel of your application.
  
  <P><b>Login dialogs</b><br>
  Login dialogs represent a special case, since they have no parent JFrame. Thus, they
  cannot inherit an icon. In JDK 6, this can be fixed, by adding a method to this class to
  specify the icon.
  
  <P>In addition, closing a login dialog should cause the application to exit. However,
  {@link JDialog#setDefaultCloseOperation(int)} does not allow for that behavior, while 
  this class does.
  
  <P><em>This class does not extend {@link JDialog}, since it 
    doesn't need to</em>. As a pleasant side-effect of this choice, the javadoc for this 
    class is <em>greatly</em> simplified.
 */
public final class StandardDialog {

  /**
   Construct a standard dialog.
   
   @param aOwner the frame which is the owner/caller/parent of this dialog. This dialog
   gets its icon and its position from the owner. Possibly null. It's strongly recommened to use 
   a non-null owner.
   @param aTitle the text to appear on the title bar of this dialog
   @param aIsModal controls whether this dialog is modal: if <tt>true</tt>, then this
   dialog must be dismissed before you are allowed to return to the main window.
   @param aOnClose specifies desired behavior when this dialog closes 
   @param aBody the body of the dialog, where the user enters information
   @param aButtons a row of buttons appearing at the bottom of this dialog
   */
  public StandardDialog (
    JFrame aOwner, String aTitle, boolean aIsModal, OnClose aOnClose, 
    JPanel aBody, java.util.List<JButton> aButtons
  ) {
    String title = UiUtil.getDialogTitle(aTitle);
    fDialog = new JDialog(aOwner, title, aIsModal);
    JPanel content = new JPanel();
    content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
    content.setBorder(UiUtil.getStandardBorder());
    aBody.setAlignmentX(Component.CENTER_ALIGNMENT);
    content.add(aBody);
    content.add(Box.createVerticalStrut(10));
    content.add(buildButtonPanel(aButtons));
    fDialog.add(content);
    fDialog.setResizable(false);
    fDialog.setDefaultCloseOperation(aOnClose.getIntValue());
    addCancelByEscapeKey(aOnClose);
  }

  /**
   Display the dialog.
    
   <P> The dialog is not automatically displayed in the constructor. This is because some
   callers may want to build a dialog upon startup, but only display it later. (Such a
   style might be chosen in order to slightly improve the apparent responsiveness of the
   application.)
   */
  public void display() {
    UiUtil.centerAndShow(fDialog);
  }

  /** Assign a default button for this dialog. */
  public void setDefaultButton(JButton aButton) {
    fDialog.getRootPane().setDefaultButton(aButton);
  }

  /** Call <tt>dispose</tt> on the underlying dialog object. */
  public void dispose() {
    fDialog.dispose();
  }

  /** Return the underlying dialog object.   */
  public JDialog getDialog() {
    return fDialog;
  }

  // PRIVATE
  private JDialog fDialog;

  /**
    Force the escape key to call the same action as the default {@link OnClose} operation
    passed to the constructor. In some special cases, this does not always work.
   */
  private void addCancelByEscapeKey(final OnClose aOnClose) {
    String CANCEL_ACTION_KEY = "CANCEL_ACTION_KEY";
    int noModifiers = 0;
    KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, noModifiers, false);
    InputMap inputMap = fDialog.getRootPane().getInputMap(
    JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    inputMap.put(escapeKey, CANCEL_ACTION_KEY);
    AbstractAction cancelAction = new AbstractAction() {
      @Override public void actionPerformed(ActionEvent e) {
        if (OnClose.DO_NOTHING == aOnClose) {
          // do nothing
        }
        else if (OnClose.DISPOSE == aOnClose) {
          fDialog.dispose();
        }
        else if (OnClose.HIDE == aOnClose) {
          fDialog.setVisible(false);
        }
        else if (OnClose.EXIT == aOnClose) {
          fDialog.dispose();
          System.exit(0);
        }
        else {
          throw new AssertionError("Unexpected branch for this value of OnClose: " + aOnClose);
        }
      }
    };
    fDialog.getRootPane().getActionMap().put(CANCEL_ACTION_KEY, cancelAction);
  }

  private JPanel buildButtonPanel(java.util.List<JButton> aButtons) {
    JPanel result = new JPanel();
    result.setLayout(new BoxLayout(result, BoxLayout.LINE_AXIS));
    result.add(Box.createHorizontalGlue());
    int count = 0;
    for (JButton button : aButtons) {
      count++;
      result.add(button);
      if (count < aButtons.size()) {
        result.add(Box.createHorizontalStrut(6));
      }
    }
    result.add(Box.createHorizontalGlue());
    return result;
  }
}
