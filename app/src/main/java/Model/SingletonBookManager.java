package Model;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.books.BookDetailsActivity;
import pt.ipleiria.estg.dei.books.Listeners.BookListener;
import pt.ipleiria.estg.dei.books.Listeners.BooksListener;
import pt.ipleiria.estg.dei.books.Listeners.LoginListener;
import pt.ipleiria.estg.dei.books.MenuMainActivity;
import pt.ipleiria.estg.dei.books.R;
import pt.ipleiria.estg.dei.books.utils.BookJsonParser;

public class SingletonBookManager {

    private ArrayList<Book> books;
    private static SingletonBookManager instance = null;

    private static final String urlAPIBook = "http://172.22.21.41/api/livros";

    private static final String mUrlAPILogin = "http://172.22.21.41/api/auth/login";

    private BookDbHelper bookDbHelper = null;

    private static RequestQueue volleyQueue = null;

    private BooksListener booksListener;

    private BookListener bookListener;

    private LoginListener loginListener;


    public static synchronized SingletonBookManager getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonBookManager(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }

    public SingletonBookManager(Context context) {
        books = new ArrayList<>();
        bookDbHelper = new BookDbHelper(context);
    }

    //REGISTER LISTENERS
    public void setBooksListener(BooksListener booksListener) {
        this.booksListener = booksListener;
    }

    public void setBookListener(BookListener bookListener) {
        this.bookListener = bookListener;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public ArrayList<Book> getBooksBD() {
        books = bookDbHelper.getAllBooksDb();
        return new ArrayList<>(books);
    }

    public Book getBook(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    public void addBookDb(Book book) {
        bookDbHelper.addBookDb(book);
    }

    public void editBookDb(Book book) {
        bookDbHelper.editBookDb(book);
    }

    public void removeBookDb(int bookId) {
        Book b = getBook(bookId);
        if (b != null) {
            bookDbHelper.removeBookDb(b.getId());
        }
    }

    public void addBooksDb(ArrayList<Book> books) {
        bookDbHelper.removeAllBooksDb();
        for (Book b : books) {
            addBookDb(b);
        }
    }

    //region = API METHODS #

    public void getAllBooksApi(final Context context) {
        if (!BookJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();

            if(booksListener != null){
                booksListener.onRefreshBookList(bookDbHelper.getAllBooksDb());
            }
        } else {
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    urlAPIBook,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            System.out.println("---> GETAPI: " + response);
                            books = BookJsonParser.parserJsonBooks(response);
                            addBooksDb(books);

                            if(booksListener != null){
                                booksListener.onRefreshBookList(books);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            volleyQueue.add(request);
        }
    }

    public void addBookApi(final Book book, final Context context) {
        if (!BookJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
        } else {
            StringRequest request = new StringRequest(Request.Method.POST, urlAPIBook, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    addBookDb(BookJsonParser.parserJsonBook(response));

                    if(bookListener != null){
                        bookListener.onRefreshDetails(MenuMainActivity.ADD);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", "AMSI-TOKEN");
                    params.put("titulo", book.getTitle());
                    params.put("serie", book.getSerie());
                    params.put("autor", book.getAutor());
                    params.put("ano", "" + book.getYear());
                    params.put("capa", book.getCover() == null ? BookDetailsActivity.DEFAULT_IMG : book.getCover());
                    return params;
                }
            };
            volleyQueue.add(request);
        }
    }

    public void editBookApi(final Book book, final Context context) {
        if (!BookJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
        } else {
            StringRequest request = new StringRequest(Request.Method.PUT, urlAPIBook + '/' + book.getId()
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    editBookDb(book);

                    if(bookListener != null){
                        bookListener.onRefreshDetails(MenuMainActivity.EDIT);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", "AMSI-TOKEN");
                    params.put("titulo", book.getTitle());
                    params.put("serie", book.getSerie());
                    params.put("autor", book.getAutor());
                    params.put("ano", "" + book.getYear());
                    params.put("capa", book.getCover() == null ? BookDetailsActivity.DEFAULT_IMG : book.getCover());
                    return params;
                }
            };
            volleyQueue.add(request);
        }
    }

    public void removeBookApi(final Book book, final Context context) {
        if (!BookJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
        } else {
            StringRequest request = new StringRequest(Request.Method.DELETE, urlAPIBook + '/' + book.getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    removeBookDb(book.getId());

                    if(bookListener != null){
                        bookListener.onRefreshDetails(MenuMainActivity.DELETE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(request);
        }
    }

    public void loginAPI(final String email, final String password, final String token, Context context){
        if(loginListener != null){
            loginListener.onValidateLogin(token,email,context);
        }
    }

    //endregion
}
