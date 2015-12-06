package hirondelle.movies.edit;

import hirondelle.movies.util.ui.OnClose;
import hirondelle.movies.util.Edit;
import hirondelle.movies.util.Util;
import hirondelle.movies.util.ui.UiUtil;
import hirondelle.movies.util.ui.StandardDialog;

import java.util.logging.Logger;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
  Dialog allowing user to input {@link Movie} information.

  <P>This view can be used to either add a new Movie, or to change an existing one.
  <P>It's important to note that validation of user entries is <em>not</em> 
  performed by this class. Rather, it's performed by the {@link Movie} class.
*/
final class MovieView {

  /**
   Constructor.
    
   <P>Called when adding a new {@link Movie}.
  */
  MovieView(JFrame aParent) {
    fEdit = Edit.ADD;
    buildGui(aParent, "Add Movie");
    fStandardDialog.display();
  }

  /**
    Constructor.
    
    <P>Called when editing an existing {@link Movie}. The text fields are simply
    prepopulated with the text taken from the currently selected row of the table.
  */
  MovieView(JFrame aParent, Movie aSelectedMovie) {
    fLogger.fine("Editing selected movie:" + aSelectedMovie);
    fEdit = Edit.CHANGE;
    fId = aSelectedMovie.getId();
    buildGui(aParent, "Edit Movie");
    populateFields(aSelectedMovie);
    fStandardDialog.display();
  }

  /**
   Return the movie id. The id is used by the database, but is never shown to the user,
   nor is it ever edited by the end user. This method is supplied since it's
   convenient to carry the id with the other information related to a movie, and the
   {@link MovieDAO} needs a way to uniquely identify records.
  */
  String getId() {
    return fId;
  }

  /** The title of the movie, as entered by the user. */
  String getTitle() {
    return fTitle.getText();
  }

  /** The date the movie was viewed, as entered by the user. */
  String getDateViewed() {
    return fDateViewed.getText();
  }

  /** The movie rating, as entered by the user. */
  String getRating() {
    return fRating.getText();
  }

  /** The comment on the movie, as entered by the user. */
  String getComment() {
    return fComment.getText();
  }

  /** Close the view. */
  void closeDialog() {
    fStandardDialog.dispose();
  }

  /** Return the underlying dialog. */
  JDialog getDialog() {
    return fStandardDialog.getDialog();
  }

  // PRIVATE 
  private StandardDialog fStandardDialog;
  private Edit fEdit;
  private String fId;
  private JTextField fTitle = new JTextField();
  private JTextField fDateViewed = new JTextField();
  private JTextField fRating = new JTextField();
  private JTextField fComment = new JTextField();
  private JButton fEditButton;
  private static final Logger fLogger = Util.getLogger(MovieView.class);

  /** Populate the GUI with data from the movie. */
  private void populateFields(Movie aSelectedMovie) {
    fTitle.setText(Util.format(aSelectedMovie.getTitle()));
    fDateViewed.setText(Util.format(aSelectedMovie.getDateViewed()));
    fRating.setText(Util.format(aSelectedMovie.getRating()));
    fComment.setText(aSelectedMovie.getComment());
  }

  private void buildGui(JFrame aParent, String aDialogTitle) {
    fStandardDialog = new StandardDialog(
      aParent, aDialogTitle, true, OnClose.DISPOSE, getUserInputArea(), getButtons()
    );
    fStandardDialog.setDefaultButton(fEditButton);
  }

  private JPanel getUserInputArea() {
    JPanel result = new JPanel();
    result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));

    addTextField(fTitle, "Title", result);
    addTextField(fDateViewed, "Date Viewed", result);
    addTextField(fRating, "Rating", result);
    addTextField(fComment, "Comment", result);
    UiUtil.alignAllX(result, UiUtil.AlignX.LEFT);
    return result;
  }

  private void addTextField(JTextField aTextField, String aLabel, JPanel aPanel) {
    JLabel label = new JLabel(aLabel);
    aPanel.add(label);
    aPanel.add(aTextField);
    aTextField.setColumns(15);
  }

  private java.util.List<JButton> getButtons() {
    java.util.List<JButton> result = new ArrayList<>();

    fEditButton = new JButton(fEdit.toString());
    fEditButton.addActionListener(new MovieController(this, fEdit));
    result.add(fEditButton);

    JButton cancel = new JButton("Cancel");
    cancel.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent arg0) {
        closeDialog();
      }
    });
    result.add(cancel);
    return result;
  }
}
