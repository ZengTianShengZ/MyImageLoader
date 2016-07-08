package zts.com.myimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.List;

import zts.com.imageloader.adapter.BaseRecycleViewAdapter;
import zts.com.imageloader.adapter.RecycleViewHolder;
import zts.com.imageloader.utils.ImageLoader;

/**
 * Created by Administrator on 2016/7/6.
 */

public class PhotoFloderItemAdapter extends BaseRecycleViewAdapter {

    private ImageLoader mImageLoader;

    public PhotoFloderItemAdapter(Context context, List datas) {
        super(context, R.layout.grid_item, datas);
        this.mContext = context;
        mImageLoader = new ImageLoader();
    }

    @Override
    public void convert(RecycleViewHolder holder, Object t, int holderPosition) {

        String imageUrl = (String) t;
        ImageView grid_item = holder.getView(R.id.grid_item);

        grid_item.setTag(imageUrl);
        mImageLoader.displayBitmap(mContext,grid_item, imageUrl, R.mipmap.ic_launcher,this);

    }

}

