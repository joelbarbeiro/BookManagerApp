package Model;

import android.content.Context;

import java.util.ArrayList;

public class SingletonBookManager {

    private ArrayList<Book> books;
    private static SingletonBookManager instance = null;

    private BookDbHelper bookDbHelper = null;

    public static synchronized SingletonBookManager getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonBookManager(context);
        }
        return instance;
    }

    private SingletonBookManager(Context context) {
        books = new ArrayList<>();
        bookDbHelper = new BookDbHelper(context);
    }

//    private void generateDynamicData() {
//        books = new ArrayList<>();
//        books.add(new Book( R.drawable.programarandroid2, 2024, "Programar em Android AMSI - 1", "2ª Temporada", "AMSI TEAM"));
//        books.add(new Book( R.drawable.programarandroid1, 2024, "Programar em Android AMSI - 2", "2ª Temporada", "AMSI TEAM"));
//        books.add(new Book( R.drawable.logoipl, 2024, "Programar em Android AMSI - 3", "2ª Temporada", "AMSI TEAM"));
//        books.add(new Book( R.drawable.programarandroid2, 2024, "Programar em Android AMSI - 4", "2ª Temporada", "AMSI TEAM"));
//        books.add(new Book( R.drawable.programarandroid1, 2024, "Programar em Android AMSI - 5", "2ª Temporada", "AMSI TEAM"));
//        books.add(new Book( R.drawable.logoipl, 2024, "Programar em Android AMSI - 6", "2ª Temporada", "AMSI TEAM"));
//        books.add(new Book( R.drawable.programarandroid2, 2024, "Programar em Android AMSI - 7", "2ª Temporada", "AMSI TEAM"));
//        books.add(new Book( R.drawable.programarandroid1, 2024, "Programar em Android AMSI - 8", "2ª Temporada", "AMSI TEAM"));
//        books.add(new Book( R.drawable.logoipl, 2024, "Programar em Android AMSI - 9", "2ª Temporada", "AMSI TEAM"));
//        books.add(new Book( R.drawable.programarandroid2, 2024, "Programar em Android AMSI - 10", "2ª Temporada", "AMSI TEAM"));
//    }

    public ArrayList<Book> getBooksDb() {
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
        Book auxBook = bookDbHelper.addBookDb(book);
        if (auxBook != null) {
            books.add(book);
        }
    }

    public void editBookDb(Book book) {
        Book b = getBook(book.getId());
        if (b != null) {
            bookDbHelper.editBookDb(book);
        }
    }

    public void removeBookDb(int bookId) {
        Book b = getBook(bookId);
        if (b != null) {
            if (bookDbHelper.removeBookDb(b.getId()))
                books.remove(b);
        }
    }
}
