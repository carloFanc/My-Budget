package zero.mybudget;

 import android.support.v4.app.Fragment;
 import android.support.v4.app.FragmentManager;
 import android.support.v4.app.FragmentStatePagerAdapter;


public class pagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public pagerAdapter(FragmentManager fm, int NumOfTabs) {

        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                tabFragment1 tab1 = new tabFragment1();
                return tab1;
            case 1:
                tabFragment2 tab2 = new tabFragment2();
                return tab2;
            case 2:
                tabFragment3 tab3 = new tabFragment3();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
