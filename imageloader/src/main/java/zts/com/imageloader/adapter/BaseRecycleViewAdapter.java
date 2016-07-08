package zts.com.imageloader.adapter;

import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import zts.com.imageloader.utils.ImageLoader;

/**
 * RecycleViewAdapter 基类
 * @param <T> mDatas
 */
public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter<RecycleViewHolder> implements ImageLoader.ImageCallback{

	protected Context mContext;
	protected int mLayoutId;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;


	public BaseRecycleViewAdapter(Context context, int layoutId, List<T> datas) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mLayoutId = layoutId;
		mDatas = datas;
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void onBindViewHolder(final RecycleViewHolder holder, final int position) {
		holder.updatePosition(position);
		convert(holder, mDatas.get(position), holder.getAdapterPosition());
	}

	@Override
	public RecycleViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

		return RecycleViewHolder.get(mContext, parent, mLayoutId);
	}

	public abstract void convert(RecycleViewHolder holder, T t, int holderPosition);


	/**
	 * 图片 缓存回调
	 */
	@Override
	public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
		if (imageView != null && bitmap != null) {
			String url = (String) params[0];
			// 判断 这里的 url 是否 对应  imageView.getTag()
			// 如果 将这句 判断 去掉  那么 就会出现  经常出现的  图片 显示 错位 问题 ！！！！
			if (url != null && url.equals((String) imageView.getTag())) {

				((ImageView) imageView).setImageBitmap(bitmap);
			}
		}
	}
}
