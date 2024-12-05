package pt.ipleiria.estg.dei.books;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import Model.Book;
import Model.SingletonBookManager;

public class BookDetailsActivity extends AppCompatActivity {

    public static final String ID_BOOK = "ID_BOOK";
    private Book book;

    private EditText etTitle, etSeries, etAuthor, etYear;
    private ImageView ivCover;

    public static final String DEFAULT_IMG = "http://amsi.dei.estg.ipleiria.pt/img/ipl_semfundo.png";

    private FloatingActionButton fabSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        int id = getIntent().getIntExtra(ID_BOOK, 0);

        book = SingletonBookManager.getInstance(getApplicationContext()).getBook(id);
        fabSave = findViewById(R.id.fabSave);
        etTitle = findViewById(R.id.et_title);
        etSeries = findViewById(R.id.et_series);
        etAuthor = findViewById(R.id.et_author);
        etYear = findViewById(R.id.et_year);
        ivCover = findViewById(R.id.iv_cover);

        if (book != null) {
            loadBook();
            fabSave.setImageResource(R.drawable.ic_save);
        } else {
            setTitle(getString(R.string.add_book));
            fabSave.setImageResource(R.drawable.ic_add);
        }

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book != null) {
                    //Save book
                    //IF book im saving is valid and fields are not empty function and call it here
                    book.setTitle(etTitle.getText().toString());
                    book.setSerie(etSeries.getText().toString());
                    book.setAutor(etAuthor.getText().toString());
                    book.setYear(Integer.parseInt(etYear.getText().toString()));

                    SingletonBookManager.getInstance(getApplicationContext()).editBookDb(book);

                    //EX 10.2
                    Intent intent = new Intent();
                    intent.putExtra(MenuMainActivity.OP_CODE, MenuMainActivity.EDIT);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    //Add Book
                    //IF book im saving is valid and fields are not empty function and call it here
                    book = new Book(
                            0,
                            DEFAULT_IMG,
                            Integer.parseInt(etYear.getText().toString()),
                            etTitle.getText().toString(),
                            etSeries.getText().toString(),
                            etAuthor.getText().toString()
                    );
                    SingletonBookManager.getInstance(getApplicationContext()).addBookDb(book);
                    //EX 10.2
                    Intent intent = new Intent();
                    intent.putExtra(MenuMainActivity.OP_CODE, MenuMainActivity.ADD);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void loadBook() {
        setTitle("Details: " + book.getTitle());
        etTitle.setText(book.getTitle());
        etSeries.setText(book.getSerie());
        etAuthor.setText(book.getAutor());
        etYear.setText("" + book.getYear());
        Glide.with(getApplicationContext()).
                load(book.getCover()).
                placeholder(R.drawable.logoipl).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                into(ivCover);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (book != null) {
            getMenuInflater().inflate(R.menu.menu_remover, menu);
            return super.onCreateOptionsMenu(menu);
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemRemove) {
            dialogRemove();
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogRemove() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_remove_message);
        builder.setMessage(R.string.remove_book);
        builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SingletonBookManager.getInstance(getApplicationContext()).removeBookDb(book.getId());
                        Intent intent = new Intent();
                        intent.putExtra(MenuMainActivity.OP_CODE, MenuMainActivity.DELETE);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.string_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(R.drawable.ic_dialog_icon)
                .show();
    }

}