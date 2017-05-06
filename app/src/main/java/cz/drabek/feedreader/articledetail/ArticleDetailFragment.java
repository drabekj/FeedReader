package cz.drabek.feedreader.articledetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.drabek.feedreader.R;

public class ArticleDetailFragment extends Fragment implements ArticleDetailContract.View {

    @NonNull
    private static final String ARGUMENT_ARTICLE_ID = "ARTICLE_ID";

    ArticleDetailContract.Presenter mPresenter;

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
}
