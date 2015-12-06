/** 
Edit movies. An edit is an 'add', 'change', or 'delete'.

<P>The flow is as follows:
<PRE>{@link hirondelle.movies.main.MainWindow}
  -> {@link hirondelle.movies.edit.MovieActionAdd} (simple Swing Action) - similar for Change
    -> {@link hirondelle.movies.edit.MovieView} (dialog)
      -> {@link hirondelle.movies.edit.MovieController}
        -> {@link hirondelle.movies.edit.Movie} (model) 
        -> {@link hirondelle.movies.edit.MovieDAO} (data access)</PRE>

The delete operation does not proceed through the controller. Many would prefer to 
change that. 
*/
package hirondelle.movies.edit;