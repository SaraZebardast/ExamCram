package ez10.com;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;


public class ImageAdapter extends PagerAdapter {
    private Context mContext;
    private int[] mImageIds = new int[]{R.drawable.steve, R.drawable.rosan, R.drawable.isac, R.drawable.davinci, R.drawable.einstien};


    ImageAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(mImageIds[position]);
        container.addView(imageView, 0);
        return imageView;
    }

    void setAvailableProfilePics(int i) {
        if (i==0) mImageIds = new int[]{R.drawable.steve, R.drawable.rosan, R.drawable.isac};
        else if (i==1) mImageIds = new int[]{R.drawable.steve, R.drawable.rosan, R.drawable.isac, R.drawable.davinci};
        else if (i==2) mImageIds = new int[]{R.drawable.steve, R.drawable.rosan, R.drawable.isac, R.drawable.davinci, R.drawable.einstien};
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}