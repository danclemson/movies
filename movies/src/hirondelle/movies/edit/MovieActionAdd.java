package hirondelle.movies.edit;

import hirondelle.movies.util.Util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

/** Show dialog for adding a new {@link Movie}. */
public final class MovieActionAdd extends AbstractAction  {
  
  /** Constructor. */
  public MovieActionAdd(JFrame aFrame){
    super("Add...", null );
    putValue(SHORT_DESCRIPTION, "Add a new movie"); 
    putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_A) );
    fFrame = aFrame;
  }
  
  @Override public void actionPerformed(ActionEvent aActionEvent) {
    fLogger.config("Adding a new movie.");
    MovieView view = new MovieView(fFrame);
  }
  
  // PRIVATE
  private JFrame fFrame;
  private static final Logger fLogger = Util.getLogger(MovieActionAdd.class);
}
