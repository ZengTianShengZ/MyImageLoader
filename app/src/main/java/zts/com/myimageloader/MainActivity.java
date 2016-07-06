package zts.com.myimageloader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.DividerGridItemDecoration;
import utils.ScannerImageUrl;

public class MainActivity extends AppCompatActivity {


    private PhotoFloderItemAdapter mPhotoFloderItemAdapter;
    private RecyclerView mRecyclerView;
    private List<String> gridItemList = new ArrayList<String>();

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplication();

        initData();
    }


    private void initData() {

        mRecyclerView = (RecyclerView) findViewById(R.id._recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(), 4));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));

        new Thread() {
            public void run() {

                gridItemList = ScannerImageUrl.getImageUrl(context);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        mPhotoFloderItemAdapter = new PhotoFloderItemAdapter(context,gridItemList);
                        mRecyclerView.setAdapter(mPhotoFloderItemAdapter);

                    }
                });
            };

        }.start();
    }

}
