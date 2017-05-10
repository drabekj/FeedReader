package cz.drabek.feedreader.articledetail;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.articles.ArticlesPresenter;
import cz.drabek.feedreader.data.Article;

public class ArticleDetailFragment extends Fragment implements ArticleDetailContract.View {

    private static final String TAG = "HONZA-ArticleDetailFrag";

    @NonNull
    private static final String ARGUMENT_ARTICLE_ID = "ARTICLE_ID";

    ArticleDetailContract.Presenter mPresenter;

    private LinearLayout mDetailView;
    private LinearLayout mNoDetailView;
    private TextView mTitle;
    private TextView mDate;
    private TextView mAuthor;
    private TextView mUrl;
    private TextView mContent;
    private String mUrlRaw;


    public ArticleDetailFragment() {
        // Required empty public constructor
    }
    public static ArticleDetailFragment newInstance() {
        return new ArticleDetailFragment();
    }

    @Override
    public void setPresenter(ArticleDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.articledetail_frag, container, false);

        mTitle  = (TextView) root.findViewById(R.id.article_detail_title);
        mDate   = (TextView) root.findViewById(R.id.article_detail_date);
        mAuthor = (TextView) root.findViewById(R.id.article_detail_author);
        mUrl    = (TextView) root.findViewById(R.id.article_detail_url_link);
        mContent= (TextView) root.findViewById(R.id.article_detail_content);

        mDetailView     = (LinearLayout) root.findViewById(R.id.detailLayout);
        mNoDetailView   = (LinearLayout) root.findViewById(R.id.noDetailLayout);

        setRetainInstance(true);
        return root;
    }



    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    /**
     * Show {@param article} in the view.
     *
     * @param article   Article to be displayed
     */
    @Override
    public void showArticle(Article article) {
        Log.d(TAG, "showArticle: url=" + article.getUrl());
        mUrlRaw = article.getUrl();
        String urlString = "<a href='" + mUrlRaw + "'> "
                + getResources().getString(R.string.view_full_article) + " </a>";

        mTitle  .setText(article.getTitle());
        mDate   .setText(article.getDate());
        mAuthor .setText(article.getAuthor());
        mUrl    .setClickable(true);
        mUrl    .setMovementMethod(LinkMovementMethod.getInstance());
        mUrl    .setText(Html.fromHtml(urlString));
        mContent.setText(Html.fromHtml(article.getContent()));

        mDetailView  .setVisibility(View.VISIBLE);
        mNoDetailView.setVisibility(View.GONE);
    }

    /**
     * Show empty screen in view.
     */
    @Override
    public void showNoArticle() {
        mDetailView  .setVisibility(View.GONE);
        mNoDetailView.setVisibility(View.VISIBLE);
    }

    /**
     * Get raw URL for article in the view.
     *
     * @return  String: raw url
     */
    @Override
    public String getArticleUrl() {
        return mUrlRaw;
    }
}
