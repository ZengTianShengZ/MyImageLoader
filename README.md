# MyImageLoader

轻量级 imageloader
===
![aa](https://github.com/ZengTianShengZ/MyImageLoader/blob/master/img/iiwidim.png)

你可以这么使用：
 
- 在子线程中获取 本地手机图片保存的路径 url
```java
List<String> imgUrlList = ScannerImageUrl.getImageUrl(context);
```
- 将 imgUrlList  给适配器使用，我这里已经封装好了一个适配器 

```java
public class PhotoAdapter extends BaseRecycleViewAdapter {

    private ImageLoader mImageLoader;

    public PhotoAdapter(Context context, List datas) {
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
```
- 当然你也可以自己 new 给适配器 只需这么显示你的图片即可
```java
  mImageLoader.displayBitmap(mContext,grid_item, imageUrl, R.mipmap.ic_launcher,this);
```
-  不过你的适配器需要继承图片回调的接口
```java
implements ImageLoader.ImageCallback
```
- 并且实现该接口
```java
	/**
	 * 图片 缓存回调
	 */
	@Override
	public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
		if (imageView != null && bitmap != null) {
			String url = (String) params[0];
			// 判断 这里的 url 是否 对应  imageView.getTag()
			// 如果 将这句 判断 去掉  那么 就会出现  经常出现的  图片 显示 错位 问题   
			if (url != null && url.equals((String) imageView.getTag())) {

				((ImageView) imageView).setImageBitmap(bitmap);
			}
		}
	}
```

- 为了你方便使用可以添加如下依赖即可
```java 
dependencies {
            compile 'com.zts:imageloader:1.1.1'

    }
```

- 跟多内容请查看源码
