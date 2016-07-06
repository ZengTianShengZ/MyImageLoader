package utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class ScannerImageUrl {

    /**
     * 遍历 手机 图片 所在的 文件夹
     *
     * @return 文件夹 list
     */
    public static List<String> getImageUrl(Context context){
        List<String> gridItemList = new ArrayList<String>();

        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String columns[] = new String[] { MediaStore.Audio.Media.DATA };
        ContentResolver mContentResolver = context.getContentResolver();

        Cursor cursor = mContentResolver.query(mImageUri, columns, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC");

        int photoPathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        String firstImage = null;

        while (cursor.moveToNext()) {
            // 获取图片的路径
            String path = cursor.getString(photoPathIndex);
            gridItemList.add(path);
        }
        cursor.close();

        return gridItemList;

    }
}
