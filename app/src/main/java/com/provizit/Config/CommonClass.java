package com.provizit.Config;

import android.app.Activity;
import android.content.Context;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.recyclerview.widget.RecyclerView;

import com.provizit.R;

public class CommonClass {
    private static final String TAG = CommonClass.class.getSimpleName();
    private Context context;
    private Activity activity;

    public CommonClass(Context context) {
        this.context = context;
        this.activity = (Activity) context;
    }

    public CommonClass(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void AnimationLeft(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layoutanimation);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
