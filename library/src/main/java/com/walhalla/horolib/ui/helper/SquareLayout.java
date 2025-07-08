package com.walhalla.horolib.ui.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SquareLayout extends RelativeLayout  /*SubsamplingScaleImageView*/ {


    public SquareLayout(Context context) {
        super(context);
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        // Set a square layout.
        //int size = (widthMeasureSpec < heightMeasureSpec) ? widthMeasureSpec : heightMeasureSpec;
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);//widthMeasureSpec, heightMeasureSpec
//
//
//        // получаем рассчитанные размеры кнопки
//        final int height = getMeasuredHeight();    // высота
//        final int width = getMeasuredWidth();    // ширина
//
//        // теперь задаем новый размер
//        // ширину оставляем такую же как у стандартной кнопки
//        // высоту выбираем как максимум между стандартной высотой и шириной
//        setMeasuredDimension(width, Math.max(width, height));
//
//        Log.d(TAG, "onMeasure: " + height + " - " + width);

    }

//    @Override
//    protected void onImageLoaded() {
//        super.onImageLoaded();
//
//
//        /**
//         * Validate if image is small
//         */
//        if (getSWidth() < getWidth()) {
//
//
//            int norm_scale = (getSWidth() > getSHeight())
//                    ? (getWidth() / getSWidth()) : (getHeight() / getSHeight());
//
//
//            System.out.println(String.format("Container size: %sx%s", getWidth(), getHeight()));//480x480
//            System.out.println(String.format("Bitmap size: %sx%s", getSWidth(), getSHeight()));//232x287
//
//
//            setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
//
//            DisplayMetrics metrics = this.getResources().getDisplayMetrics();
//
//            float maxDpi_h = metrics.densityDpi //240               , 320 emulator
//                    * getSHeight() / getHeight();//Dpi
//
//            float maxDpi_w = metrics.densityDpi
//                    * getSWidth() / getWidth();//Dpi
//
//
//            float averageDpi = (metrics.xdpi + metrics.ydpi) / 2.0F;
//            //===================================
//            setMaximumDpi((int) maxDpi_h);
////            //1 дм = 2.54 см
////            Log.d(TAG, "onImageLoaded: "
////                    + metrics.xdpi + " >> "//254 Точные физические пиксели на дюйм экрана в размерности X.
////                    + metrics.ydpi //254 Точные физические пиксели на дюйм экрана в измерении Y
////            );
////            this.setMinScale(norm_scale);//min масштаб | averageDpi / (float) maxDpi_h
//
//            //===================================
//            setMinimumDpi((int) maxDpi_h);
////            this.setMaxScale(norm_scale);//max averageDpi / (float) maxDpi_h
//
//
//            resetScaleAndCenter();
////            setDebug(true);
////
////
////            Log.d(TAG,
////                    metrics.density             //Плотность 1.5 (prestigio) 2.0 - emulator nexus
////                            + " maxDpi: " + getResources().getDisplayMetrics().densityDpi
////                            + "*" + getSHeight() + "/" + getHeight() + "=" + maxDpi_h);
////
////            Log.d(TAG, metrics.density + " maxDpi: " + getResources().getDisplayMetrics().densityDpi
////                    + "*" + getSWidth() + "/" + getSWidth() + "=" + maxDpi_w);
//        }
////
////        Toast.makeText(getContext(), "#" + getScale(), Toast.LENGTH_SHORT).show();
//    }

}