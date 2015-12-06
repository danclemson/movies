package hirondelle.movies.edit;

import hirondelle.movies.util.Util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JTable;

/** Show dialog for editing an existing {@link Movie}. */
public final class MovieActionChange extends AbstractAction  {
  
  /** Constructor. */
  public MovieActionChange(JFrame aFrame, JTable aTable, MovieTableModel aMovieTableModel){
    super("Edit...", null );
    putValue(SHORT_DESCRIPTION, "Edit an existing movie"); 
    putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_E) );
    fFrame = aFrame;
    fTable = aTable;
    fMovieTableModel = aMovieTableModel;
    setEnabled(false);
  }
  
  @Override public void actionPerformed(ActionEvent aActionEvent) {
    fLogger.config("Edit an existing movie.");
    setEnabled(false);
    int row = fTable.getSelectedRow();
    Movie selectedMovie = fMovieTableModel.getMovie(row);
    MovieView view = new MovieView(fFrame, selectedMovie);
  }
  
  // PRIVATE 
  private JFrame fFrame;
  private JTable fTable;
  private MovieTableModel fMovieTableModel;
  private static final Logger fLogger = Util.getLogger(MovieActionAdd.class);
}
