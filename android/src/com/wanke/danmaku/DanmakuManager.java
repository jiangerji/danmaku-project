package com.wanke.danmaku;

import java.util.Date;

import master.flame.danmaku.controller.DrawHandler.Callback;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.android.DanmakuGlobalConfig;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.DanmakuFactory;
import master.flame.danmaku.ui.widget.DanmakuSurfaceView;
import android.graphics.Color;
import android.util.Log;

public class DanmakuManager {
    private final static String TAG = "DanmakuManager";

    private static DanmakuManager _instance = new DanmakuManager();

    /**
     * 获取弹幕管理器实例
     * 
     * @return
     */
    public static DanmakuManager getInstance() {
        return _instance;
    }

    private DanmakuSurfaceView mDanmakuView = null;

    /**
     * 使用DanmakuSurfaceView初始化，当不在使用该view时，请调用deinit接口释放资源
     * 
     * @param view
     *            显示弹幕的view
     */
    public void init(DanmakuSurfaceView view) {
        Log.d(TAG, "初始化弹幕:" + view);
        mDanmakuView = view;

        mDanmakuView.setCallback(new Callback() {

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void prepared() {
                mDanmakuView.start();
            }
        });

        mDanmakuView.showFPS(false);
        mDanmakuView.enableDanmakuDrawingCache(true);
        mDanmakuView.setDrawingThreadType(DanmakuSurfaceView.THREAD_TYPE_LOW_PRIORITY);

        // DanmakuGlobalConfig.DEFAULT.setDanmakuStyle(DanmakuGlobalConfig.DANMAKU_STYLE_STROKEN,
        // 3);

        createParser();
    }

    /**
     * 不再显示弹幕，释放相关资源
     */
    public void deinit() {
        Log.d(TAG, "释放弹幕资源！");
        if (mDanmakuView != null) {
            mDanmakuView.release();
        }
        mDanmakuView = null;
    }

    /**
     * 启动弹幕
     */
    public void start() {
        Log.d(TAG, "启动弹幕:" + new Date().toString());
        mDanmakuView.prepare(mParser);
    }

    private BaseDanmakuParser mParser;

    private BaseDanmakuParser createParser() {
        mParser = new BaseDanmakuParser() {

            @Override
            protected Danmakus parse() {
                return new Danmakus();
            }
        };

        return mParser;
    }

    /**
     * 显示弹幕
     */
    public void show() {
        Log.d(TAG, "显示弹幕:" + new Date().toString());
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.show();
        }
    }

    /**
     * 隐藏弹幕
     */
    public void hide() {
        Log.d(TAG, "隐藏弹幕:" + new Date().toString());
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.hide();
        }
    }

    /**
     * 停止弹幕更新显示
     */
    public void pause() {
        Log.d(TAG, "停止弹幕:" + new Date().toString());
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    /**
     * 继续显示弹幕
     */
    public void resume() {
        Log.d(TAG, "继续弹幕:" + new Date().toString());
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.resume();
        }
    }

    /**
     * 默认弹幕的类型，从右到左显示
     */
    private int mDefaultDanmakuType = BaseDanmaku.TYPE_SCROLL_RL;

    /**
     * 弹幕的类型
     * 
     * @author Administrator
     */
    public static enum DanmakuType {
        /**
         * 从右到左
         */
        R2L,
        /**
         * 从左到右
         */
        L2R,
        /**
         * 在顶部中间显示
         */
        TOP,
        /**
         * 在底部总监显示
         */
        BOTTOM
    }

    /**
     * 设置默认显示的弹幕类型
     * 
     * @param type
     *            弹幕类型
     * @return
     */
    public DanmakuManager setDanmakuType(DanmakuType type) {
        switch (type) {
        case R2L:
            mDefaultDanmakuType = BaseDanmaku.TYPE_SCROLL_RL;
            break;

        case L2R:
            mDefaultDanmakuType = BaseDanmaku.TYPE_SCROLL_LR;
            break;

        case TOP:
            mDefaultDanmakuType = BaseDanmaku.TYPE_FIX_TOP;
            break;

        case BOTTOM:
            mDefaultDanmakuType = BaseDanmaku.TYPE_FIX_BOTTOM;
            break;

        default:
            mDefaultDanmakuType = BaseDanmaku.TYPE_SCROLL_RL;
            break;
        }
        return this;
    }

    /**
     * 默认弹幕文字的大小
     */
    private int mDefaultDanmakuTextSize = 30;

    /**
     * 设置默认弹幕文字大小
     * 
     * @param size
     *            弹幕文字大小
     */
    public DanmakuManager setTextSize(int size) {
        if (size > 0) {
            mDefaultDanmakuTextSize = size;
        }

        return this;
    }

    /**
     * 默认弹幕文字的颜色
     */
    private int mDefaultDanmakuTextColor = Color.WHITE;

    /**
     * 设置默认弹幕文字的颜色
     * 
     * @param color
     *            文字的颜色，RGB格式
     * @return
     */
    public DanmakuManager setTextColor(int color) {
        mDefaultDanmakuTextColor = color;
        return this;
    }

    /**
     * 默认弹幕文字阴影的颜色
     */
    private int mDefaultDanmakuTextShadowColor = Color.BLACK;

    /**
     * 设置默认弹幕文字阴影颜色
     * 
     * @param color
     *            文字阴影颜色，RGB格式
     * @return
     */
    public DanmakuManager setTextShadowColor(int color) {
        mDefaultDanmakuTextShadowColor = color;
        return this;
    }

    /**
     * 默认弹幕文字下划线的颜色, 0表示没有下划线
     */
    private int mDefaultDanmakuUnderlineColor = 0;

    /**
     * 设置弹幕文字下划线的颜色，0表示没有下划线
     * 
     * @param color
     *            下划线颜色，0表示没有下划线
     * @return
     */
    public DanmakuManager setUnderlineColor(int color) {
        mDefaultDanmakuUnderlineColor = color;
        return this;
    }

    /**
     * 默认弹幕文字边框的颜色, 0表示没有下划线
     */
    private int mDefaultDanmakuBorderColor = 0;

    /**
     * 设置弹幕文字边框的颜色，0表示没有文字边框
     * 
     * @param color
     *            文字边框颜色，0表示没有文字边框
     * @return
     */
    public DanmakuManager setBorderColor(int color) {
        mDefaultDanmakuBorderColor = color;
        return this;
    }

    /**
     * 发送一条普通的弹幕
     * 
     * @param text
     *            弹幕文字
     */
    public void sendDanmaku(String text) {
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            BaseDanmaku danmaku = DanmakuFactory.createDanmaku(mDefaultDanmakuType);

            danmaku.text = text;
            danmaku.padding = 5;
            danmaku.priority = 1;// 0为低优先级,>0为高优先级
            danmaku.time = mParser.getTimer().currMillisecond + 100;
            danmaku.textSize = mDefaultDanmakuTextSize
                    * (mParser.getDisplayer().getDensity() - 0.6f);
            danmaku.textColor = mDefaultDanmakuTextColor;
            danmaku.textShadowColor = mDefaultDanmakuTextShadowColor;
            danmaku.underlineColor = mDefaultDanmakuUnderlineColor;
            danmaku.borderColor = mDefaultDanmakuBorderColor;

            mDanmakuView.addDanmaku(danmaku);
        }
    }

    /**
     * DANMAKU_STYLE_NONE: 没有描边, DANMAKU_STYLE_SHADOW: 阴影描边,
     * DANMAKU_STYLE_STROKEN: 使用描边, DANMAKU_STYLE_DEFAULT: 使用默认，默认是描边
     */
    public enum DanmakuStyle {
        DANMAKU_STYLE_NONE, DANMAKU_STYLE_SHADOW, DANMAKU_STYLE_STROKEN, DANMAKU_STYLE_DEFAULT
    }

    /**
     * 设置弹幕描边样式
     * 
     * @param style
     *            弹幕描边的样式
     * @param size
     *            描边的大小
     * @return
     */
    public DanmakuManager
            setDanmakuStyle(DanmakuStyle danmakuStyle, float size) {
        Log.d(TAG, "setDanmakuStyle: danmakuStyle=" + danmakuStyle + ", size="
                + size);
        int style = DanmakuGlobalConfig.DANMAKU_STYLE_NONE;
        switch (danmakuStyle) {
        case DANMAKU_STYLE_SHADOW:
            style = DanmakuGlobalConfig.DANMAKU_STYLE_SHADOW;
            break;

        case DANMAKU_STYLE_DEFAULT:
        case DANMAKU_STYLE_STROKEN:
            style = DanmakuGlobalConfig.DANMAKU_STYLE_STROKEN;
            break;

        default:
            break;
        }
        DanmakuGlobalConfig.DEFAULT.setDanmakuStyle(style, size);

        return this;
    }

}

/**
 * int types[] = { BaseDanmaku.TYPE_SCROLL_RL, BaseDanmaku.TYPE_SCROLL_LR,
 * BaseDanmaku.TYPE_FIX_BOTTOM, BaseDanmaku.TYPE_FIX_TOP,
 * BaseDanmaku.TYPE_SPECIAL, 0x1111 }; Random random = new
 * Random(System.currentTimeMillis()); int type =
 * types[random.nextInt(types.length)]; String info = "这是一条弹幕！"; switch (type) {
 * case BaseDanmaku.TYPE_SCROLL_RL: info = "这是从右到左的弹幕"; break; case
 * BaseDanmaku.TYPE_SCROLL_LR: info = "这是从左到右的弹幕"; break; case
 * BaseDanmaku.TYPE_FIX_BOTTOM: info = "这是底部的弹幕"; break; case
 * BaseDanmaku.TYPE_FIX_TOP: info = "这是定部的弹幕"; break; case
 * BaseDanmaku.TYPE_SPECIAL: info = "这是特殊的弹幕"; break; case 0x1111: type =
 * BaseDanmaku.TYPE_SCROLL_RL; info =
 * "这是好长好长的弹幕！这是好长好长的弹幕！这是好长好长的弹幕！\n这是好长好长的弹幕！这是好长好长的弹幕！\n这是好长好长的弹幕！\n这是好长好长的弹幕！\n这是好长好长的弹幕！"
 * ; break; default: break; } BaseDanmaku danmaku =
 * DanmakuFactory.createDanmaku(type, mDanmakuView.getWidth()); StringBuffer sb
 * = new StringBuffer(); // for(int i=0;i<100;i++){ sb.append(info); // }
 * danmaku.text = sb.toString(); danmaku.lines = info.split("\n");
 * danmaku.padding = 5; danmaku.priority = 1; Log.d("haha", "" +
 * mParser.getTimer().currMillisecond); danmaku.time =
 * mParser.getTimer().currMillisecond + 100; danmaku.textSize = (18 +
 * random.nextInt(100)) (mParser.getDisplayer().getDensity() - 0.6f);
 * danmaku.textColor = Color.RED; danmaku.textShadowColor = Color.WHITE; //
 * danmaku.underlineColor = Color.GREEN; danmaku.borderColor = Color.GREEN; if
 * (type == BaseDanmaku.TYPE_SPECIAL) { danmaku.duration = new Duration(2000);
 * DanmakuFactory.fillTranslationData(danmaku, mDanmakuView.getWidth(),
 * mDanmakuView.getHeight(), 0, 0, 0, 0, 1000, 0); String pathsString =
 * "M67,264L66,264L230,263"; if (a) { pathsString = "M67,220L67,300L230,320"; }
 * a = !a; String motionPathString = pathsString.substring(1); String[]
 * pointStrArray = motionPathString.split("L"); if (pointStrArray != null &&
 * pointStrArray.length > 0) { float[][] points = new
 * float[pointStrArray.length][2]; for (int i = 0; i < pointStrArray.length;
 * i++) { String[] pointArray = pointStrArray[i].split(","); points[i][0] =
 * Float.parseFloat(pointArray[0]); points[i][1] =
 * Float.parseFloat(pointArray[1]); } DanmakuFactory.fillLinePathData(danmaku,
 * mDanmakuView.getWidth(), mDanmakuView.getHeight(), points); } }
 */
