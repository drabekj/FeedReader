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
import android.widget.LinearLayout;
import android.widget.ListView;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.articledetail.ArticleDetailActivity;
import cz.drabek.feedreader.articledetail.ArticleDetailContract;
import cz.drabek.feedreader.articledetail.ArticleDetailFragment;
import cz.drabek.feedreader.articledetail.ArticleDetailPresenter;
import cz.drabek.feedreader.data.Article;
import cz.drabek.feedreader.util.Injection;

public class ArticlesFragment extends Fragment implements ArticlesContract.View {

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

    /**
     * Show article list from the local storage in the view.
     *
     * @param articles  Articles to be shown in list
     */
    @Override
    public void showArticles(Cursor articles) {
        mListAdapter.swapCursor(articles);

        mArticlesView.setVisibility(View.VISIBLE);
        mNoArticlesView.setVisibility(View.GONE);
    }

    /**
     * Show empty screen for no articles available.
     */
    @Override
    public void showNoArticles() {
        mArticlesView.setVisibility(View.GONE);
        mNoArticlesView.setVisibility(View.VISIBLE);
    }

    /**
     * Depending on UI, either launch new activity or refresh fragment for the {@param article}
     * @param article   To be displayed in view.
     */
    @Override
    public void showArticleDetailsUi(Article article) {
        if (getActivity().findViewById(R.id.detailContentFrame) != null)
            createForTablet(article);
        else
            createNewActivity(article);
    }

    private void createNewActivity(Article article) {
        Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.EXTRA_ARTICLE_ID, article.getId());
        startActivity(intent);
    }

    private void createForTablet(Article article) {
        ArticleDetailFragment detailFrag = ArticleDetailFragment.newInstance();
        // create presenter
        ArticleDetailContract.Presenter presenter = new ArticleDetailPresenter(
                getContext(),
                getLoaderManager(),
                Injection.provideTasksRepository(getContext()),
                detailFrag);
        presenter.setArticleId(article.getId());

        detailFrag.setPresenter(presenter);

        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.detailContentFrame, detailFrag).commit();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (getView() == null)
            return;

        ((ArticlesActivity) getActivity()).setLoadingIndicator(active);
    }

    // Interface for handling callbacks when article in list is clicked.
    public interface ArticleItemListener {
        void onArticleClick(Article clickedArticle);
    }
}
