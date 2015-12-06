package hirondelle.movies.util;

/**
 Enumeration of kinds of edit operations.
    An edit operation is defined here as either an Add, Change, or Delete. */
public enum Edit {
  
  ADD("Add"), CHANGE("Change"), DELETE("Delete");
  
  @Override public String toString(){
    return fName;
  }
  
  // PRIVATE
  
  /** Enums can only have a private constructor. */
  private Edit(String aName){
    fName = aName;
  }
  
  private String fName;
}
