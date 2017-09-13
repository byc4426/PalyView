package surui.huizhou.com.face;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPagerLineIndicator mViewpageLinerlay;
    private android.support.v4.view.ViewPager mViewPager;
    List<String> mTitles = Arrays.asList("识别", "管理");
    List<Fragment> mContents = new ArrayList<>();
    FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        this.mViewpageLinerlay = (ViewPagerLineIndicator) findViewById(R.id.mViewpageLinerlay);
        initDatas();
    }

    private void initDatas() {
        RecognitionFrament fragment0 = new RecognitionFrament();
        mContents.add(fragment0);
        ManagementFrament fragment = new ManagementFrament();
        mContents.add(fragment);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mContents.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mContents.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewpageLinerlay.setViewPager(mViewPager, 0);
        mViewpageLinerlay.setTabItemTitles(mTitles);
    }
}
