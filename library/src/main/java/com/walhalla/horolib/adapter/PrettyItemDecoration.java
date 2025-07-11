package com.walhalla.horolib.adapter;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * {@link RecyclerView} 的 ItemDecoration,
 * {@link RecyclerView#addItemDecoration(RecyclerView.ItemDecoration)}，支持
 * {@link LinearLayoutManager}, {@link GridLayoutManager}, 可自定义方向，颜色，间隔线宽度.
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 1.0.0
 */

public class PrettyItemDecoration extends RecyclerView.ItemDecoration
{
    private static final int DEFAULT_WIDTH = 1;
    public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
    public static final int VERTICAL = OrientationHelper.VERTICAL;

    private Paint paint;
    //Divider宽度，像素
    private int dividerWidth;
    //Divider方向
    private int orientation;
    @ColorInt
    private int color;
    private Rect rect = new Rect();
    private int dividerOffset;

    @ColorInt
    public static final int DEFAULT_COLOR = 0XFF7CB9E8;

    //仅供内部使用的View
    private View v;

    public PrettyItemDecoration()
    {
        this(OrientationHelper.HORIZONTAL);
    }

    public PrettyItemDecoration(int orientation)
    {
        this(orientation, DEFAULT_COLOR);
    }

    public PrettyItemDecoration(int orientation, @ColorInt int color)
    {
        this(orientation, color, DEFAULT_WIDTH);
    }

    public PrettyItemDecoration(int orientation, @ColorInt int color, int dividerWidth)
    {
        this.orientation = orientation;
        this.color = color;
        this.dividerWidth = dividerWidth;
        initPaint();
        dividerOffset = (int) (dividerWidth / 2f);
    }

    private void initPaint()
    {
        paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(this.dividerWidth);
    }

    @SuppressLint("NewApi")
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state)
    {
        super.onDrawOver(c, parent, state);

        if(parent.getLayoutManager() == null)
        {
            return;
        }

        if(parent.getClipToPadding())
        {
            c.clipRect(parent.getPaddingLeft() - dividerOffset,
                    parent.getPaddingTop() - dividerOffset,
                    parent.getWidth() - parent.getPaddingRight() + dividerOffset,
                    parent.getHeight() - parent.getPaddingBottom() + dividerOffset);
        }

        if(orientation == HORIZONTAL)
        {
            drawHorizontal(c, parent);
        }
        else
        {
            drawVertical(c, parent);
        }
    }

    @SuppressLint("NewApi")
    private void drawVertical(Canvas c, RecyclerView parent)
    {
        int childCount = parent.getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            v = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(v, rect);
            c.drawLine(rect.left, rect.top, rect.left, rect.bottom, paint);
        }

        int itemDividerWidth = getItemDividerWidth(parent);
        int spanCount = getSpanCount(parent);

        if(parent.getClipToPadding())
        {
            for(int i = spanCount - 1; i < childCount; i += spanCount)
            {
                v = parent.getChildAt(i);
                parent.getDecoratedBoundsWithMargins(v, rect);
                int left = rect.left + itemDividerWidth;
                c.drawLine(left, rect.top, left, rect.bottom, paint);
            }
        }

        v = parent.getChildAt(childCount - 1);
        parent.getDecoratedBoundsWithMargins(v, rect);
        int left = rect.left + itemDividerWidth;
        c.drawLine(left, rect.top, left, rect.bottom, paint);
    }

    private int getSpanCount(RecyclerView parent)
    {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager)
        {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }
        else if(layoutManager instanceof StaggeredGridLayoutManager)
        {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        else if(layoutManager instanceof LinearLayoutManager)
        {
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if(orientation == LinearLayoutManager.HORIZONTAL)
            {
                return parent.getChildCount();
            }
        }

        return 1;
    }

    @SuppressLint("NewApi")
    private void drawHorizontal(Canvas c, RecyclerView parent)
    {
        int itemDividerWidth = getItemDividerWidth(parent);
        int childCount = parent.getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            v = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(v, rect);
            int right = rect.left + itemDividerWidth;
            c.drawLine(rect.left, rect.top, right, rect.top, paint);
        }

        int spanCount = getSpanCount(parent);
        if(spanCount >= 0)
        {
            for(int i = childCount - spanCount; i < childCount; i++)
            {
                v = parent.getChildAt(i);
                parent.getDecoratedBoundsWithMargins(v, rect);
                int right = rect.left + itemDividerWidth;
                c.drawLine(rect.left, rect.bottom, right, rect.bottom, paint);
            }
        }
    }

    private int getItemDividerWidth(RecyclerView parent)
    {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int width = layoutManager.getWidth() - layoutManager.getPaddingLeft() - layoutManager
                .getPaddingRight();
        if(layoutManager instanceof GridLayoutManager)
        {
            return width / ((GridLayoutManager) layoutManager).getSpanCount();
        }
        else if(layoutManager instanceof StaggeredGridLayoutManager)
        {
            return width / ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }

        return width;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);
        if(orientation == HORIZONTAL)
        {
            outRect.set(0, dividerOffset, 0, -dividerOffset);
        }
        else
        {
            outRect.set(dividerOffset, 0, -dividerOffset, 0);
        }
    }
}
