package com.example.kenju.shiningliveperformancecalculater

import android.content.Context
import android.widget.ListView
import android.view.MotionEvent
import android.view.WindowManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.widget.ImageView
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener


class SortableListView : ListView, OnItemLongClickListener {

    companion object {
        private val SCROLL_SPEED_FAST = 25
        private val SCROLL_SPEED_SLOW = 8
        private val DRAG_BITMAP_CONFIG = Bitmap.Config.ARGB_8888
    }

    // ソートモード
    var sortable = false

    private var mDragging = false
    private var mDragListener: DragListener? = SimpleDragListener()
    private var mBitmapBackgroundColor = Color.argb(128, 0xFF, 0xFF, 0xFF)
    private var mDragBitmap: Bitmap? = null
    private var mDragImageView: ImageView? = null
    private var mLayoutParams: WindowManager.LayoutParams? = null
    private var mActionDownEvent: MotionEvent? = null
    private var mPositionFrom = -1

    // WindowManager の取得
    protected val windowManager: WindowManager
        get() = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    // コンストラクタ
    constructor(context: Context) : super(context) {
        onItemLongClickListener = this
    }

    // コンストラクタ
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        onItemLongClickListener = this
    }

    // コンストラクタ
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        onItemLongClickListener = this
    }

    // ドラッグイベントリスナの設定
    fun setDragListener(listener: DragListener) {
        mDragListener = listener
    }

    // ソート中アイテムの背景色を設定
    override fun setBackgroundColor(color: Int) {
        mBitmapBackgroundColor = color
    }

    // MotionEvent から position を取得する
    private fun eventToPosition(event: MotionEvent?): Int {
        return pointToPosition(event!!.x.toInt(), event.y.toInt())
    }

    // タッチイベント処理
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!sortable) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                storeMotionEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                if (duringDrag(event)) {
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (stopDrag(event, true)) {
                    return true
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                if (stopDrag(event, false)) {
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    // リスト要素長押しイベント処理
    override fun onItemLongClick(parent: AdapterView<*>, view: View, position: Int, id: Long): Boolean {
        return startDrag()
    }

    // ACTION_DOWN 時の MotionEvent をプロパティに格納
    private fun storeMotionEvent(event: MotionEvent) {
        mActionDownEvent = MotionEvent.obtain(event) // 複製しないと値が勝手に変わる
    }

    // ドラッグ開始
    private fun startDrag(): Boolean {
        // イベントから position を取得
        mPositionFrom = eventToPosition(mActionDownEvent)

        // 取得した position が 0未満＝範囲外の場合はドラッグを開始しない
        if (mPositionFrom < 0) {
            return false
        }
        mDragging = true

        // View, Canvas, WindowManager の取得・生成
        val view = getChildByIndex(mPositionFrom)
        val canvas = Canvas()
        val wm = windowManager

        // ドラッグ対象要素の View を Canvas に描画
        mDragBitmap = Bitmap.createBitmap(view!!.getWidth(), view!!.getHeight(),
                DRAG_BITMAP_CONFIG)
        canvas.setBitmap(mDragBitmap)
        view!!.draw(canvas)

        // 前回使用した ImageView が残っている場合は除去（念のため？）
        if (mDragImageView != null) {
            wm.removeView(mDragImageView)
        }

        // ImageView 用の LayoutParams が未設定の場合は設定する
        if (mLayoutParams == null) {
            initLayoutParams()
        }

        // ImageView を生成し WindowManager に addChild する
        mDragImageView = ImageView(context)
        mDragImageView!!.setBackgroundColor(mBitmapBackgroundColor)
        mDragImageView!!.setImageBitmap(mDragBitmap)
        wm.addView(mDragImageView, mLayoutParams)

        // ドラッグ開始
        if (mDragListener != null) {
            mPositionFrom = mDragListener!!.onStartDrag(mPositionFrom)
        }
        return duringDrag(mActionDownEvent)
    }

    // ドラッグ処理
    private fun duringDrag(event: MotionEvent?): Boolean {
        if (!mDragging || mDragImageView == null) {
            return false
        }
        val x = event!!.x.toInt()
        val y = event.y.toInt()
        val height = height
        val middle = height / 2

        // スクロール速度の決定
        val speed: Int
        val fastBound = height / 9
        val slowBound = height / 4
        if (event.eventTime - event.downTime < 500) {
            // ドラッグの開始から500ミリ秒の間はスクロールしない
            speed = 0
        } else if (y < slowBound) {
            speed = if (y < fastBound) -SCROLL_SPEED_FAST else -SCROLL_SPEED_SLOW
        } else if (y > height - slowBound) {
            speed = if (y > height - fastBound)
                SCROLL_SPEED_FAST
            else
                SCROLL_SPEED_SLOW
        } else {
            speed = 0
        }

        // スクロール処理
        if (speed != 0) {
            // 横方向はとりあえず考えない
            var middlePosition = pointToPosition(0, middle)
            if (middlePosition == AdapterView.INVALID_POSITION) {
                middlePosition = pointToPosition(0, middle + dividerHeight
                        + 64)
            }
            val middleView = getChildByIndex(middlePosition)
            if (middleView != null) {
                setSelectionFromTop(middlePosition, middleView!!.getTop() - speed)
            }
        }

        // ImageView の表示や位置を更新
        if (mDragImageView!!.height < 0) {
            mDragImageView!!.visibility = View.INVISIBLE
        } else {
            mDragImageView!!.visibility = View.VISIBLE
        }
        updateLayoutParams(event.rawY.toInt()) // ここだけスクリーン座標を使う
        windowManager.updateViewLayout(mDragImageView, mLayoutParams)
        if (mDragListener != null) {
            mPositionFrom = mDragListener!!.onDuringDrag(mPositionFrom,
                    pointToPosition(x, y))
        }
        return true
    }

    // ドラッグ終了
    private fun stopDrag(event: MotionEvent, isDrop: Boolean): Boolean {
        if (!mDragging) {
            return false
        }
        if (isDrop && mDragListener != null) {
            mDragListener!!.onStopDrag(mPositionFrom, eventToPosition(event))
        }
        mDragging = false
        if (mDragImageView != null) {
            windowManager.removeView(mDragImageView)
            mDragImageView = null
            // リサイクルするとたまに死ぬけどタイミング分からない by vvakame
            // mDragBitmap.recycle();
            mDragBitmap = null

            mActionDownEvent!!.recycle()
            mActionDownEvent = null
            return true
        }
        return false
    }

    // 指定インデックスのView要素を取得する
    private fun getChildByIndex(index: Int): View? {
        return getChildAt(index - firstVisiblePosition)
    }

    // ImageView 用 LayoutParams の初期化
    protected fun initLayoutParams() {
        mLayoutParams = WindowManager.LayoutParams()
        mLayoutParams!!.gravity = Gravity.TOP or Gravity.LEFT
        mLayoutParams!!.height = WindowManager.LayoutParams.WRAP_CONTENT
        mLayoutParams!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        mLayoutParams!!.flags = (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        mLayoutParams!!.format = PixelFormat.TRANSLUCENT
        mLayoutParams!!.windowAnimations = 0
        mLayoutParams!!.x = left
        mLayoutParams!!.y = top
    }

    // ImageView 用 LayoutParams の座標情報を更新
    protected fun updateLayoutParams(rawY: Int) {
        mLayoutParams!!.y = rawY - 32
    }

    // ドラッグイベントリスナーインターフェース
    interface DragListener {
        // ドラッグ開始時の処理
        fun onStartDrag(position: Int): Int

        // ドラッグ中の処理
        fun onDuringDrag(positionFrom: Int, positionTo: Int): Int

        // ドラッグ終了＝ドロップ時の処理
        fun onStopDrag(positionFrom: Int, positionTo: Int): Boolean
    }

    // ドラッグイベントリスナー実装
    open class SimpleDragListener : DragListener {
        // ドラッグ開始時の処理
        override fun onStartDrag(position: Int): Int {
            return position
        }

        // ドラッグ中の処理
        override fun onDuringDrag(positionFrom: Int, positionTo: Int): Int {
            return positionFrom
        }

        // ドラッグ終了＝ドロップ時の処理
        override fun onStopDrag(positionFrom: Int, positionTo: Int): Boolean {
            return positionFrom != positionTo && positionFrom >= 0 || positionTo >= 0
        }
    }
}