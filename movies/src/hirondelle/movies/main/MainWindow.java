package hirondelle.movies.main;

import hirondelle.movies.LaunchApplication;
import hirondelle.movies.about.AboutAction;
import hirondelle.movies.edit.MovieActionAdd;
import hirondelle.movies.edit.MovieActionChange;
import hirondelle.movies.edit.MovieActionDelete;
import hirondelle.movies.edit.MovieTableModel;
import hirondelle.movies.exit.ExitAction;
import hirondelle.movies.util.Util;
import hirondelle.movies.util.ui.UiUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/** Main window for the application.
 
 <P>A menu bar, and a sortable table containing the user's list of movies.
 
 <P>Some applications would add a confirmation dialog when the user exits.*/
public final class MainWindow {
  
  /** 
   Return an instance of this class.
   
   <P>This class is made a singleton, since there is only one main window.
   Any caller can refresh the main window using
   <PRE>MainWindow.getInstance().refreshView();</PRE>
   This lets the app avoid needing to pass around an object reference 
   to the main window.
  */
  public static MainWindow getInstance() {
    return INSTANCE;
  }
  
  /**
   Build and display the main window.
   @param aUserName user name, as validated by 
   {@link hirondelle.movies.login.LoginController}. 
  */
  public void buildAndShow(String aUserName){
    fUserName = aUserName;
    fLogger.fine("Building GUI for user : " + aUserName);
    buildGui();
  }
  
  /** Refresh the display in response to changing database content. */
  public void refreshView(){
    fMovieTableModel.refreshView();
  }
  
  /** 
   Return the user name passed to {@link #buildAndShow(String)}.
   
  <P>The user name can be accessed anywhere using :
  <PRE>MainWindow.getInstance().getUserName();</PRE>
  */
  public String getUserName(){
    return fUserName;
  }
  
  // PRIVATE 
  
  /** The single instance of this class. */
  private static MainWindow INSTANCE = new MainWindow();
  
  /** Empty constructor prevents the caller from creating an object. */
  private MainWindow() {  }
  
  private MovieTableModel fMovieTableModel;
  private JTable fMovieTable;
  private Action fChangeMovieAction;
  private Action fDeleteMovieAction;
  private String fUserName;
  private static final Logger fLogger = Util.getLogger(MainWindow.class);
  
  /** Build the user interface. */
  private void buildGui(){
    JFrame frame = new JFrame(
      LaunchApplication.APP_NAME + 
      " - " + fUserName.toUpperCase(Locale.ENGLISH)
    ); 
    
    fMovieTableModel = new MovieTableModel();
    fMovieTable = new JTable(fMovieTableModel);
    
    buildActionsAndMenu(frame);
    buildContent(frame);
    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    placeInMiddlePartOfTheScreen(frame);
    addApplicationIcon(frame);
    UiUtil.centerAndShow(frame);
  }

  /** Sort the table.  Listens for clicks on the JTableHeader. */
  private final class SortMovieTable extends MouseAdapter {
    @Override public void mouseClicked(MouseEvent aEvent) {
      fLogger.config("Sorting the table.");
      int columnIdx = fMovieTable.getColumnModel().getColumnIndexAtX(aEvent.getX());
      fMovieTableModel.sortByColumn(columnIdx);
    }
  }
  
  /** Show a dialog to edit a movie.  Listens for double-clicks on the JTable. */
  private final class LaunchEditMovieDialog extends MouseAdapter {
    @Override public void mouseClicked(MouseEvent aEvent) {
      if( aEvent.getClickCount() == 2) {
        fLogger.config("Editing a movie.");
        ActionEvent event = new ActionEvent(this, 0, "");
        fChangeMovieAction.actionPerformed(event);
      }
    }
  }
  
  /** Enable edit and delete actions only when something is selected in the table. */
  private final class EnableEditActions implements ListSelectionListener {
    @Override public void valueChanged(ListSelectionEvent aEvent) {
      fLogger.fine(
        "List selection changed. First:" + aEvent.getFirstIndex() 
        + " Last " + aEvent.getLastIndex()
      );
      if( aEvent.getFirstIndex() != -1) {
        fDeleteMovieAction.setEnabled(true);
        fChangeMovieAction.setEnabled(true);
      }
      else {
        fDeleteMovieAction.setEnabled(false);
        fChangeMovieAction.setEnabled(false);
      }
    }
  }
  
  /** Build the menu bar. */
  private void buildActionsAndMenu(JFrame aFrame) {
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic('F'); 
    Action addMovieAction = new MovieActionAdd(aFrame);
    fileMenu.add(new JMenuItem(addMovieAction));
    fChangeMovieAction = new MovieActionChange(aFrame, fMovieTable, fMovieTableModel);
    fileMenu.add(new JMenuItem(fChangeMovieAction));
    fDeleteMovieAction = new MovieActionDelete(fMovieTable, fMovieTableModel);
    fileMenu.add(new JMenuItem(fDeleteMovieAction));
    
    Action exitAction = new ExitAction();
    fileMenu.add(new JMenuItem(exitAction));
    menuBar.add(fileMenu);
    
    JMenu helpMenu = new JMenu("Help");
    helpMenu.setMnemonic('H');
    helpMenu.add(new JMenuItem(new AboutAction(aFrame)));
    menuBar.add(helpMenu);
    
    aFrame.setJMenuBar(menuBar);
  }
  
  /** Expand the frame to fill the middle part of the screen. */
  private void placeInMiddlePartOfTheScreen(JFrame aFrame) {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension halfScreen = new Dimension(2*screen.width/3, screen.height/2);
    aFrame.setPreferredSize(halfScreen);
  }

  /**
    Custom icon for the upper left corner of the frame.
    Not that a path relative to this class is used. 
   */
  private void addApplicationIcon(JFrame aFrame) {
    ImageIcon icon =  UiUtil.createImageIcon("app_icon.png", "Application icon", this.getClass());
    aFrame.setIconImage(icon.getImage());
  }

  /** Build the main content of the frame. */
  private void buildContent(JFrame aFrame) {
    fMovieTable.setBackground(Color.LIGHT_GRAY);
    
    //relative column widths
    fMovieTable.getColumnModel().getColumn(0).setPreferredWidth(100);
    fMovieTable.getColumnModel().getColumn(1).setPreferredWidth(20);
    fMovieTable.getColumnModel().getColumn(2).setPreferredWidth(20);
    fMovieTable.getColumnModel().getColumn(3).setPreferredWidth(200);
    
    /* 
     Interesting: even though these methods are one-liners, it's 
     still useful to create them, since, from the point of view of the caller, 
     they *greatly* clarify the intent, and 
     read at a higher level of abstraction. 
    */
    clickOnHeaderSortsTable();
    doubleClickShowsEditDialog();
    rowSelectionEnablesActions();
    
    JScrollPane panel = new JScrollPane(fMovieTable);
    aFrame.getContentPane().add(panel);  
  }

  private void clickOnHeaderSortsTable() {
    //generic sorting, not performed here: 
    //fMovieTable.setAutoCreateRowSorter(true); 
    fMovieTable.getTableHeader().addMouseListener(new SortMovieTable());
  }
  
  private void doubleClickShowsEditDialog() {
    fMovieTable.addMouseListener( new LaunchEditMovieDialog() );
  }

  private void rowSelectionEnablesActions() {
    fMovieTable.getSelectionModel().addListSelectionListener(new EnableEditActions());
  }
}
      