package hirondelle.movies.edit;

import java.math.BigDecimal;
import java.util.*;

import hirondelle.movies.exception.InvalidInputException;
import hirondelle.movies.util.Util;

/** Data-centric class encapsulating all fields related to movies (a 'model object'). 
 
 <P>This class exists  in order to encapsulate, validate, and sort movie information.
  This class is used both to validate user input, and act as a 'transfer object' when 
  interacting with the database.
 
  <P>Like most model objects, <b>this class would greatly benefit from a JUnit test class, 
  to test its data validation and sorting.</b>*/
public final class Movie implements Comparable<Movie>{

  /**   Constructor taking regular Java objects natural to the domain.
   
   <P>When the user has entered text, this constructor is called indirectly, through 
   {@link #Movie(String, String, String, String, String)}.
   
   @param aId optional, the database identifier for the movie. This item is optional since, 
   for 'add' operations,  it has yet to be assigned by the database.
   @param aTitle has content, name of the movie   @param aDateViewed optional, date the movie was screened by the user
   @param aRating optional, in range 0.0 to 10.0
   @param aComment optional, any comment on the movie
  */
  Movie(
    String aId, String aTitle, Date aDateViewed, BigDecimal aRating, String aComment
  ) throws InvalidInputException {
    fId = aId;
    fTitle = aTitle;
    fDateViewed = aDateViewed;
    fRating = aRating;
    fComment = aComment;
    validateState();
  }
  
  /**   Constructor which takes all parameters as <em>text</em>.
   
   <P>Raw user input is usually in the form of <em>text</em>.
   This constructor <em>first</em> parses such text into the required 'base objects' - 
   {@link Date}, {@link BigDecimal} and so on. If those parse operations <em>fail</em>, 
   then the user is shown an error message. If N such errors are present in user input, 
   then  N <em>separate</em> message will be presented for each failure, one by one.
   
   <P>If all such parse operations <em>succeed</em>, then the "regular" constructor 
   {@link #Movie(String, String, Date, BigDecimal, String)}
   will then be called. It's important to note that this call to the second constructor 
   can in turn result in <em>another</em> error message being shown to the 
   user (just one this time).  */
  Movie(
    String aId, String aTitle, String aDateViewed, String aRating, String aComment
  ) throws InvalidInputException {
      this(
        aId, aTitle, Util.parseDate(aDateViewed, "Date Viewed"), 
        Util.parseBigDecimal(aRating, "Rating"), aComment
      );
  }
  
  String getId(){ return fId; }
  
  /**   This set method is rather artificial. It results from the toy persistence layer. 
   It's dissatisfying to add this method since the class would otherwise be immutable,
   and immutability is a highly desirable characteristic.  */
  void setId(String aId){  fId = aId; }
  
  String getTitle(){ return fTitle; }
  Date getDateViewed(){ return fDateViewed; }
  BigDecimal getRating(){ return fRating; }
  String getComment(){ return fComment; }
  
  @Override public boolean equals(Object aThat){
    if ( this == aThat ) return true;
    if ( !(aThat instanceof Movie) ) return false;
    Movie that = (Movie)aThat;
    return 
      areEqual(this.fTitle, that.fTitle) && 
      areEqual(this.fDateViewed, that.fDateViewed) && 
      areEqual(this.fRating, that.fRating) && 
      areEqual(this.fComment, that.fComment)
    ; 
  }
  
  @Override public int hashCode(){
    int result = 17;
    result = addHash(result, fTitle);
    result = addHash(result, fDateViewed);
    result = addHash(result, fRating);
    result = addHash(result, fComment);
    return result;
  }
  
  @Override public String toString(){
    return 
      "Movie  Id:" + fId + " Title:" + fTitle + " Date Viewed:" + fDateViewed + 
      " Rating:" + fRating + " Comment: " + fComment
    ; 
  }
  
  /** 
   Default sort by Date Viewed, then Title. 
   Dates have the most recent items listed first. 
 */
  @Override public int compareTo(Movie aThat) {
    if ( this == aThat ) return EQUAL;
   
    int comparison = DESCENDING*comparePossiblyNull(this.fDateViewed, aThat.fDateViewed);
    if ( comparison != EQUAL ) return comparison;
    
    comparison = this.fTitle.compareTo(aThat.fTitle);
    if ( comparison != EQUAL ) return comparison;
    
    comparison = comparePossiblyNull(this.fRating, aThat.fRating);
    if ( comparison != EQUAL ) return comparison;
   
    comparison = comparePossiblyNull(this.fComment, aThat.fComment);
    if ( comparison != EQUAL ) return comparison;
    
    return EQUAL;
  }
  
  /** Sort by Title. */
  public static Comparator<Movie> TITLE_SORT = new Comparator<Movie>(){
    @Override public int compare(Movie aThis, Movie aThat) {
      if ( aThis == aThat ) return EQUAL;

      int comparison = aThis.fTitle.compareTo(aThat.fTitle);
      if ( comparison != EQUAL ) return comparison;
      
      comparison = DESCENDING*comparePossiblyNull(aThis.fDateViewed, aThat.fDateViewed);
      if ( comparison != EQUAL ) return comparison;
      
      comparison = comparePossiblyNull(aThis.fRating, aThat.fRating);
      if ( comparison != EQUAL ) return comparison;
     
      comparison = comparePossiblyNull(aThis.fComment, aThat.fComment);
      if ( comparison != EQUAL ) return comparison;
      
      return EQUAL;
    };
  };
  
  /** Sort by Rating (descending), then Date Viewed (descending). */
  public static Comparator<Movie> RATING_SORT = new Comparator<Movie>(){
    @Override public int compare(Movie aThis, Movie aThat) {
      if ( aThis == aThat ) return EQUAL;

      int comparison = DESCENDING*comparePossiblyNull(aThis.fRating, aThat.fRating);
      if ( comparison != EQUAL ) return comparison;

      comparison = DESCENDING*comparePossiblyNull(aThis.fDateViewed, aThat.fDateViewed);
      if ( comparison != EQUAL ) return comparison;
      
      comparison = aThis.fTitle.compareTo(aThat.fTitle);
      if ( comparison != EQUAL ) return comparison;
      
      comparison = comparePossiblyNull(aThis.fComment, aThat.fComment);
      if ( comparison != EQUAL ) return comparison;
      
      return EQUAL;
    };
  };
  
  /** Sort by Comment. */
  public static Comparator<Movie> COMMENT_SORT = new Comparator<Movie>(){
    @Override public int compare(Movie aThis, Movie aThat) {
      if ( aThis == aThat ) return EQUAL;

      int comparison = comparePossiblyNull(aThis.fComment, aThat.fComment);
      if ( comparison != EQUAL ) return comparison;
      
      comparison = aThis.fTitle.compareTo(aThat.fTitle);
      if ( comparison != EQUAL ) return comparison;
      
      comparison = comparePossiblyNull(aThis.fRating, aThat.fRating);
      if ( comparison != EQUAL ) return comparison;

      comparison = DESCENDING*comparePossiblyNull(aThis.fDateViewed, aThat.fDateViewed);
      if ( comparison != EQUAL ) return comparison;
      
      return EQUAL;
    };
  };
  
  // PRIVATE
  private String fId;
  private final String fTitle;
  private final Date fDateViewed;
  private final BigDecimal fRating;
  private final String fComment;
  private static final BigDecimal TEN = new BigDecimal("10.0");
  private static final int EQUAL = 0;
  private static final int DESCENDING = -1;
  
  private void validateState() throws InvalidInputException {
    InvalidInputException ex = new InvalidInputException();
    
    if( ! Util.textHasContent(fTitle) ) {
      ex.add("Title must have content");
    }
    if ( fRating != null ){
      if ( fRating.compareTo(BigDecimal.ZERO) < 0 ) {
        ex.add("Rating cannot be less than 0.");
      }
      if ( fRating.compareTo(TEN) > 0 ) {
        ex.add("Rating cannot be greater than 10.");
      }
    }
    if ( ex.hasErrors() ) {
      throw ex;
    }
  }
  
  private boolean areEqual(Object aThis, Object aThat){
    return aThis == null ? aThat == null : aThis.equals(aThat);
  }
  
  private int addHash(int aHash, Object aField){
    int result = 37*aHash;
    if (aField != null){
      result = result + aField.hashCode();
    }
    return result;
  }
  
  /** Utility method.  */
  private static <T extends Comparable<T>> int comparePossiblyNull(T aThis, T aThat){
    int result = EQUAL;
    int BEFORE = -1;
    int AFTER = 1;
    
    if(aThis != null && aThat != null){ 
      result = aThis.compareTo(aThat);
    }
    else {
      //at least one reference is null - special handling
      if(aThis == null && aThat == null) {
        //do nothing - they are not distinct 
      }
      else if(aThis == null && aThat != null) {
        result = BEFORE;
      }
      else if( aThis != null && aThat == null) {
        result = AFTER;
      }
    }
    return result;
  }
}