package in.srain.cube.views.ptr;

import android.graphics.PointF;

public class PtrIndicator {

    public static  boolean isPullHeader=false;
    public static  boolean isPullFooter=false;

    public final static int POS_START = 0;
    protected int mOffsetToRefresh = 0;
    protected int  mOffsetToLoad = 0;
    private PointF mPtLastMove = new PointF();
    private float mOffsetX;
    private float mOffsetY;
    private int mCurrentPos = 0;
    private int mLastPos = 0;
    private int mHeaderHeight;
    private int mFooterHeight;
    private int mPressedPos = 0;

    private float mRatio= 1.2f;
    private float mResistance = 1.7f;
    private boolean mIsUnderTouch = false;
    private int mOffsetToKeepHeaderWhileLoading = -1;
    private int mOffsetToKeepFooterWhileLoading=-1;
    // record the refresh complete position
    private int mRefreshCompleteY = 0;

    public boolean isUnderTouch() {
        return mIsUnderTouch;
    }

    public float getResistance() {
        return mResistance;
    }

    public void setResistance(float resistance) {
        mResistance = resistance;
    }

    public void onRelease() {
        mIsUnderTouch = false;
    }

    public void onUIRefreshComplete() {
        mRefreshCompleteY = mCurrentPos;
    }

    public boolean goDownCrossFinishPosition() {
        return mCurrentPos >= mRefreshCompleteY;
    }
    public boolean goUpCrossFinishPosition() {
        return mCurrentPos <= mRefreshCompleteY;
    }

    protected void processOnMove(float currentX, float currentY, float offsetX, float offsetY) {
        setOffset(offsetX, offsetY / mResistance);
    }

    public void setRatioOfHeight(float ratio) {
        mRatio = ratio;
        mOffsetToRefresh = (int) (mHeaderHeight * ratio);
        mOffsetToLoad=(int)(mFooterHeight*ratio);
    }
    public float getRatio() {
        return mRatio;
    }

    public int getOffsetToRefresh() {
        return mOffsetToRefresh;
    }
    public int getOffsetToLoad() {
        return mOffsetToLoad;
    }

    public void setOffsetToRefresh(int offset) {
        mRatio = mHeaderHeight * 1f / offset;
        mOffsetToRefresh = offset;
    }
    public void setOffsetToLoad(int offset) {
        mRatio = mFooterHeight * 1f / offset;
        mOffsetToLoad = offset;
    }

    public void onPressDown(float x, float y) {
        mIsUnderTouch = true;
        mPressedPos = mCurrentPos;
        mPtLastMove.set(x, y);
    }

    public final void onMove(float x, float y) {
        float offsetX = x - mPtLastMove.x;
        float offsetY = (y - mPtLastMove.y);
        processOnMove(x, y, offsetX, offsetY);
        mPtLastMove.set(x, y);
    }

    protected void setOffset(float x, float y) {
        mOffsetX = x;
        mOffsetY = y;
    }

    public float getOffsetX() {
        return mOffsetX;
    }

    public float getOffsetY() {
        return mOffsetY;
    }

    public int getLastPosY() {
        return mLastPos;
    }

    public int getCurrentPosY() {
        return mCurrentPos;
    }
    public void setCurrentPosY(int pos){
        this.mCurrentPos=pos;
    }

    /**
     * Update current position before update the UI
     */
    public final void setCurrentPos(int current) {
        mLastPos = mCurrentPos;
        mCurrentPos = current;
        onUpdatePos(current, mLastPos);
    }

    protected void onUpdatePos(int current, int last) {

    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    public void setHeaderHeight(int height) {
        mHeaderHeight = height;
        updateHeaderHeight();
    }

    protected void updateHeaderHeight() {
        mOffsetToRefresh = (int) (mRatio * mHeaderHeight);
    }

    public void setFooterHeight(int height) {
        mFooterHeight = height;
        updateFooterHeight();
    }

    protected void updateFooterHeight() {
        mOffsetToLoad = (int) (mRatio * mFooterHeight);
    }


    public void convertFrom(PtrIndicator ptrSlider) {
        mCurrentPos = ptrSlider.mCurrentPos;
        mLastPos = ptrSlider.mLastPos;
        mHeaderHeight = ptrSlider.mHeaderHeight;
    }

    public boolean hasLeftStartPosition() {
        return mCurrentPos > POS_START;
    }
    public boolean hasLeftBottomPosition() {
        return mCurrentPos<POS_START;
    }
    public boolean hasJustLeftStartPosition() {
        return mLastPos == POS_START && hasLeftStartPosition();
    }
    public boolean hasJustLeftBottomPosition() {
        return mLastPos == POS_START && hasLeftBottomPosition();
    }
    public boolean hasJustBackToStartPosition() {
        return mLastPos != POS_START && isInStartPosition();
    }
    public boolean hasJustBackToBottomPosition() {
        return mLastPos != POS_START && isInBottomPosition();
    }

    public boolean isOverOffsetToRefresh() {
        return mCurrentPos >= getOffsetToRefresh();
    }
    public boolean isOverOffsetToLoad() {
        return -mCurrentPos >=getOffsetToLoad();//计算保持量展示代替
    }

    public boolean hasMovedAfterPressedDown() {
        return mCurrentPos != mPressedPos;
    }

    public boolean isInStartPosition() {
        return mCurrentPos == POS_START;
    }
    public boolean isInBottomPosition() {
        return mCurrentPos == POS_START;
    }

    public boolean crossRefreshLineFromTopToBottom() {
        return mLastPos < getOffsetToRefresh() && mCurrentPos >= getOffsetToRefresh();
    }

    public boolean crossLoadLineFromTopToBottom() {
        return (-mLastPos) < getOffsetToLoad() && (-mCurrentPos) >= getOffsetToLoad();
    }

    public boolean hasJustReachedHeaderHeightFromTopToBottom() {
        return mLastPos < mHeaderHeight && mCurrentPos >= mHeaderHeight;
    }

    public boolean isOverOffsetToKeepHeaderWhileLoading() {
        return mCurrentPos > getOffsetToKeepHeaderWhileLoading();
    }
    public boolean isOverOffsetToKeepFooterWhileLoading() {
        return (-mCurrentPos)>getOffsetToKeepFooterWhileLoading();
    }

    public void setOffsetToKeepHeaderWhileLoading(int offset) {
        mOffsetToKeepHeaderWhileLoading = offset;
    }

    public void setOffsetToKeepFooterWhileLoading(int offset) {
        mOffsetToKeepFooterWhileLoading = offset;
    }

    public int getOffsetToKeepHeaderWhileLoading() {
        return mOffsetToKeepHeaderWhileLoading >= 0 ? mOffsetToKeepHeaderWhileLoading : mHeaderHeight;
    }

    public int getOffsetToKeepFooterWhileLoading() {
        return mOffsetToKeepFooterWhileLoading >= 0 ? mOffsetToKeepFooterWhileLoading : mFooterHeight;
    }

    public boolean isAlreadyHere(int to) {
        return mCurrentPos == to;
    }

    public float getLastPercent() {
        final float oldPercent = mHeaderHeight == 0 ? 0 : mLastPos * 1f / mHeaderHeight;
        return oldPercent;
    }

    public float getCurrentPercent() {
        final float currentPercent = mHeaderHeight == 0 ? 0 : mCurrentPos * 1f / mHeaderHeight;
        return currentPercent;
    }

    public boolean willOverTop(int to) {
        return to < POS_START;
    }
    public boolean willOverBottom(int to) {
        return to > POS_START;
    }

    public void resetPtrIndicator(){
         mOffsetX=0;
         mOffsetY=0;
         mCurrentPos = 0;
         mLastPos = 0;
        mRefreshCompleteY=0;
    }
}
