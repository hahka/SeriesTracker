package fr.hahka.seriestracker.utilitaires;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;

import fr.hahka.seriestracker.DownloadResultReceiver;
import fr.hahka.seriestracker.R;

import static android.support.v7.widget.RecyclerView.OnScrollListener;

/**
 * Created by thibautvirolle on 26/06/2016.
 */
public class ScrollableFragmentWithBottomBar extends Fragment implements DownloadResultReceiver.Receiver {


    protected BottomNavigationBar mBottomNavigationBar;

    // 1 = up, -1 = down
    protected int mDirection = 1;

    protected int mBottomBarTranslationY = 0;

    protected RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBottomNavigationBar =  (BottomNavigationBar) getActivity().findViewById(R.id.bottom_navigation_bar);

        return null;

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }

    public void setRecyclerView(RecyclerView rv) {
        this.rv = rv;
    }

    public void smoothScrollToPosition(int position) {

        if(rv != null)
            rv.smoothScrollToPosition(position);

    }

    public void setScrollBehavior(RecyclerView rv) {

        if(rv != null) {
            setRecyclerView(rv);
            this.rv.addOnScrollListener(new OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0 && mDirection == -1) {
                        // Scrolling up
                        mDirection = 1;
                    } else if (dy < 0 && mDirection == 1) {
                        // Scrolling down
                        mDirection = -1;
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
                            || newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        // Do something
                        if (mDirection == 1) {
                            // Going up
                            mBottomBarTranslationY = mBottomNavigationBar.getHeight();
                        } else if (mDirection == -1) {
                            // Going down
                            mBottomBarTranslationY = 0;
                        }

                        mBottomNavigationBar.animate().translationY(mBottomBarTranslationY);

                    } else {
                        // Do something
                    }
                }
            });
        }

    }


}
