package com.wanke.util;

import android.os.Handler;
import android.os.Message;

/**
 * 创建一个自定义的异步任务的处理类.
 * 
 * @author Administrator
 * 
 */
public abstract class MyAsyncTask {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			onPostExecute();
		};
	};

	/**
	 * 1. 在耗时任务执行之前调用的方法. 必须运行在主线程
	 */
	protected abstract void onPreExectue();

	/**
	 * 2. 在子线程, 在后台运行的耗时任务. 运行在子线程
	 */
	protected abstract void doInbackgroud();

	/**
	 * 3. 耗时任务执行完毕后调用的方法. 运行在主线程
	 */
	protected abstract void onPostExecute();

	/**
	 * 执行一个异步任务 必须在主线程里面才能调用.
	 */
	public void execute() {
		onPreExectue();
		new Thread() {
			public void run() {
				doInbackgroud(); // 发布进度
				Message msg = new Message();
				handler.sendMessage(msg);
			};
		}.start();
	}
}
