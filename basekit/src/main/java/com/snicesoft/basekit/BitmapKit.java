package com.snicesoft.basekit;

public abstract class BitmapKit implements IBitmapKit {
	protected static BitmapKit instance;

	public synchronized static BitmapKit getInstance() {
		return instance;
	}
}
