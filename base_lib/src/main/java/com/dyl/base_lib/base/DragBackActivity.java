package com.dyl.base_lib.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dyl.base_lib.R;
import com.dyl.base_lib.base.ui.EdgeDragLayer;

public class DragBackActivity extends AppCompatActivity {
    private FrameLayout mRootContainer;
    protected EdgeDragLayer mDragLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isDisableDrag()) {
            mRootContainer = (FrameLayout) findViewById(android.R.id.content);
            setupDragView();
        }
    }

    /**
     * 设置拖动手势
     */
    protected void setupDragView(){
        if(mDragLayer != null){
            return;
        }
        mDragLayer = new EdgeDragLayer(this);
        ((ViewGroup)getWindow().getDecorView()).addView(
                mDragLayer, new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
        mDragLayer.setOnDragListener(new EdgeDragLayer.DragListener() {
            @Override
            public void onDragEvent(int dis) {
                dragEvent(dis);
            }

            @Override
            public void onCancelDrag() {
            }

            @Override
            public void onDragBackEnd() {
                customFinish();
            }
        });
    }

    void customFinish(){
        finish();
        overridePendingTransition(R.anim.drag_activity_enter_anim, R.anim.drag_activity_exit_anim);
    }

    void dragEvent(int dis){
        if(dis < 0){
            dis = 0;
        }
        //移动activity中内容部分
        mRootContainer.setX(dis);
    }

    protected boolean isDisableDrag(){
        return false;
    }
}