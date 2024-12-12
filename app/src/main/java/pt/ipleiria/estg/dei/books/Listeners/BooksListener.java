package pt.ipleiria.estg.dei.books.Listeners;

import java.util.ArrayList;

import Model.Book;

public interface BooksListener {
    void onRefreshBookList(ArrayList<Book> bookList);
}
