package cz.drabek.feedreader.articles;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.articledetail.ArticleDetailActivity;
import cz.drabek.feedreader.data.Article;

public class ArticlesFragment extends Fragment
        implements ArticlesContract.View {

    private final String TAG = "ArticleListFragment";
    private final int FEED_ENTRY_LOADER = 1;

    private ArticlesContract.Presenter mPresenter;
    private ArticlesCursorAdapter mListAdapter;
    private ArticleItemListener mItemListener = new ArticleItemListener() {
        @Override
        public void onArticleClick(Article clickedArticle) {
            mPresenter.openArticleDetails(clickedArticle);
        }
    };

    private LinearLayout mArticlesView;
    private LinearLayout mNoArticlesView;
    private ImageView mNoArticlesIcon;
    private TextView mNoArticlesMessage;


    public ArticlesFragment() {
        // Required empty public constructor
    }

    public static ArticlesFragment newInstance() {
        return new ArticlesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.articles_frag, container, false);

        // Set up articles view
        mListAdapter = new ArticlesCursorAdapter(getActivity(), mItemListener);
        ListView listView = (ListView) root.findViewById(R.id.articles_list);
        listView.setAdapter(mListAdapter);

        mArticlesView = (LinearLayout) root.findViewById(R.id.articlesLL);

        // Set up no articles view
        mNoArticlesView = (LinearLayout) root.findViewById(R.id.noArticles);
        mNoArticlesIcon = (ImageView) root.findViewById(R.id.no_articles_icon);
        mNoArticlesMessage = (TextView) root.findViewById(R.id.no_articles_message);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull ArticlesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public interface ArticleItemListener {

        void onArticleClick(Article clickedArticle);

    }

    @Override
    public void showArticleDetailsUi(Article article) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.EXTRA_ARTICLE_ID, article.getId());
        startActivity(intent);
    }

    @Override
    public void showArticles(Cursor articles) {
        mListAdapter.swapCursor(articles);

        mArticlesView.setVisibility(View.VISIBLE);
        mNoArticlesView.setVisibility(View.GONE);
    }

    @Override
    public void showNoArticles() {
        mArticlesView.setVisibility(View.GONE);
        mNoArticlesView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (getView() == null)
            return;

        ((ArticlesActivity) getActivity()).setLoadingIndicator(active);
    }
}
