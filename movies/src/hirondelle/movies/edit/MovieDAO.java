package hirondelle.movies.edit;

import hirondelle.movies.exception.InvalidInputException;
import hirondelle.movies.main.MainWindow;
import hirondelle.movies.util.Util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 Data Access Object (DAO) for {@link Movie} objects.
 
  <P> Implements persistence for movie information. This class uses a simple text file called
  <tt>movie_list_for_&lt;<em>user name</em>&gt;.txt</tt>, stored locally, in the application's
  home directory.
  Each logged in user gets their own list. Each logged in user can see their own list, 
  but they cannot see anyone else's list.
  
  <P>The format of the file is specific to this application. The file should not be edited 
  directly by an end user, in case the format is violated.
   
  <P>Upon startup, all records in the data-store are read into memory. Edits are performed initially
  only in memory. When the application shuts down, then the updated data store is written
  back to the disk, for use during the next launch of the application.
 */
public final class MovieDAO {

  /**
    Save all data to a text file. Must be called explicitly when the
    app shuts down, in order to save all edits.
  */
  public void shutdown() {
    fLogger.fine("Saving all movie records to file.");
    String fileContents = buildFileContents();
    writeStringToFile(fileContents);
  }

  /** Add a new {@link Movie}. */
  void add(Movie aMovie) {
    String id = nextId();
    aMovie.setId(id.toString());
    fTable.put(id, aMovie);
  }

  /** Change an existing {@link Movie}. */
  void change(Movie aMovie) {
    fTable.put(aMovie.getId(), aMovie);
  }

  /**
   * List all {@link Movie}s. Order is the natural order of the {@link Movie} class
   * (descending date, then title).
   */
  List<Movie> list() {
    List<Movie> result = new ArrayList<>(fTable.values());
    Collections.sort(result);
    return result;
  }

  /** Delete an existing {@link Movie}, given the movie id. */
  void delete(String aMovieId) {
    fTable.remove(aMovieId);
  }

  // PRIVATE 
  private static final Map<String, Movie> fTable = new LinkedHashMap<>();
  private static int fNextId = 0;
  private static final String MOVIES_FILE_NAME = "movie_list_for_";
  private static final String DELIMITER = "|";
  private static final String NULL = "NULL";
  private static final Logger fLogger = Util.getLogger(MovieDAO.class);
  private final static Charset ENCODING = StandardCharsets.UTF_8;
  
  static {
    readInMovieFileUponStartup();
    fLogger.config("Number of movies read in from file: " + fTable.size());
  }

  private static void readInMovieFileUponStartup() {
    Path moviesPath = Paths.get(getMovieFileName());
    fLogger.fine("Reading movies from :" + moviesPath);
    String line = "";
    try (Scanner scanner = new Scanner(moviesPath, ENCODING.name())){
      while (scanner.hasNextLine()) {
        line = scanner.nextLine();
        if (Util.textHasContent(line)) {
          parseLine(line);
        }
      }
    }
    catch (FileNotFoundException ex) {
      fLogger.config("Movies file not present. Will be created when the app closes.");
    }
    catch (InvalidInputException ex) {
      fLogger.severe("Movies file: date-viewed field not in expected format: " + line);
    }
    catch (NoSuchElementException ex) {
      fLogger.severe("Movies file: Not in expected format: " + line);
    }
    catch(IOException ex){
      fLogger.severe("Unable to access the movies file.");
    }
  }

  private static void parseLine(String aLine) throws InvalidInputException {
    Scanner scanner = new Scanner(aLine);
    // note how the quoting is needed here, since '|' is a special character in
    // regular expressions :
    scanner.useDelimiter(Pattern.quote(DELIMITER));
    scanner.useLocale(Locale.US);
    if (scanner.hasNext()) {
      String title = scanner.next();
      Date viewed = Util.parseDate(maybeNull(scanner.next()), "Date Viewed");
      BigDecimal rating = Util.parseBigDecimal(maybeNull(scanner.next()), "Rating");
      String comment = maybeNull(scanner.next());
      Movie movie = new Movie(nextId().toString(), title, viewed, rating, comment);
      fTable.put(movie.getId(), movie);
    }
    scanner.close();
  }

  private static String nextId() {
    ++fNextId;
    return String.valueOf(fNextId);
  }

  private void appendTo(StringBuilder aText, Object aField, String aAppend) {
    if (Util.textHasContent(Util.format(aField))) {
      aText.append(Util.format(aField));
    }
    else {
      aText.append(NULL);
    }
    aText.append(aAppend);
  }

  private static String maybeNull(String aText) {
    return NULL.equals(aText) ? null : aText;
  }

  private static String getMovieFileName() {
    return MOVIES_FILE_NAME + MainWindow.getInstance().getUserName().toLowerCase(Locale.ENGLISH) + ".txt";
  }

  /** Create a string, holding all movie records. */
  private String buildFileContents() {
    String NEW_LINE = System.getProperty("line.separator");
    StringBuilder result = new StringBuilder();
    for (Movie movie : fTable.values()) {
      appendTo(result, movie.getTitle(), DELIMITER);
      appendTo(result, movie.getDateViewed(), DELIMITER);
      appendTo(result, movie.getRating(), DELIMITER);
      appendTo(result, movie.getComment(), NEW_LINE);
    }
    return result.toString();
  }

  /** Write string containing all movie records to a file - overwrite the whole file. */
  private void writeStringToFile(String aFileContents) {
    Path moviesPath = Paths.get(getMovieFileName());
    fLogger.fine("Writing movies to: " + moviesPath);
    try (BufferedWriter writer = Files.newBufferedWriter(moviesPath, ENCODING)){
      writer.write(aFileContents);
    }    
    catch (FileNotFoundException ex) {
      fLogger.severe("Cannot find movies text file.");
    }
    catch (IOException ex) {
      fLogger.severe("Problem while saving movies text file.");
    }
  }
}
