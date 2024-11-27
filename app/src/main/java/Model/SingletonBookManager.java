package Model;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.books.R;

public class SingletonBookManager {

    private ArrayList<Book> books;
    private static SingletonBookManager instance = null;

    public static synchronized SingletonBookManager getInstance() {
        if (instance == null) {
            instance = new SingletonBookManager();
        }
        return instance;
    }

    private SingletonBookManager() {
        generateDynamicData();
    }

    private void generateDynamicData() {
        books = new ArrayList<>();
        books.add(new Book( R.drawable.programarandroid2, 2024, "Programar em Android AMSI - 1", "2ª Temporada", "AMSI TEAM"));
        books.add(new Book( R.drawable.programarandroid1, 2024, "Programar em Android AMSI - 2", "2ª Temporada", "AMSI TEAM"));
        books.add(new Book( R.drawable.logoipl, 2024, "Programar em Android AMSI - 3", "2ª Temporada", "AMSI TEAM"));
        books.add(new Book( R.drawable.programarandroid2, 2024, "Programar em Android AMSI - 4", "2ª Temporada", "AMSI TEAM"));
        books.add(new Book( R.drawable.programarandroid1, 2024, "Programar em Android AMSI - 5", "2ª Temporada", "AMSI TEAM"));
        books.add(new Book( R.drawable.logoipl, 2024, "Programar em Android AMSI - 6", "2ª Temporada", "AMSI TEAM"));
        books.add(new Book( R.drawable.programarandroid2, 2024, "Programar em Android AMSI - 7", "2ª Temporada", "AMSI TEAM"));
        books.add(new Book( R.drawable.programarandroid1, 2024, "Programar em Android AMSI - 8", "2ª Temporada", "AMSI TEAM"));
        books.add(new Book( R.drawable.logoipl, 2024, "Programar em Android AMSI - 9", "2ª Temporada", "AMSI TEAM"));
        books.add(new Book( R.drawable.programarandroid2, 2024, "Programar em Android AMSI - 10", "2ª Temporada", "AMSI TEAM"));
    }

    public ArrayList<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public Book getBook(int id){
        for(Book book: books){
            if(book.getId() == id){
                return book;
            }
        }
        return null;
    }

    public void addBook(Book book){
        books.add(book);
    }

    public void editBook(Book book){
        Book b = getBook(book.getId());
        if(b != null){
            b.setTitle(book.getTitle());
            b.setAutor(book.getAutor());
            b.setYear(book.getYear());
            b.setSerie(book.getSerie());
        }
    }

    public void removeBook(int bookId){
        Book b = getBook(bookId);
        if(b != null){
            books.remove(b);
        }
    }
}
