package Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BookDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "dbBooks";
    private static final int DB_VERSION = 1;

    private final SQLiteDatabase db;

    private static final String TABLE_NAME = "books";
    private static final String TITLE = "title";
    private static final String SERIES = "series";
    private static final String AUTHOR = "author";
    private static final String YEAR = "year";
    private static final String COVER = "cover";
    private static final String ID = "id";

    public BookDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBookTable = "CREATE TABLE " + TABLE_NAME +
                "(" + ID + " INTEGER, " +
                TITLE + " TEXT NOT NULL, " +
                SERIES + " TEXT NOT NULL, " +
                AUTHOR + " TEXT NOT NULL, " +
                YEAR + " INTEGER NOT NULL, " +
                COVER + " TEXT" +
                ");";
        db.execSQL(createBookTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void addBookDb(Book book) {
        ContentValues values = new ContentValues();
        values.put(TITLE, book.getTitle());
        values.put(SERIES, book.getSerie());
        values.put(AUTHOR, book.getAutor());
        values.put(YEAR, book.getYear());
        values.put(COVER, book.getCover());

        this.db.insert(TABLE_NAME, null, values);
    }

    public boolean editBookDb(Book book) {
        ContentValues values = new ContentValues();
        values.put(TITLE, book.getTitle());
        values.put(SERIES, book.getSerie());
        values.put(AUTHOR, book.getAutor());
        values.put(YEAR, book.getYear());
        values.put(COVER, book.getCover());

        return this.db.update(TABLE_NAME, values, ID + "= ?", new String[]{"" + book.getId()}) > 0;
    }

    public boolean removeBookDb(int id) {
        return this.db.delete(TABLE_NAME, ID + "= ?", new String[]{"" + id}) == 1;
    }

    public ArrayList<Book> getAllBooks(){
        ArrayList<Book> Books = new ArrayList<>();

        Cursor cursor = this.db.query(TABLE_NAME, new String[]{ID, TITLE, SERIES, AUTHOR, YEAR, COVER}, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                Book $auxBook = new Book(
                        cursor.getInt(0),
                        cursor.getString(5),
                        cursor.getInt(4),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
                Books.add($auxBook);
            }while(cursor.moveToNext());
        }
        return Books;
    }

    public ArrayList<Book> getAllBooksDb() {
        ArrayList<Book> books = new ArrayList<>();

        Cursor cursor = this.db.query(TABLE_NAME, new String[]{ID, TITLE, SERIES, AUTHOR, YEAR, COVER},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Book auxBook = new Book(cursor.getInt(0),
                        cursor.getString(5),
                        cursor.getInt(4),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
                books.add(auxBook);
            } while (cursor.moveToNext());
        }
        return books;
    }

    public void removeAllBooksDb() {
        this.db.delete(TABLE_NAME, null, null);
    }

}
