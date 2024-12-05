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
import pt.ipleiria.estg.dei.books.R;
import pt.ipleiria.estg.dei.books.utils.BookJsonParser;

public class SingletonBookManager {

    private ArrayList<Book> books;
    private static SingletonBookManager instance = null;

    private static final String urlAPIBook = "http://amsi.dei.estg.ipleiria.pt/api/livros";

    private static final String mUrlAPILogin = "http://amsi.dei.estg.ipleiria.pt/api/auth/login";

    private BookDbHelper bookDbHelper = null;

    private static RequestQueue volleyQueue = null;

    public SingletonBookManager(Context context) {
        books = new ArrayList<Book>();
        bookDbHelper = new BookDbHelper(context);
    }

    public static synchronized SingletonBookManager getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonBookManager(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
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

            //TODO LOAD BOOKS FROM DATABASE
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

                    //TODO IMPLEMENT LISTENERS
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

    public void removeBookApi(final Book book, final Context context){
        if (!BookJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
        } else {
            StringRequest request = new StringRequest(Request.Method.DELETE, urlAPIBook + '/' + book.getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    removeBookDb(book.getId());

                    //TODO IMPLEMENT LISTENERS
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
    //endregion
}
