package hirondelle.movies.edit;

import hirondelle.movies.main.MainWindow;
import hirondelle.movies.util.Util;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JTable;

/**  Delete the selected {@link Movie}.
  
  <P>This <tt>Action</tt> is an example of an <tt>Action</tt> that is enabled 
  only under certain circumstances. This <tt>Action</tt> is enabled only when the 
  table has a row selected. Otherwise, it is disabled.*/
public class MovieActionDelete  extends AbstractAction {

  /** Constructor. */
  public MovieActionDelete(JTable aTable, MovieTableModel aMovieTableModel){
    super("Delete", null );
    putValue(SHORT_DESCRIPTION, "Delete the selected movie"); 
    putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_D) );
    fTable = aTable;
    fMovieTableModel = aMovieTableModel;
    setEnabled(false);
  }
  
  /** Delete the {@link Movie} currently selected in the table. */
  @Override public void actionPerformed(ActionEvent aActionEvent) {
    int row = fTable.getSelectedRow();
    Movie selectedMovie = fMovieTableModel.getMovie(row);
    fLogger.config("Deleting the selected movie: " + selectedMovie);
    MovieDAO dao = new MovieDAO();
    dao.delete(selectedMovie.getId());
    setEnabled(false); //this action is now over
    MainWindow.getInstance().refreshView();
  }
  
  // PRIVATE
  private JTable fTable;
  private MovieTableModel fMovieTableModel;
  private static final Logger fLogger = Util.getLogger(MovieActionAdd.class);
}
