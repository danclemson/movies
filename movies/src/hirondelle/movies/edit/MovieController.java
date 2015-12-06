package hirondelle.movies.edit;

import hirondelle.movies.exception.InvalidInputException;
import hirondelle.movies.main.MainWindow;
import hirondelle.movies.util.Edit;
import hirondelle.movies.util.Util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/** Add a new {@link Movie} to the database, or change an existing one. 
 
 <P>It's important to note that this class uses most of the other classes in 
 this feature to get its job done (it doesn't use the <tt>Action</tt> classes):
 <ul>   <li>it gets user input from the view - {@link MovieView}   <li>it validates user input using the model - {@link Movie}   <li>it persists the data using the Data Access Object  - {@link MovieDAO}
 </ul>
*/
final class MovieController implements ActionListener {
  
  /**
   Constructor.
   @param aView user interface
   @param aEdit identifies what type of edit - add or change 
  */
  MovieController(MovieView aView, Edit aEdit){
    fView = aView;
    fEdit = aEdit;
  }
  
  /**
   Attempt to add a new {@link Movie}, or edit an existing one.
   
   <P>If the input is invalid, then inform the user of the problem(s).
   If the input is valid, then add or change the <tt>Movie</tt>, close the dialog, 
   and update the main window's display.  
  */
  @Override public void actionPerformed(ActionEvent aEvent){
    fLogger.fine("Editing movie " + fView.getTitle());
    try {
      createValidMovieFromUserInput();
    }
    catch(InvalidInputException ex){
      informUserOfProblems(ex);
    }
    if ( isUserInputValid() ){
      if( Edit.ADD == fEdit ) {
        fLogger.fine("Add operation.");
        fDAO.add(fMovie);
      }
      else if (Edit.CHANGE == fEdit) {
        fLogger.fine("Change operation.");
        fDAO.change(fMovie);
      }
      else {
        throw new AssertionError();
      }
      fView.closeDialog();
      MainWindow.getInstance().refreshView();
    }
  }

  // PRIVATE 
  private final MovieView fView;
  private Movie fMovie;
  private Edit fEdit;
  private MovieDAO fDAO = new MovieDAO();
  private static final Logger fLogger = Util.getLogger(MovieController.class);
  
  private void createValidMovieFromUserInput() throws InvalidInputException {
    fMovie = new Movie(
      fView.getId(), fView.getTitle(), fView.getDateViewed(), 
      fView.getRating(), fView.getComment()
    );
  }

  private boolean isUserInputValid(){
    return fMovie != null;
  }
  
  private void informUserOfProblems(InvalidInputException aException) {
    Object[] messages = aException.getErrorMessages().toArray();
    JOptionPane.showMessageDialog(
      fView.getDialog(), messages, 
      "Movie cannot be saved", JOptionPane.ERROR_MESSAGE
    );
  }
}