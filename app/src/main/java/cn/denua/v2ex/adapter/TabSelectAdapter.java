package cn.denua.v2ex.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.denua.v2ex.R;
import cn.denua.v2ex.Tab;

/**
 * cn.denua.v2ex.adapter
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/3/1
 */
public class TabSelectAdapter extends RecyclerView.Adapter<TabSelectAdapter.MViewHolder> {

    private List<Tab> mTabs;
    private OnDragListener mOnDragListener;
    private static boolean ANIMATING = false;

    public TabSelectAdapter(List<Tab> mTabs, OnDragListener mOnDragListener) {
        this.mTabs = mTabs;
        this.mOnDragListener = mOnDragListener;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_dragable_tab, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        holder.setTitle(mTabs.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mTabs.size();
    }

    interface OnDragListener{
        void onDrag(MotionEvent event, int pos);
    }

    class MViewHolder extends RecyclerView.ViewHolder{

        private TextView mTitle;
        private View mContainer;

        @SuppressLint("ClickableViewAccessibility")
        public MViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tv_title);
            mContainer = itemView.findViewById(R.id.container);

            itemView.findViewById(R.id.iv_menu).setOnTouchListener((v,e)->{
                mOnDragListener.onDrag(e, getAdapterPosition());
                return false;
            });
        }
        public void setTitle(String title){
            mTitle.setText(title);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private class DeleteListener implements View.OnTouchListener{

        private float mStartX;
        private float mIncrement;
        private long mGlideTimeSpan;
        private boolean mCanGlide = false;
        private int mContainerWidth;

        private RecyclerView.ViewHolder mViewHolder;
        private View mContainer;


        private DeleteListener(RecyclerView.ViewHolder viewHolder){
            this.mViewHolder = viewHolder;

            mContainer = viewHolder.itemView.findViewById(R.id.container);
            mContainerWidth = mContainer.getWidth();
        }

        @Override
        public boolean onTouch(View vi, MotionEvent event) {

            if (ANIMATING)  return false;
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mStartX = event.getX();
                    mGlideTimeSpan = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mIncrement = Math.abs(event.getX() - mStartX);
                    if (mIncrement > 20 || mCanGlide){
                        mContainer.setX(mContainer.getX() + event.getX() - mStartX);
                        mCanGlide = true;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    cancelDeleteAnim(mContainer);
                    break;
                case MotionEvent.ACTION_UP:
                default:
                    mGlideTimeSpan = System.currentTimeMillis() - mGlideTimeSpan;
                    float s = (mContainerWidth - Math.abs(mContainer.getX())) / mGlideTimeSpan;
                    if (Math.abs(mContainer.getX()) > 250){
                        int moveToX = mContainerWidth * (mContainer.getX() > 0 ? 1: - 1);
                        long speed = (long) (mContainerWidth / s);
                        ObjectAnimator animator = ObjectAnimator.ofFloat(
                                mContainer, "X", moveToX).setDuration(speed);
                        ViewWrapper viewWrapper = new ViewWrapper(mContainer);
                        ObjectAnimator animator1 = ObjectAnimator.ofInt(
                                viewWrapper, "height", 0).setDuration(500);
                        int p = mViewHolder.getAdapterPosition();
                        animator.addUpdateListener(animation -> {
                            if (Math.abs((float)animation.getAnimatedValue()) == mContainerWidth){
                                animator1.start();
                            }
                        });
                        animator1.addUpdateListener( animation -> {
                            if ((int)animation.getAnimatedValue() == 0) {
                                mTabs.remove(p);
                                notifyItemRemoved(p);
                                ANIMATING = false;
                            }
                        });
                        ANIMATING = true;
                        animator.start();
                    } else{
                        cancelDeleteAnim(mContainer);
                    }
            }
            return true;
        }
        private void cancelDeleteAnim(View o){
            ObjectAnimator animator = ObjectAnimator.ofFloat(o, "X", 0).setDuration(400);
            animator.start();
        }
    }
    private class ViewWrapper {

        private View rView;
        public ViewWrapper(View target) {
            rView = target;
        }

        public int getWidth() {
            return rView.getLayoutParams().width;
        }

        public void setWidth(int width) {
            rView.getLayoutParams().width = width;
            rView.requestLayout();
        }

        public int getHeight() {
            return rView.getLayoutParams().height;
        }

        public void setHeight(int height) {
            rView.getLayoutParams().height = height;
            rView.requestLayout();
        }
    }
}
