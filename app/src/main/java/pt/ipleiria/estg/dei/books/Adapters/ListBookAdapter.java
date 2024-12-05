package pt.ipleiria.estg.dei.books.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import Model.Book;
import pt.ipleiria.estg.dei.books.R;

public class ListBookAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Book> arrayList;

    public ListBookAdapter(Context context, ArrayList<Book> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_list_book, null);
        }

        ViewHolderList viewHolderList = (ViewHolderList) convertView.getTag();
        if (viewHolderList == null) {
            viewHolderList = new ViewHolderList(convertView);
            convertView.setTag(viewHolderList);
        }

        viewHolderList.update(arrayList.get(position));

        return convertView;
    }

    private class ViewHolderList {
        private TextView tvTitle, tvSeries, tvYear, tvAuthor;
        private ImageView cover;

        public ViewHolderList(View view) {
            tvTitle = view.findViewById(R.id.tvTitleContent);
            tvSeries = view.findViewById(R.id.tvSeriesContent);
            tvYear = view.findViewById(R.id.tvYearContent);
            tvAuthor = view.findViewById(R.id.tvAuthorContent);
            cover = view.findViewById(R.id.imageViewCover);
        }

        private void update(Book book) {
            tvTitle.setText(book.getTitle());
            tvSeries.setText(book.getSerie());
            tvYear.setText("" + book.getYear());
            tvAuthor.setText(book.getAutor());
            Glide.with(context).
                    load(book.getCover()).
                    placeholder(R.drawable.logoipl).
                    diskCacheStrategy(DiskCacheStrategy.ALL).
                    into(cover);
        }
    }
}
