package cz.drabek.feedreader.articles;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.data.Article;

public class ArticlesCursorAdapter extends CursorAdapter {

    private ArticlesFragment.ArticleItemListener mItemListener;

    private static class ViewHolder {
        TextView title;
        TextView content;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.article_item_title);
            content = (TextView) view.findViewById(R.id.article_item_desc);
        }
    }

    public ArticlesCursorAdapter(Context context, ArticlesFragment.ArticleItemListener articleItemListener) {
        super(context, null, 0);
        mItemListener = articleItemListener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        final Article article = Article.from(cursor);
        viewHolder.title.setText(article.getTitle());
        viewHolder.content.setText(article.getContent());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onArticleClick(article);
            }
        });
    }
}
