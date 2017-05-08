package cz.drabek.feedreader.articledetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.drabek.feedreader.R;
import cz.drabek.feedreader.data.Article;

public class ArticleDetailFragment extends Fragment implements ArticleDetailContract.View {

    @NonNull
    private static final String ARGUMENT_ARTICLE_ID = "ARTICLE_ID";

    ArticleDetailContract.Presenter mPresenter;

    private TextView mTitle;
    private TextView mDate;
    private TextView mAuthor;
    private TextView mUrl;
    private TextView mContent;
    private String mUrlRaw;


    public ArticleDetailFragment() {
        // Required empty public constructor
    }

    public static ArticleDetailFragment newInstance(@Nullable int articleId) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_ARTICLE_ID, articleId);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    // TODO implement onCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.articledetail_frag, container, false);

        mTitle  = (TextView) root.findViewById(R.id.article_detail_title);
        mDate   = (TextView) root.findViewById(R.id.article_detail_date);
        mAuthor = (TextView) root.findViewById(R.id.article_detail_author);
        mUrl    = (TextView) root.findViewById(R.id.article_detail_url_link);
        mContent= (TextView) root.findViewById(R.id.article_detail_content);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(ArticleDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    // TODO date
    @Override
    public void showArticle(Article article) {
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
    }

    public String getArticleUrl() {
        return mUrlRaw;
    }
}
