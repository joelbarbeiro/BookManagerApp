package pt.ipleiria.estg.dei.books;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import Model.Book;
import Model.SingletonBookManager;
import pt.ipleiria.estg.dei.books.Adapters.ListBookAdapter;

public class ListBookFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView lvBooks;
    private ArrayList<Book> bookList;
    private FloatingActionButton fabList;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;


    public ListBookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_book, container, false);
        setHasOptionsMenu(true);
        fabList = view.findViewById(R.id.fabList);


        lvBooks = view.findViewById(R.id.lv_books);
        bookList = SingletonBookManager.getInstance().getBooks();
        lvBooks.setAdapter(new ListBookAdapter(getContext(), bookList));
        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), bookList.get(position).getTitle() , Toast.LENGTH_SHORT).show();
                //Fazer code para ir para os details do livro
                Intent intent = new Intent(getContext(), BookDetailsActivity.class);
                intent.putExtra(BookDetailsActivity.ID_BOOK, (int) id);
                startActivityForResult(intent, MenuMainActivity.EDIT);
            }
        });

        fabList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BookDetailsActivity.class);
                startActivityForResult(intent, MenuMainActivity.ADD);
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem itemSearch = menu.findItem(R.id.itemSearch);
        searchView = (SearchView) itemSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Book> tempBooks = new ArrayList<>();

                for (Book b : SingletonBookManager.getInstance().getBooks()) {
                    if (b.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                        tempBooks.add(b);
                    }
                }

                lvBooks.setAdapter(new ListBookAdapter(getContext(), tempBooks));
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MenuMainActivity.ADD || requestCode == MenuMainActivity.EDIT) {
                bookList = SingletonBookManager.getInstance().getBooks();
                lvBooks.setAdapter(new ListBookAdapter(getContext(), bookList));
                switch (requestCode) {
                    case MenuMainActivity.ADD:
                        Snackbar.make(getView(), "Book Added Successfully", Snackbar.LENGTH_SHORT).show();
                        break;
                    case MenuMainActivity.EDIT:
                        if (data.getIntExtra(MenuMainActivity.OP_CODE, 0) == MenuMainActivity.DELETE) {
                            Snackbar.make(getView(), "Book Removed Successfully", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(getView(), "Book Edited Successfully", Snackbar.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRefresh() {
        bookList = SingletonBookManager.getInstance().getBooks();
        lvBooks.setAdapter(new ListBookAdapter(getContext(), bookList));
        swipeRefreshLayout.setRefreshing(false);
    }
}