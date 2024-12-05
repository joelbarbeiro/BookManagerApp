package pt.ipleiria.estg.dei.books.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Model.Book;

public class BookJsonParser {

    public static ArrayList<Book> parserJsonBooks(JSONArray response) {
        ArrayList<Book> books = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject Book = (JSONObject) response.get(i);
                int idBook = Book.getInt("id");
                String titleBook = Book.getString("titulo");
                String seriesBook = Book.getString("serie");
                String authorBook = Book.getString("autor");
                int yearBook = Book.getInt("ano");
                String coverBook = Book.getString("capa");

                Book auxBook = new Book(
                        idBook,
                        coverBook,
                        yearBook,
                        titleBook,
                        seriesBook,
                        authorBook
                );
                books.add(auxBook);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

    public static Book parserJsonBook(String response) {
        Book auxBook = null;

        try {
            JSONObject Book = new JSONObject(response);
            int idBook = Book.getInt("id");
            String titleBook = Book.getString("titulo");
            String seriesBook = Book.getString("serie");
            String authorBook = Book.getString("autor");
            int yearBook = Book.getInt("ano");
            String coverBook = Book.getString("capa");

            auxBook = new Book(
                    idBook,
                    coverBook,
                    yearBook,
                    titleBook,
                    seriesBook,
                    authorBook
            );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return auxBook;
    }

    // TODO : parserJsonLogin

    public static boolean isConnectionInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

}
