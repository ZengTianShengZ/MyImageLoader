package zts.com.myimageloader;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zts.com.imageloader.adapter.DividerGridItemDecoration;
import zts.com.imageloader.utils.ScannerImageUrl;

public class MainActivity extends AppCompatActivity {


    private PhotoFloderItemAdapter mPhotoFloderItemAdapter;
    private RecyclerView mRecyclerView;
    private List<String> imgUrlList = new ArrayList<String>();

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

                imgUrlList = ScannerImageUrl.getImageUrl(context);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        mPhotoFloderItemAdapter = new PhotoFloderItemAdapter(context,imgUrlList);
                        mRecyclerView.setAdapter(mPhotoFloderItemAdapter);

                    }
                });
            };

        }.start();
    }

}
