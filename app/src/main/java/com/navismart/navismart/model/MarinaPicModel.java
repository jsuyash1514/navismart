package com.navismart.navismart.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.navismart.navismart.R;

public class MarinaPicModel {
    Bitmap pic;

    public MarinaPicModel(Bitmap pic) {
        this.pic = pic;
    }

    public Bitmap getPic() {
        return pic;
    }
}
