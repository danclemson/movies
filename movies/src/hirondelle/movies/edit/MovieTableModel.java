package hirondelle.movies.edit;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import hirondelle.movies.util.Util;
import javax.swing.table.AbstractTableModel;

/** 
  Table model used by {@link javax.swing.JTable}, explicitly for {@link Movie} objects.  
 
  <P>When a database operation occurs, the view is refreshed by calling 
  {@link #refreshView()}.
  <P>Note this class includes some methods which are unrelated to the needs of the 
  superclass, but are useful in the context of this application. 
 */
public final class MovieTableModel extends AbstractTableModel {
  
  /** Constructor. */
  public MovieTableModel(){
    fDAO = new MovieDAO();
    fMovies = fDAO.list();
  }

  /** 
   Explicitly refresh the view.
  
   <P>This style seems to be cleaner and simpler than implementing 
   a listener on the DAO.
  */ 
  public void refreshView() {
    fMovies = fDAO.list();
    //one might want to preserve the sort order here
    fireTableDataChanged();
  }
  
  /** Returned the selected movie's id. */
  public String getId(int aRow){
    Movie movie = fMovies.get(aRow);
    return movie.getId(); 
  }
  
  /** Return the selected {@link Movie}. */
  public Movie getMovie(int aRow){
    return fMovies.get(aRow);
  }
  
  /** 
   Sort the movies.
   
   When called repeatedly, this method will toggle the sort between 
   ascending and descending.
   @param aIdx index of the column by which to sort.  
  */
  public void sortByColumn(int aIdx){
    fNumClicks++;
    if( aIdx == 1 ) {
      //natural sorting of the Movie class
      Collections.sort(fMovies);
    }
    else {
      Comparator<Movie> comparator = null;
      if ( aIdx == 0 ){
        comparator = Movie.TITLE_SORT;
      }
      else if ( aIdx == 2 ){
        comparator = Movie.RATING_SORT;
      }
      else if ( aIdx == 3 ){
        comparator = Movie.COMMENT_SORT;
      }
      Collections.sort(fMovies, comparator);
    }
    if( (fNumClicks % 2) == 0){
      Collections.reverse(fMovies);
    }
    fireTableDataChanged();
  }
  
  /** Return the number of columns in the table. */
  @Override public int getColumnCount() {
    return 4;
  }
  
  /** Return the number of rows in the table. */
  @Override public int getRowCount() {
    return fMovies.size();
  }
  
  /** Return the <tt>Object</tt> in a specific table cell. */
  @Override public Object getValueAt(int aRow, int aCol) {
    Object result = null;
    Movie movie = fMovies.get(aRow);
    if(aCol == 0) {
      result = movie.getTitle();
    }
    else if(aCol == 1) {
      result = Util.format(movie.getDateViewed());
    }
    else if(aCol == 2) {
      result = movie.getRating();
    }
    else if(aCol == 3) {
      result = movie.getComment();
    }
    return result;
  }
  
  /** Return the name of a specific column. */
  @Override public String getColumnName(int aIdx){
    String result = "";
    if( aIdx == 0) {
      result = "Title";
    }
    else if( aIdx == 1) {
      result = "Viewed";
    }
    else if( aIdx == 2) {
      result = "Rating";
    }
    else if( aIdx == 3) {
      result =  "Comment";
    }
    return result;
  }
  
  // PRIVATE //
  private MovieDAO fDAO;
  private List<Movie> fMovies;
  private int fNumClicks = 0;
}
